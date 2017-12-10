package com.lvsocialsdk.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lvsocialsdk.R;
import com.lvsocialsdk.SocialConfig;
import com.lvsocialsdk.utils.SocialUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;


/**
 * Created by lvzishen on 16/12/5.
 */
public class WeiboShare {
    private static volatile WeiboShare weiboShare;
    private Context context;
    private final IWeiboShareAPI mWeiboShareAPI;
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private String weiboToken;
    public static final long HALF_HOUR = 30 * 60 * 1000L;

    private boolean isSessionValid() {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(context);
        if (null != mAccessToken && mAccessToken.isSessionValid() && !isExpired(mAccessToken.getExpiresTime())) {
            return true;
        }
        return false;
    }

    /**
     * 是否过期；剩余时间不足半小时视为过期
     *
     * @param expiredAt date
     * @return boolean
     */
    private boolean isExpired(long expiredAt) {
        return System.currentTimeMillis() > (expiredAt - HALF_HOUR);
    }

    public static WeiboShare getInstance(Context context) {
        if (weiboShare == null) {
            synchronized (WeiboShare.class) {
                if (weiboShare == null) {
                    weiboShare = new WeiboShare(context);
                }
            }
        }
        return weiboShare;
    }

    private WeiboShare(Context context) {
        this.context = context;
        mAuthInfo = new AuthInfo(context, SocialConfig.weibo_key, SocialConfig.weibo_redirect_url, SocialConfig.weibo_scope);
        mSsoHandler = new SsoHandler((Activity) context, mAuthInfo);
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context.getApplicationContext(),
                SocialConfig.weibo_key);
        mWeiboShareAPI.registerApp();
    }


    public void handleIntent(Intent intent, IWeiboHandler.Response response) {
        if (null != mWeiboShareAPI) {
            mWeiboShareAPI.handleWeiboResponse(intent, response);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mSsoHandler) {
            // SSO 授权回调
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    /**
     * 微博分享纯文字
     *
     * @param content 分享内容
     */
    public void sendText(String content) {

        if (isSessionValid()) {
            // 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            TextObject textObject = getTextObj(content);
            weiboMessage.textObject = textObject;

            sendSendMessage(weiboMessage);
        } else {
            mSsoHandler.authorize(mAuthListener);
        }

    }


    /**
     * 微博分享纯图片
     *
     * @param image 分享图片
     */
    public void sendImage(Bitmap image) {
        if (isSessionValid()) {
            // 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            ImageObject imageObject = getImageObj(image);
            weiboMessage.imageObject = imageObject;

            sendSendMessage(weiboMessage);
        } else {
            mSsoHandler.authorize(mAuthListener);
        }

    }

    /**
     * 分享网页
     *
     * @param title       标题
     * @param description 描述内容
     * @param image       缩略图链接
     * @param webUrl      网页链接
     */
    public void sendWebPage(String title, String description, Bitmap image, String webUrl) {
        if (isSessionValid()) {
            // 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            WebpageObject webpageObject = new WebpageObject();
            webpageObject.identify = Utility.generateGUID();
            webpageObject.title = title;
            webpageObject.description = description;
            webpageObject.setThumbImage(image);
            webpageObject.actionUrl = webUrl;

            weiboMessage.mediaObject = webpageObject;
            sendSendMessage(weiboMessage);
        } else {
            mSsoHandler.authorize(mAuthListener);
        }

    }


    /**
     * 分享网页
     *
     * @param title       标题
     * @param description 描述内容
     * @param imageUrl    缩略图链接
     * @param webUrl      网页链接
     */
    public void sendWebPage(final String title, final String description, final String imageUrl, final String webUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isSessionValid()) {
                    // 初始化微博的分享消息
                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    WebpageObject webpageObject = new WebpageObject();
                    webpageObject.identify = Utility.generateGUID();
                    webpageObject.title = title;
                    webpageObject.description = description;
                    byte[] thumb = SocialUtils.getHtmlByteArray(imageUrl);
                    if (null != thumb) {
                        webpageObject.thumbData = SocialUtils.compressBitmap(thumb, 32);
                    }
                    webpageObject.actionUrl = webUrl;

                    weiboMessage.mediaObject = webpageObject;
                    sendSendMessage(weiboMessage);
                } else {
                    mSsoHandler.authorize(mAuthListener);
                }
            }
        }).start();

    }


    /**
     * 分享音乐链接
     *
     * @param title       标题
     * @param description 描述内容
     * @param image       缩略图链接
     * @param musicUrl    音乐链接
     */
    public void sendAudio(String title, String description, Bitmap image, String musicUrl) {
        if (isSessionValid()) {
            // 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

            MusicObject musicObject = new MusicObject();
            musicObject.identify = Utility.generateGUID();
            musicObject.title = title;
            musicObject.description = description;
            // 设置 Bitmap 类型的图片到视频对象里
            // 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            musicObject.setThumbImage(image);
            musicObject.actionUrl = musicUrl;
            musicObject.dataUrl = SocialConfig.weibo_redirect_url;
            musicObject.dataHdUrl = SocialConfig.weibo_redirect_url;
            musicObject.duration = 10;
            musicObject.defaultText = description;
            weiboMessage.mediaObject = musicObject;
            sendSendMessage(weiboMessage);
        } else {
            mSsoHandler.authorize(mAuthListener);
        }

    }

    /**
     * 分享视频
     *
     * @param title       标题
     * @param description 描述内容
     * @param image       缩略图链接
     * @param videoUrl    网页链接
     */
    public void sendVideo(String title, String description, Bitmap image, String videoUrl) {
        if (isSessionValid()) {
            // 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            VideoObject videoObject = new VideoObject();
            videoObject.identify = Utility.generateGUID();
            videoObject.title = title;
            videoObject.description = description;

            videoObject.setThumbImage(image);
            videoObject.actionUrl = videoUrl;
            videoObject.dataUrl = videoUrl;
            videoObject.dataHdUrl = videoUrl;
            videoObject.duration = 10;
            videoObject.defaultText = description;

            weiboMessage.mediaObject = videoObject;
            sendSendMessage(weiboMessage);
        } else {
            mSsoHandler.authorize(mAuthListener);
        }

    }

