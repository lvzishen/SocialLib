package com.lvsocialsdk.wechat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lvsocialsdk.SocialConfig;
import com.lvsocialsdk.utils.ImageUtil;
import com.lvsocialsdk.utils.SocialUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * Created by lvzishen on 16/12/6.
 * <p/>
 * 微信分享类
 */
public class WeChatShare {

    private static volatile WeChatShare weChatShare;
    private IWXAPI mApi;
    public static final int TYPE_WECHAT_FRIEND = 0;
    public static final int TYPE_WECHAT_TIMELINE = 1;


    private static final int MAX_IMAGE_LENGTH = 32 * 1024;
    private static final int DEFAULT_MAX_SIZE = 150;

    public static WeChatShare getInstance(IWXAPI mApi) {
        if (weChatShare == null) {
            synchronized (WeChatShare.class) {
                if (weChatShare == null) {
                    weChatShare = new WeChatShare(mApi);
                }
            }
        }
        return weChatShare;
    }

    private WeChatShare(IWXAPI mApi) {
        this.mApi = mApi;
    }

    /**
     * 分享一段纯文字
     *
     * @param text 分享的文字
     * @param type 分享类型 WXSceneTimeline朋友圈  WXSceneSession 好友
     */
    public void sendText(String text, int type) {
        //初始化WXTextObject对象 填写需要分享的内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        // 用WXTextObject初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        // 构造一个Rep
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        if (type == TYPE_WECHAT_FRIEND) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        }

        mApi.sendReq(req);
    }


    /**
     * 分享图片
     *
     * @param bitmap 分享的图片
     * @param type   分享类型 WXSceneTimeline朋友圈  WXSceneSession 好友
     */
    public void sendImageUrl(Bitmap bitmap, int type) {
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        if (bitmap != null) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(bitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        if (type == TYPE_WECHAT_FRIEND) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        }
        mApi.sendReq(req);
    }


    /**
     * @param path 本地文件路径
     * @param type 分享类型 WXSceneTimeline朋友圈  WXSceneSession 好友
     */
    public void sendImageLocal(String path, int type) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        if (bmp != null) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(bmp, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        if (type == TYPE_WECHAT_FRIEND) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        }
        mApi.sendReq(req);

    }


    /**
     * 分享音乐链接
     *
     * @param audioPath   分享的音乐链接
     * @param title       分享的标题
     * @param description 分享的描述
     * @param bitmap      分享的图片
     * @param type        分享类型 WXSceneTimeline朋友圈  WXSceneSession 好友
     */
    public void sendAudio(String audioPath, String title, String description, Bitmap bitmap, int type) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = audioPath;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;

        if (bitmap != null) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(bitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        if (type == TYPE_WECHAT_FRIEND) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        }
        mApi.sendReq(req);

    }


    /**
     * 分享视频链接
     *
     * @param videoUrl    分享的视频链接
     * @param title       分享的标题
     * @param description 分享的描述
     * @param bitmap      分享的图片
     * @param type        分享类型 WXSceneTimeline朋友圈  WXSceneSession 好友
     */
    public void sendVedio(String videoUrl, String title, String description, Bitmap bitmap, int type) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = description;
        if (bitmap != null) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(bitmap, true);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        if (type == TYPE_WECHAT_FRIEND) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        }
        mApi.sendReq(req);

    }


    /**
     * 分享网页类型
     *
     * @param url         分享的链接
     * @param title       分享的标题
     * @param description 分享的描述
     * @param bitmap      分享的图片
     * @param type        分享类型 WXSceneTimeline朋友圈  WXSceneSession 好友
     */
    public void sendWebpage(String url, String title, String description, Bitmap bitmap, int type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        if (bitmap != null) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(bitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        if (type == TYPE_WECHAT_FRIEND) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        }
        mApi.sendReq(req);

    }

    /**
     * 分享网页类型
     *
     * @param url         分享的链接
     * @param title       分享的标题
     * @param description 分享的描述
     * @param thumbnail   分享的图片
     * @param type        分享类型 WXSceneTimeline朋友圈  WXSceneSession 好友
     */
    public void sendWebpage(final String url, final String title, final String description, final String thumbnail, final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = description;
                byte[] thumb = SocialUtils.getHtmlByteArray(thumbnail);
                if (null != thumb) {
                    msg.thumbData = SocialUtils.compressBitmap(thumb, 32);
                }
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.message = msg;
                if (type == TYPE_WECHAT_FRIEND) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
                }
                mApi.sendReq(req);
            }
        }).start();

    }

    /**
     * 根据微信的要求缩放缩略图
     *
     * @param bitmap 图片
     * @return 图片
     */
    public Bitmap zoomOut(Bitmap bitmap) {
        Bitmap dstBitmap = null;
        if (null != bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width <= 0 || height <= 0) return null;

            int w, h;
            float scale = height * 1.0f / width;
            if (width > height) {
                w = DEFAULT_MAX_SIZE;
                h = (int) (w * scale);
            } else {
                h = DEFAULT_MAX_SIZE;
                w = (int) (h / scale);
            }

            dstBitmap = ImageUtil.zoom(bitmap, w, h);
            byte[] data = ImageUtil.bitmapToBytes(dstBitmap, false);

            while (data.length > MAX_IMAGE_LENGTH) {
                dstBitmap.recycle();

                w -= 10;
                h = (int) (w * scale);

                dstBitmap = ImageUtil.zoom(bitmap, w, h);
                data = ImageUtil.bitmapToBytes(dstBitmap, false);
            }
        }

        return dstBitmap;
    }
}
