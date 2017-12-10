package com.lvsocialsdk.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.lvsocialsdk.listener.LoginStatusCallBack;
import com.lvsocialsdk.SocialConfig;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lvzishen on 16/12/8.
 * 微信分发管理类
 */
public class WeChatCore {

    private static volatile WeChatCore weChatCore;
    private IWXAPI mApi;
    WeChatLogin weChatLogin;

    public static WeChatCore getInstance(Context context) {
        if (weChatCore == null) {
            synchronized (WeChatShare.class) {
                if (weChatCore == null) {
                    weChatCore = new WeChatCore(context);
                }
            }
        }
        return weChatCore;
    }

    private WeChatCore(Context context) {
        mApi = WXAPIFactory.createWXAPI(context.getApplicationContext(),
                SocialConfig.wechat_key);
        mApi.registerApp(SocialConfig.wechat_key);
    }

    public void login(Activity activity, boolean mRequestInfoEnable, LoginStatusCallBack loginStatusCallBack) {
        weChatLogin = new WeChatLogin(activity);
        weChatLogin.setmRequestInfoEnable(mRequestInfoEnable);
        weChatLogin.setLoginStatusCallBack(loginStatusCallBack);
        weChatLogin.loginWechat(mApi);
    }

    public WeChatShare share() {
        return WeChatShare.getInstance(mApi);
    }



    /**
     * 处理分享结果
     *
     * @param intent  {@link Intent}
     * @param handler IWXAPIEventHandler
     */
    public void handleResponse(Intent intent, IWXAPIEventHandler handler) {
        mApi.handleIntent(intent, handler);
    }

    public void handleResponseWithLogin(SendAuthWechat baseResp) {
        if (weChatLogin != null) {
            weChatLogin.handleResponse(baseResp);
        }
    }
}
