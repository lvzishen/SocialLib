package com.lvsocialsdk.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lvsocialsdk.utils.GsonUtil;
import com.lvsocialsdk.listener.LoginStatusCallBack;
import com.lvsocialsdk.SocialConfig;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * Created by lvzishen on 16/12/2.
 */
public class WeiBoLogin {

    /**
     * weibo api.
     */
    private static final String URL_GET_USER_INFO = "https://api.weibo.com/2/users/show.json";


    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Activity activity;
    private boolean mRequestInfoEnable = true;  //是否用第三方自带的数据作为整个app的登录数据
    private String appKey;
    private String redirect_Url;
    private String scope;
    private LoginStatusCallBack loginStatusCallBack;

    public void setmRequestInfoEnable(boolean mRequestInfoEnable) {
        this.mRequestInfoEnable = mRequestInfoEnable;
    }

    public WeiBoLogin(Activity activity) {
        this.activity = activity;
        this.appKey = SocialConfig.weibo_key;
        this.redirect_Url = SocialConfig.weibo_redirect_url;
        this.scope = SocialConfig.weibo_scope;
    }

    public void setLoginStatusCallBack(LoginStatusCallBack loginStatusCallBack) {
        this.loginStatusCallBack = loginStatusCallBack;
    }

    public void loginWeiBo() {
        mAuthInfo = new AuthInfo(activity, appKey, redirect_Url, scope);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mSsoHandler.authorize(mAuthListener);

    }


    private WeiboAuthListener mAuthListener = new WeiboAuthListener() {

        @Override
        public void onComplete(Bundle values) {
            //    解析values结果
            WeiboLoginResult result = new WeiboLoginResult();
            result.access_token = values.getString("access_token");
            result.expires_in = values.getString("expires_in");
            result.remind_in = values.getString("remind_in");
            result.uid = values.getString("uid");
            result.userName = values.getString("userName");
            result.refresh_token = values.getString("refresh_token");
            if (SocialConfig.isDebug) {
                Log.i(SocialConfig.TAG, result.toString());
            }


            if (mRequestInfoEnable) {
                loginStatusCallBack.onLoginIng();
//                 request user info
                requestUserInfo(result);
            } else {
                loginStatusCallBack.onAuthSuccess(result);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            loginStatusCallBack.onAuthException(e);
        }

        @Override
        public void onCancel() {
            loginStatusCallBack.onAuthCancel();

        }
    };


    /**
     * request user information.
     *
     * @param result the login result.
     */
    private void requestUserInfo(final WeiboLoginResult result) {
        // 2. 调用接口发送微博
        WeiboParameters params = new WeiboParameters(appKey);
        params.put("uid", result.uid);
        params.put("access_token", result.access_token);
        params.put("source", appKey);
        new AsyncWeiboRunner(activity.getApplicationContext()).requestAsync(
                URL_GET_USER_INFO,
                params,
                "GET",
                new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            WeiboUserInfo info = GsonUtil.fromJson(s, WeiboUserInfo.class);
                            loginStatusCallBack.onLoginSuccess(info);
                        } else {
                            loginStatusCallBack.onLoginFailed();
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        //log
                        if(SocialConfig.isDebug){
                            Log.i(SocialConfig.TAG,e.toString());
                        }
                        loginStatusCallBack.onLoginFailed();
                    }
                });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }

}
