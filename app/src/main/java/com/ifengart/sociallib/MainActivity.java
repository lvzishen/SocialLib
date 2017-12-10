package com.ifengart.sociallib;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvsocialsdk.LoginManager;
import com.lvsocialsdk.ShareManager;
import com.lvsocialsdk.SocailActivity;
import com.lvsocialsdk.SocialConfig;
import com.lvsocialsdk.listener.LoginStatusCallBack;
import com.lvsocialsdk.listener.ShareStatusCallBack;
import com.lvsocialsdk.qq.QQLoginResult;
import com.lvsocialsdk.qq.QQShare;
import com.lvsocialsdk.wechat.WeChatShare;
import com.lvsocialsdk.wechat.WechatLoginResult;
import com.lvsocialsdk.weibo.WeiboLoginResult;
import com.lvsocialsdk.weibo.WeiboShare;

import java.util.ArrayList;

public class MainActivity extends SocailActivity implements View.OnClickListener {
    FrameLayout fl_weibo;
    FrameLayout fl_wechat;
    FrameLayout fl_qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocialConfig.getInstance().isDebug(false)
                .initQQ("1104817836")
                .initWeiBo("1354293035", "https://api.weibo.com/oauth2/default.html", "")
                .initWechat("wx80dc403e157187d9", "c7dbac08bbe8c09b4d10c2e255a66508", "snsapi_userinfo", "ifengartwxaccb11f40868eb86");

