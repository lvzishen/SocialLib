package com.lvsocialsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.lvsocialsdk.listener.ActivityLifeListener;
import com.lvsocialsdk.listener.ShareStatusCallBack;
import com.lvsocialsdk.qq.QQShare;
import com.lvsocialsdk.wechat.SendAuthWechat;
import com.lvsocialsdk.wechat.WeChatCore;
import com.lvsocialsdk.wechat.WeChatShare;
import com.lvsocialsdk.weibo.WeiboShare;


/**
 * Created by lvzishen on 16/12/8.
 * <p/>
 * 分享管理类
 */
public class ShareManager {
    private static volatile ShareManager shareManager;

    public ShareStatusCallBack shareStatusCallBack;

    private SocailActivity activity;

    public static final String WECHAT_SHARE = "wechat_share";

    public static final String WECHAT_SHARE_RESULT = "wechat_share_result";

    public static final String WEIBO_SHARE = "weibo_share";

    public static final String WEIBO_SHARE_RESULT = "weibo_share_result";

    private ShareManager() {
    }

    public static ShareManager getInstance() {
        if (shareManager == null) {
            synchronized (ShareManager.class) {
                if (shareManager == null) {
                    shareManager = new ShareManager();
                }
            }
        }
        return shareManager;
    }


    public QQShare shareWithQQ(final SocailActivity activity, ShareStatusCallBack shareStatusCallBack) {
        this.activity = activity;
        if (!SocialConfig.isQQInstalled(activity)) {
            Log.i(SocialConfig.TAG, "请先安装QQ");
            shareStatusCallBack.onShareFailed();
            return null;
        }
        final QQShare qqShare = QQShare.getInstance(activity.getApplicationContext());
        qqShare.setShareStatusCallBack(shareStatusCallBack);
        activity.setActivityLife(new ActivityLifeListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                qqShare.onActivityResult(requestCode, resultCode, data);
                activity.setActivityLife(null);
            }
        });
        return qqShare;
    }


    public WeChatShare shareWithWechat(final SocailActivity activity, ShareStatusCallBack shareStatusCallBack) {
        this.shareStatusCallBack = shareStatusCallBack;
        this.activity = activity;
        if (!SocialConfig.isWeiXinInstalled(activity)) {
            Log.i(SocialConfig.TAG, "请先安装微信");
            shareStatusCallBack.onShareFailed();
            return null;
        }
        if (!SocialConfig.isSupportedWechatTimeLine(activity)) {
            Log.i(SocialConfig.TAG, "您的微信版本不支持微信朋友圈分享,请下载最新版微信");
            shareStatusCallBack.onShareFailed();
            return null;
        }
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(WECHAT_SHARE);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(mReceiver, filter);
        return WeChatCore.getInstance(activity).share();
    }

    public WeiboShare shareWithWeibo(final SocailActivity activity, ShareStatusCallBack shareStatusCallBack) {
        this.shareStatusCallBack = shareStatusCallBack;
        this.activity = activity;
        if (!SocialConfig.isWeiBoInstalled(activity)) {
            Log.i(SocialConfig.TAG, "请先安装微博");
            shareStatusCallBack.onShareFailed();
            return null;
        }
        final WeiboShare weiboShare = WeiboShare.getInstance(activity);
        activity.setActivityLife(new ActivityLifeListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                weiboShare.onActivityResult(requestCode, resultCode, data);
                activity.setActivityLife(null);
            }
        });
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(WEIBO_SHARE);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(mReceiver, filter);
        return weiboShare;
    }

    //接收微信微博回调信息广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WECHAT_SHARE:
                    String resultWechat = intent.getStringExtra(WECHAT_SHARE_RESULT);
                    switch (resultWechat) {
                        case "分享成功":
                            shareStatusCallBack.onShareSuccess();
                            break;
                        case "分享失败":
                            shareStatusCallBack.onShareFailed();
                            break;
                        case "取消分享":
                            shareStatusCallBack.onShareCanceled();
                            break;
                    }
                    break;

                case WEIBO_SHARE:
                    String resultWeibo = intent.getStringExtra(WEIBO_SHARE_RESULT);
                    switch (resultWeibo) {
                        case "分享成功":
                            shareStatusCallBack.onShareSuccess();
                            break;
                        case "分享失败":
                            shareStatusCallBack.onShareFailed();
                            break;
                        case "取消分享":
                            shareStatusCallBack.onShareCanceled();
                            break;
                    }
                    break;

            }
            LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(mReceiver);
        }
    };

}
