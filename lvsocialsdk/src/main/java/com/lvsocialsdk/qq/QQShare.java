package com.lvsocialsdk.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lvsocialsdk.SocialConfig;
import com.lvsocialsdk.listener.ShareStatusCallBack;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * QQAPI 文档 http://wiki.open.qq.com/index.php?title=Android_API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E&=45038#1.14_.E5.88.86.E4.BA.AB.E5.88.B0QQ.E7.A9.BA.E9.97.B4.EF.BC.88.E6.97.A0.E9.9C.80QQ.E7.99.BB.E5.BD.95.EF.BC.89
 * <p>
 * <p>
 * Created by lvzishen on 16/12/6.
 */
public class QQShare {

    private static final int TYPE_QQ = 0;
    private static final int TYPE_QZONE = 1;


    private ShareStatusCallBack shareStatusCallBack;

    private static QQShare sInstance;

    private Tencent mTencent;


    private QQShare(Context context) {
        mTencent = Tencent.createInstance(SocialConfig.qq_key,
                context.getApplicationContext());
    }

    public synchronized static QQShare getInstance(Context context) {
        if (sInstance == null) {
            synchronized (QQShare.class) {
                if (sInstance == null) {
                    sInstance = new QQShare(context);
                }
            }
        }
        return sInstance;
    }

    public void setShareStatusCallBack(ShareStatusCallBack shareStatusCallBack) {
        this.shareStatusCallBack = shareStatusCallBack;
    }


    /**
     * 分享到qq好友(本地图片，注：如果没有安装qq客户端，有问题，坑啊)
     *
     * @param activity  activity
     * @param title     标题
     * @param summary   分享内容、摘要 可选填
     * @param targetUrl 点击跳转的url
     * @param imagePath 图片路径
     */
    public void shareLocalImage(Activity activity, String title, String summary,
                                String targetUrl, String imagePath) {
        final Bundle params = new Bundle();
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, title);
        if (!TextUtils.isEmpty(summary)) {
            params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, summary);
        }
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath); //本地图片
        mTencent.shareToQQ(activity, params, new BaseUiListener(TYPE_QQ));
    }

    /**
     * 分享到qq好友（网络图片）
     * 分享图文消息
     *
     * @param activity  activity
     * @param title     标题
     * @param summary   分享内容、摘要    可选填
     * @param targetUrl 点击跳转的url
     * @param imageUrl  图片url          可选填
     */
    public void shareToQQWithNetworkImage(Activity activity, String title, String summary,
                                          String targetUrl, String imageUrl) {
        final Bundle params = new Bundle();
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, title);
        if (!TextUtils.isEmpty(summary)) {
            params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, summary);
        }
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl); //网络图片
        }
        mTencent.shareToQQ(activity, params, new BaseUiListener(TYPE_QQ));
    }

    /**
     * 分享本地图片到QQ 分享纯图片
     *
     * @param activity activity
     * @param imageUrl 图片URL
     */
    public void shareToQQWithImage(Activity activity, String imageUrl) {
        final Bundle params = new Bundle();
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        mTencent.shareToQQ(activity, params, new BaseUiListener(TYPE_QQ));
    }


    /**
     * 分享网络音乐链接到QQ 分享音乐
     *
     * @param activity  activity
     * @param title     标题 可选填写
     * @param summary   分享内容、摘要 可选填写
     * @param targetUrl 点击跳转的url
     * @param imageUrl  图片url 可选填写
     * @param audioUrl  音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
     */
    private void shareToQQWithAudio(Activity activity, String title, String summary, String targetUrl, String imageUrl, String audioUrl) {
        final Bundle params = new Bundle();
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        if (!TextUtils.isEmpty(title)) {
            params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, title);
        }
        if (!TextUtils.isEmpty(summary)) {
            params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, summary);
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        }
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);
        mTencent.shareToQQ(activity, params, new BaseUiListener(TYPE_QQ));
    }


    /**
     * 分享到qq空间 （图文模式）
     * 注意:QZone接口暂不支持发送多张图片的能力，若传入多张图片，则会自动选入第一张图片作为预览图。多图的能力将会在以后支持。
     *
     * @param activity  activity
     * @param title     标题
     * @param summary   内容、摘要 选填
     * @param targetUrl 跳转url
     * @param imageUrls 图片url集合 选填
     */
    public void shareToQzoneWithNetWorkImages(Activity activity, String title,
                                              String summary, String targetUrl,
                                              ArrayList<String> imageUrls) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        if (imageUrls != null && imageUrls.size() > 0) {
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        }
        mTencent.shareToQzone(activity, params, new BaseUiListener(TYPE_QZONE));
    }


    private class BaseUiListener implements IUiListener {
        private final int mShareType;

        public BaseUiListener(int shareType) {
            mShareType = shareType;
        }


        @Override
        public void onComplete(Object response) {
            // TODO
            switch (mShareType) {
                case TYPE_QQ:
                    if (SocialConfig.isDebug) {
                        Log.e(SocialConfig.TAG, "QQ share success: " + "friend");
                    }
                    if (shareStatusCallBack != null) {
                        shareStatusCallBack.onShareSuccess();
                    }
                    break;

                case TYPE_QZONE:
                    if (SocialConfig.isDebug) {
                        Log.e(SocialConfig.TAG, "QQ share success: " + "zone");
                    }
                    if (shareStatusCallBack != null) {
                        shareStatusCallBack.onShareSuccess();
                    }
                    break;
            }
        }

        @Override
        public void onError(UiError e) {
            // TODO
            if (SocialConfig.isDebug) {
                Log.e(SocialConfig.TAG, "QQ share error: " + e.errorMessage);
            }
            if (shareStatusCallBack != null) {
                shareStatusCallBack.onShareFailed();
            }
        }

        @Override
        public void onCancel() {
            // TODO
            Log.e(SocialConfig.TAG, "QQ share cancel");
            if (shareStatusCallBack != null) {
                shareStatusCallBack.onShareCanceled();
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
    }
}