        fl_wechat = (FrameLayout) findViewById(R.id.activity_login_wechat_btn);
        fl_weibo = (FrameLayout) findViewById(R.id.activity_login_weibo_btn);
        fl_qq = (FrameLayout) findViewById(R.id.activity_login_qq_btn);
        fl_wechat.setOnClickListener(this);
        fl_weibo.setOnClickListener(this);
        fl_qq.setOnClickListener(this);
//    private void showShareDialog() {
//        shareFeed = (Feed) saltDiscoverAdapter.getList().get(socialPosition);
//        if (shareDialog == null) {
//            shareDialog = new ShareDialog(mActivity, R.style.Normal_Dialog);
//            shareDialog.show();
//            shareDialog.setCanceledOnTouchOutside(true);
//            shareDialog.setCancelable(true);
//            shareDialog.setShareListener(new ShareDialog.ShareListener() {
//                                             @Override
//                                             public void shareWithWechat() {
//                                                 WeChatShare weChatShare = ShareManager.getInstance().shareWithWechat((SocailActivity) mActivity, new ShareStatusCallBack() {
//                                                             @Override
//                                                             public void onShareSuccess() {
//                                                                 shortToast(getString(R.string.share_success));
//                                                                 shareDialog.dismiss();
//                                                             }
//
//                                                             @Override
//                                                             public void onShareFailed() {
//                                                                 shortToast(getString(R.string.share_fail));
//                                                                 shareDialog.dismiss();
//                                                             }
//
//                                                             @Override
//                                                             public void onShareCanceled() {
//                                                                 shortToast(getString(R.string.share_cancel));
//                                                                 shareDialog.dismiss();
//                                                             }
//                                                         }
//
//                                                 );
//                                                 if (weChatShare != null) {
//                                                     if (shareFeed.type.equals(AppContanst.TYPE_IMAGE)) {
//                                                         if (shareFeed.feed_image.isEmpty()) {//有字无图
//                                                             Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                                                             weChatShare.sendWebpage(shareFeed.feed_link, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, bitmap, WeChatShare.TYPE_WECHAT_FRIEND);
//                                                         } else {
//                                                             weChatShare.sendWebpage(shareFeed.feed_link, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_image.get(0), WeChatShare.TYPE_WECHAT_FRIEND);
//                                                         }
//                                                     } else {
//                                                         weChatShare.sendWebpage(shareFeed.feed_link, shareFeed.feed_title, shareFeed.feed_content, shareFeed.feed_image.get(0), WeChatShare.TYPE_WECHAT_FRIEND);
//                                                     }
//                                                 }
//                                             }
//
//                                             @Override
//                                             public void shareWithWeChatLine() {
//                                                 WeChatShare weChatShare = ShareManager.getInstance().shareWithWechat((SocailActivity) mActivity, new ShareStatusCallBack() {
//                                                             @Override
//                                                             public void onShareSuccess() {
//                                                                 shortToast(getString(R.string.share_success));
//                                                                 shareDialog.dismiss();
//                                                             }
//
//                                                             @Override
//                                                             public void onShareFailed() {
//                                                                 shortToast(getString(R.string.share_fail));
//                                                                 shareDialog.dismiss();
//                                                             }
//
//                                                             @Override
//                                                             public void onShareCanceled() {
//                                                                 shortToast(getString(R.string.share_cancel));
//                                                                 shareDialog.dismiss();
//                                                             }
//                                                         }
//
//                                                 );
//                                                 if (weChatShare != null) {
//                                                     if (shareFeed.type.equals(AppContanst.TYPE_IMAGE)) {
//                                                         if (shareFeed.feed_image.isEmpty()) {//有字无图
//                                                             Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                                                             weChatShare.sendWebpage(shareFeed.feed_link, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, bitmap, WeChatShare.TYPE_WECHAT_TIMELINE);
//                                                         } else {
//                                                             weChatShare.sendWebpage(shareFeed.feed_link, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_image.get(0), WeChatShare.TYPE_WECHAT_TIMELINE);
//                                                         }
//                                                     } else {
//                                                         weChatShare.sendWebpage(shareFeed.feed_link, shareFeed.feed_title, shareFeed.feed_content, shareFeed.feed_image.get(0), WeChatShare.TYPE_WECHAT_TIMELINE);
//                                                     }
//                                                 }
//                                             }
//
//                                             @Override
//                                             public void shareWithQQFriend() {
//                                                 QQShare qqShare = ShareManager.getInstance().shareWithQQ((SocailActivity) mActivity, new ShareStatusCallBack() {
//                                                     @Override
//                                                     public void onShareSuccess() {
//                                                         shortToast(getString(R.string.share_success));
//                                                         shareDialog.dismiss();
//                                                     }
//
//                                                     @Override
//                                                     public void onShareFailed() {
//                                                         shortToast(getString(R.string.share_fail));
//                                                         shareDialog.dismiss();
//                                                     }
//
//                                                     @Override
//                                                     public void onShareCanceled() {
//                                                         shortToast(getString(R.string.share_cancel));
//                                                         shareDialog.dismiss();
//                                                     }
//                                                 });
//
//                                                 if (qqShare != null) {
//                                                     if (shareFeed.type.equals(AppContanst.TYPE_IMAGE)) {
//                                                         if (shareFeed.feed_image.isEmpty()) {//有字无图
//                                                             qqShare.shareToQQWithNetworkImage(mActivity, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_link, shareFeed.avatar);
//                                                         } else {
//                                                             qqShare.shareToQQWithNetworkImage(mActivity, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_link, shareFeed.feed_image.get(0));
//                                                         }
//                                                     } else {
//                                                         qqShare.shareToQQWithNetworkImage(mActivity, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_link, shareFeed.feed_image.get(0));
//                                                     }
//                                                 }
//                                             }
//
//                                             @Override
//                                             public void shareWithQQZone() {
//                                                 ArrayList<String> arrayList = new ArrayList<>();
//                                                 QQShare qqShare = ShareManager.getInstance().shareWithQQ((SocailActivity) mActivity, new ShareStatusCallBack() {
//                                                     @Override
//                                                     public void onShareSuccess() {
//                                                         shortToast(getString(R.string.share_success));
//                                                         shareDialog.dismiss();
//                                                     }
//
//                                                     @Override
//                                                     public void onShareFailed() {
//                                                         shortToast(getString(R.string.share_fail));
//                                                         shareDialog.dismiss();
//                                                     }
//
//                                                     @Override
//                                                     public void onShareCanceled() {
//                                                         shortToast(getString(R.string.share_cancel));
//                                                         shareDialog.dismiss();
//                                                     }
//                                                 });
//                                                 if (qqShare != null) {
//                                                     if (shareFeed.type.equals(AppContanst.TYPE_IMAGE)) {
//                                                         if (shareFeed.feed_image.isEmpty()) {//有字无图
//                                                             arrayList.add(shareFeed.avatar);
//                                                             qqShare.shareToQzoneWithNetWorkImages(mActivity, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_link, arrayList);
//                                                         } else {
//                                                             arrayList.add(shareFeed.feed_image.get(0));
//                                                             qqShare.shareToQzoneWithNetWorkImages(mActivity, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_link, arrayList);
//                                                         }
//                                                     } else {
//                                                         arrayList.add(shareFeed.feed_image.get(0));
//                                                         qqShare.shareToQzoneWithNetWorkImages(mActivity, "我分享了" + shareFeed.uname + "的有言,快来围观", shareFeed.feed_content, shareFeed.feed_link, arrayList);
//                                                     }
//                                                 }
//                                             }
//
//                                             @Override
//                                             public void shareWithWeibo() {
//                                                 WeiboShare weiboShare = ShareManager.getInstance().shareWithWeibo((SocailActivity) mActivity, new ShareStatusCallBack() {
//                                                     @Override
//                                                     public void onShareSuccess() {
//                                                         shortToast(getString(R.string.share_success));
//                                                         shareDialog.dismiss();
//                                                     }
//
//                                                     @Override
//                                                     public void onShareFailed() {
//                                                         shortToast(getString(R.string.share_fail));
//                                                         shareDialog.dismiss();
//                                                     }
//
//                                                     @Override
//                                                     public void onShareCanceled() {
//                                                         shortToast(getString(R.string.share_cancel));
//                                                         shareDialog.dismiss();
//                                                     }
//                                                 });
//                                                 if (weiboShare != null) {
//                                                     if (shareFeed.type.equals(AppContanst.TYPE_IMAGE)) {
//                                                         if (shareFeed.feed_image.isEmpty()) {//有字无图
//                                                             Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                                                             weiboShare.sendWebPage(shareFeed.feed_title, shareFeed.feed_content, bitmap, shareFeed.feed_link);
//                                                         } else {
//                                                             weiboShare.sendWebPage(shareFeed.feed_title, shareFeed.feed_content, shareFeed.feed_image.get(0), shareFeed.feed_link);
//                                                         }
//                                                     } else {
//                                                         weiboShare.sendWebPage(shareFeed.feed_title, shareFeed.feed_content, shareFeed.feed_image.get(0), shareFeed.feed_link);
//                                                     }
//                                                 }
//                                             }
//
//                                             @Override
//                                             public void shareWithCopy() {
//                                                 ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
//                                                 // 将文本内容放到系统剪贴板里。
//                                                 cm.setText(shareFeed.feed_link);
//                                                 shortToast(R.string.copy_success);
//                                                 shareDialog.dismiss();
//                                             }
//                                         }
//            );
//        } else {
//            shareDialog.show();
//        }

//    }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_wechat_btn:
                LoginManager.getInstance().login(MainActivity.this, LoginManager.TYPE_WECHAT, false, new LoginStatusCallBack() {
                    @Override
                    public void onLoginSuccess(Object data) {

                    }

                    @Override
                    public void onLoginFailed() {

                    }

                    @Override
                    public void onLoginIng() {

                    }

                    @Override
                    public void onAuthSuccess(Object data) {
                        if (data instanceof WechatLoginResult) {
                            Log.i("authLOGIN", data.toString());
                            WechatLoginResult wechatLoginResult = (WechatLoginResult) data;
                        }
                        //请求网络
                    }

                    @Override
                    public void onAuthFailed() {
                    }

                    @Override
                    public void onAuthCancel() {
                    }

                    @Override
                    public void onAuthException(Exception e) {
                    }

                    @Override
                    public void onNotInstall(String message) {
                    }
                });
                break;
            case R.id.activity_login_weibo_btn:
                LoginManager.getInstance().login(MainActivity.this, LoginManager.TYPE_WEIBO, false, new LoginStatusCallBack() {
                    @Override
                    public void onLoginSuccess(Object data) {

                    }

                    @Override
                    public void onLoginFailed() {

                    }

                    @Override
                    public void onLoginIng() {

                    }

                    @Override
                    public void onAuthSuccess(Object data) {
                        if (data instanceof WeiboLoginResult) {
                            Log.i("authLOGIN", data.toString());
                            WeiboLoginResult weiboLoginResult = (WeiboLoginResult) data;

                        }
                        //请求网络
                    }

                    @Override
                    public void onAuthFailed() {
                    }

                    @Override
                    public void onAuthCancel() {
                    }

                    @Override
                    public void onAuthException(Exception e) {
                    }

                    @Override
                    public void onNotInstall(String message) {
                    }
                });
                break;
            case R.id.activity_login_qq_btn:
                LoginManager.getInstance().login(MainActivity.this, LoginManager.TYPE_QQ, false, new LoginStatusCallBack() {
                    @Override
                    public void onLoginSuccess(Object data) {

                    }

                    @Override
                    public void onLoginFailed() {

                    }

                    @Override
                    public void onLoginIng() {

                    }

                    @Override
                    public void onAuthSuccess(Object data) {
                        if (data instanceof QQLoginResult) {
                            Log.i("authLOGIN", data.toString());
                            QQLoginResult qqLoginResult = (QQLoginResult) data;
                        }
                        //请求网络
                    }

                    @Override
                    public void onAuthFailed() {
                    }

                    @Override
                    public void onAuthCancel() {
                    }

                    @Override
                    public void onAuthException(Exception e) {
                    }

                    @Override
                    public void onNotInstall(String message) {
                    }
                });
                break;
        }
    }
}
