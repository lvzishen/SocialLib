package com.lvsocialsdk.qq;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.lvsocialsdk.utils.GsonUtil;
import com.lvsocialsdk.listener.LoginStatusCallBack;
import com.lvsocialsdk.SocialConfig;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by lvzishen on 16/12/2.
 */
public class QQLogin {
    Tencent mTencent;
    Activity activity;
    private boolean mRequestInfoEnable = true;  //是否用第三方自带的数据作为整个app的登录数据
    private String appKey;

    public void setmRequestInfoEnable(boolean mRequestInfoEnable) {
        this.mRequestInfoEnable = mRequestInfoEnable;
    }

    private LoginStatusCallBack loginStatusCallBack;

    public QQLogin(Activity activity) {
        this.activity = activity;
        this.appKey = SocialConfig.qq_key;
    }

    public void setLoginStatusCallBack(LoginStatusCallBack loginStatusCallBack) {
        this.loginStatusCallBack = loginStatusCallBack;
    }

    public void loginQQ() {
        mTencent = Tencent.createInstance(appKey, activity.getApplicationContext());
        mTencent.login(activity, "all", mAuthListener);
    }


    private IUiListener mAuthListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                loginStatusCallBack.onAuthFailed();
                // release resource
                mTencent.releaseResource();
                return;
            }

            QQLoginResult result = GsonUtil.fromJson(response + "", QQLoginResult.class);
            if (SocialConfig.isDebug) {
                Log.i(SocialConfig.TAG, result.toString());
            }

            mTencent.setAccessToken(result.access_token, result.expires_in + "");
            mTencent.setOpenId(result.openid);
            long expiresIn = mTencent.getExpiresIn();
            if (mRequestInfoEnable) {
                loginStatusCallBack.onLoginIng();
                // request user info
                new UserInfo(activity, mTencent.getQQToken())
                        .getUserInfo(mGetInfoListener);
            } else {
                loginStatusCallBack.onAuthSuccess(result);
            }
        }

        @Override
        public void onError(UiError e) {
            loginStatusCallBack.onAuthFailed();
            // release resource
            mTencent.releaseResource();
        }

        @Override
        public void onCancel() {
            loginStatusCallBack.onAuthCancel();
            // release resource
            mTencent.releaseResource();
        }
    };


    private IUiListener mGetInfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                loginStatusCallBack.onLoginFailed();
                // release resource
                mTencent.releaseResource();
                return;
            }

//             request user info success
            QQUserInfo info = GsonUtil.fromJson(response + "", QQUserInfo.class);
            loginStatusCallBack.onLoginSuccess(info);

            // release resource
            mTencent.releaseResource();
        }

        @Override
        public void onError(UiError e) {
            loginStatusCallBack.onLoginFailed();
            // release resource
            mTencent.releaseResource();
        }

        @Override
        public void onCancel() {
            loginStatusCallBack.onAuthCancel();
            // release resource
            mTencent.releaseResource();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
    }
}
