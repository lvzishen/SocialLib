package com.lvsocialsdk.wechat;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.lvsocialsdk.utils.GsonUtil;
import com.lvsocialsdk.listener.LoginStatusCallBack;
import com.lvsocialsdk.SocialConfig;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;


/**
 * Created by lvzishen on 16/12/5.
 */
public class WeChatLogin {
    private boolean mRequestInfoEnable = true;  //是否用第三方自带的数据作为整个app的登录数据
    private Activity activity;
    private LoginStatusCallBack loginStatusCallBack;
    private String appKey;
    private String scope;
    private String state;
    private String appSecret;

    /**
     * Get token url.
     */
    private static final String URL_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * Get user info url.
     */
    private static final String URL_WECHAT_USER = "https://api.weixin.qq.com/sns/userinfo";

    public WeChatLogin(Activity activity) {
        this.activity = activity;
        this.appKey = SocialConfig.wechat_key;
        this.appSecret = SocialConfig.wechat_serect;
        this.scope = SocialConfig.wechat_scope;
        this.state = SocialConfig.wechat_state;
    }


    public void setLoginStatusCallBack(LoginStatusCallBack loginStatusCallBack) {
        this.loginStatusCallBack = loginStatusCallBack;
    }


    public void setmRequestInfoEnable(boolean mRequestInfoEnable) {
        this.mRequestInfoEnable = mRequestInfoEnable;
    }


    public void loginWechat(IWXAPI api) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = scope; //授权域，snsapi_userinfo 表示获取用户个人信息
        req.state = state;
        api.sendReq(req);
    }

    /**
     * handle response from wechat.
     *
     * @param response the response
     */
    public void handleResponse(SendAuthWechat response) {
        switch (response.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = response.code;
                if (!TextUtils.isEmpty(code)) {
                    requestToken(code);
                } else {
                    loginStatusCallBack.onAuthFailed();
                }
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                loginStatusCallBack.onAuthFailed();
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                loginStatusCallBack.onAuthCancel();
                break;
        }
    }


    /**
     * request token from wechat server.
     *
     * @param code the auth code
     */
    private void requestToken(String code) {
        WeiboParameters params = new WeiboParameters(appKey);
        params.put("appid", appKey);
        params.put("secret", appSecret);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        new AsyncWeiboRunner(activity.getApplicationContext()).requestAsync(
                URL_TOKEN,
                params,
                "POST",
                new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        WechatLoginResult result = GsonUtil.fromJson(s, WechatLoginResult.class);
//
                        if (SocialConfig.isDebug) {
                            Log.i(SocialConfig.TAG, result.toString());
                        }

                        if (mRequestInfoEnable) {
                            loginStatusCallBack.onLoginIng();
                            // request user info
                            requestUserInfo(result.openid, result.access_token);
                        } else {
                            loginStatusCallBack.onAuthSuccess(result);
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        loginStatusCallBack.onAuthFailed();
                    }
                });

    }

    /**
     * request user information.
     *
     * @param openId      the app id
     * @param accessToken the access token
     */
    private void requestUserInfo(String openId, String accessToken) {
        // 2. 调用接口发送微博
        WeiboParameters params = new WeiboParameters(appKey);
        params.put("access_token", accessToken);
        params.put("openid", openId);
        new AsyncWeiboRunner(activity.getApplicationContext()).requestAsync(
                URL_WECHAT_USER,
                params,
                "POST",
                new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            WechatUserInfo info = GsonUtil.fromJson(s, WechatUserInfo.class);
                            loginStatusCallBack.onLoginSuccess(info);
                        } else {
                            loginStatusCallBack.onLoginFailed();
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        loginStatusCallBack.onLoginFailed();
                    }
                });


    }

}