//    private void weiboShare(final String content, Bitmap imgUrl) {
//        sendMultiMessage(content, imgUrl);
//        if (!TextUtils.isEmpty(imgUrl)) {
//            // 网络图片
//            if (imgUrl.startsWith("http")) {

//                new BitmapAsyncTask(imgUrl, new BitmapAsyncTask.OnBitmapListener() {
//                    @Override
//                    public void onSuccess(Bitmap bitmap) {
//                        sendMultiMessage(content, bitmap);
//                    }
//
//                    @Override
//                    public void onException(Exception exception) {
//                        sendMultiMessage(content);
//                    }
//                }).execute();

//            } else {
    //本地图片
//                sendMultiMessage(content, getLoacalBitmap(imgUrl));
//            }
//        } else {
//            sendMultiMessage(content);
//        }
//    }


//    public Bitmap getLoacalBitmap(String localPath) {
//        File file = new File(localPath);
//        if (file.exists()) {
//            try {
//                return BitmapFactory.decodeFile(localPath);
//            } catch (OutOfMemoryError error) {
//                error.printStackTrace();
//            }
//        }
//        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//    }


    private TextObject getTextObj(String content) {
        TextObject textObject = new TextObject();
        if (!TextUtils.isEmpty(content)) {
            textObject.text = content;
        }
        return textObject;
    }

    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        if (null != bitmap) {
            imageObject.setImageObject(bitmap);
        }
        return imageObject;
    }


    private void sendSendMessage(WeiboMultiMessage weiboMessage) {
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        String token = "";
        if (!TextUtils.isEmpty(weiboToken)) {
            token = weiboToken;
        } else {
            Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(context);
            if (mAccessToken != null) {
                token = mAccessToken.getToken();
            }
        }
        mWeiboShareAPI.sendRequest((Activity) context, request, mAuthInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                Log.e("WeiboException", arg0 + "");
            }

            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context, newToken);
            }

            @Override
            public void onCancel() {
                Log.e("取消分享", "取消分享");
            }
        });
    }

    private WeiboAuthListener mAuthListener = new WeiboAuthListener() {

        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken.isSessionValid()) {
                weiboToken = accessToken.getToken();
                AccessTokenKeeper.writeAccessToken(context, accessToken);
            } else {
                onCancel();
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("WeiboException", e + "");
        }

        @Override
        public void onCancel() {
            Log.e("WeiboException", "取消授权");
        }
    };

}
