package com.lvsocialsdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Locale;

/**
 * Created by lvzishen on 16/12/7.
 */
public class SocialConfig {


    public static final String TAG = "SOCIAL_INFO";

    public static boolean isDebug = true;

    public static volatile SocialConfig socialConfig;
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static String weibo_key;

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static String weibo_redirect_url;

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static String weibo_scope = "";

    public static String qq_key = "";

    public static String wechat_key = "";

    public static String wechat_serect = "";
    //授权域，snsapi_userinfo 表示获取用户个人信息
    public static String wechat_scope = "";

    public static String wechat_state = "";

    private SocialConfig() {
    }

    public static SocialConfig getInstance() {
        if (socialConfig == null) {
            synchronized (SocialConfig.class) {
                if (socialConfig == null) {
                    socialConfig = new SocialConfig();
                }
            }
        }
        return socialConfig;
    }

    public SocialConfig initQQ(String qq_key) {
        this.qq_key = qq_key;
        return this;
    }

    public SocialConfig initWeiBo(String weibo_key, String weibo_redirect_url, String weibo_scope) {
        this.weibo_key = weibo_key;
        this.weibo_redirect_url = weibo_redirect_url;
        this.weibo_scope = weibo_scope;
        return this;
    }

    public SocialConfig initWechat(String wechat_key, String wechat_serect, String wechat_scope, String wechat_state) {
        this.wechat_key = wechat_key;
        this.wechat_serect = wechat_serect;
        this.wechat_scope = wechat_scope;
        this.wechat_state = wechat_state;
        return this;
    }

    public SocialConfig isDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 判断第三方客户端是否安装
    ///////////////////////////////////////////////////////////////////////////

    public static boolean isQQInstalled(@NonNull Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        if (pm == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.tencent.mobileqq".equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeiBoInstalled(@NonNull Context context) {
        IWeiboShareAPI shareAPI = WeiboShareSDK.createWeiboAPI(context, SocialConfig.weibo_key);
        return shareAPI.isWeiboAppInstalled();
    }

    public static boolean isWeiXinInstalled(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, SocialConfig.wechat_key, true);
        return api.isWXAppInstalled();
    }


    /**
     * 是否支持发送朋友圈
     *
     * @return is supported
     */
    public static boolean isSupportedWechatTimeLine(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, SocialConfig.wechat_key, true);
        return api.isWXAppSupportAPI();
    }

}
