package com.lvsocialsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.lvsocialsdk.listener.ActivityLifeListener;
import com.lvsocialsdk.listener.LoginStatusCallBack;
import com.lvsocialsdk.qq.QQLogin;
import com.lvsocialsdk.wechat.SendAuthWechat;
import com.lvsocialsdk.wechat.WeChatCore;
import com.lvsocialsdk.weibo.WeiBoLogin;

/**
 * Created by lvzishen on 16/12/8.
 * <p>
 * 第三方登录管理类
 */
public class LoginManager implements LoginStatusCallBack {
    private static volatile LoginManager loginManager;
    public static final int TYPE_WEIBO = 0x01;

    public static final int TYPE_QQ = 0x02;


    public static final int TYPE_WECHAT = 0x03;

    public static final String WECHAT_LOGIN_RESULT = "wechat_login_result";

    public static final String WECHAT_LOGIN_RESP = "wechat_login_resp";

    public SocailActivity activity;


    private static LoginStatusCallBack loginStatusCallBack;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (loginManager == null) {
            synchronized (LoginManager.class) {
                if (loginManager == null) {
                    loginManager = new LoginManager();
                }
            }
        }
        return loginManager;
    }

    /**
     * @param activity
     * @param login_type
     * @param mRequestInfoEnable  是否需要直接使用第三方数据(定义为否则直接获得AuthResult数据,可根据其中数据自行请求自己的接口,如果没有自己的登录系统则设置为true)
     * @param loginStatusCallBack
     */
    public void login(final SocailActivity activity, int login_type, boolean mRequestInfoEnable, LoginStatusCallBack loginStatusCallBack) {
        this.activity = activity;
        this.loginStatusCallBack = loginStatusCallBack;
        switch (login_type) {
            case TYPE_WEIBO:
                if (!SocialConfig.isWeiBoInstalled(activity)) {
                    Log.i(SocialConfig.TAG, "请先安装微博");
                    loginStatusCallBack.onNotInstall("请先安装微博");
                    return;
                }
                final WeiBoLogin weiBoLogin = new WeiBoLogin(activity);
                weiBoLogin.setmRequestInfoEnable(mRequestInfoEnable);
                weiBoLogin.setLoginStatusCallBack(this);
                activity.setActivityLife(new ActivityLifeListener() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        weiBoLogin.onActivityResult(requestCode, resultCode, data);
                        activity.setActivityLife(null);
                    }
                });
                weiBoLogin.loginWeiBo();
                break;

            case TYPE_QQ:
                if (!SocialConfig.isQQInstalled(activity)) {
                    Log.i(SocialConfig.TAG, "请先安装QQ");
                    loginStatusCallBack.onNotInstall("请先安装QQ");
                    return;
                }
                final QQLogin qqLogin = new QQLogin(activity);
                qqLogin.setmRequestInfoEnable(mRequestInfoEnable);
                qqLogin.setLoginStatusCallBack(this);
                activity.setActivityLife(new ActivityLifeListener() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        qqLogin.onActivityResult(requestCode, resultCode, data);
                        activity.setActivityLife(null);
                    }
                });
                qqLogin.loginQQ();
                break;

            case TYPE_WECHAT:
                if (!SocialConfig.isWeiXinInstalled(activity)) {
                    Log.i(SocialConfig.TAG, "请先安装微信");
                    loginStatusCallBack.onNotInstall("请先安装微信");
                    return;
                }
                //注册更新广播
                IntentFilter filter = new IntentFilter();
                filter.addAction(WECHAT_LOGIN_RESULT);
                LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(mReceiver, filter);
                WeChatCore.getInstance(activity).login(activity, mRequestInfoEnable, loginStatusCallBack);
                break;
        }


    }


    //接收微信回调信息广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WECHAT_LOGIN_RESULT:
                    SendAuthWechat baseResp = intent.getParcelableExtra(WECHAT_LOGIN_RESP);
                    WeChatCore.getInstance(activity).handleResponseWithLogin(baseResp);
                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(mReceiver);
                    break;

            }

        }
    };


    /**
     * @param data data instanceof WeiboUserInfo is from weibo
     *             data instanceof QQUserInfo is from qq
     *             data instanceof WechatUserInfo is from wechat
     */
    @Override
    public void onLoginSuccess(Object data) {
        loginStatusCallBack.onLoginSuccess(data);
    }

    @Override
    public void onLoginFailed() {
        loginStatusCallBack.onLoginFailed();
    }

    @Override
    public void onLoginIng() {
        loginStatusCallBack.onLoginIng();
    }

    /**
     * @param data data instanceof WeiboLoginResult is from weibo
     *             data instanceof QQLoginResult is from qq
     *             data instanceof WechatLoginResult is from wechat
     */
    @Override
    public void onAuthSuccess(Object data) {
        loginStatusCallBack.onAuthSuccess(data);
    }

    @Override
    public void onAuthFailed() {
        loginStatusCallBack.onAuthFailed();
    }

    @Override
    public void onAuthCancel() {
        loginStatusCallBack.onAuthCancel();
    }

    @Override
    public void onAuthException(Exception e) {
        loginStatusCallBack.onAuthException(e);
    }

    @Override
    public void onNotInstall(String message) {

    }


}
