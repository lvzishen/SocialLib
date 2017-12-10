package com.lvsocialsdk.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.lvsocialsdk.LoginManager;
import com.lvsocialsdk.ShareManager;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;


/**
 * 微信分享回调页面 抽象类
 * <p/>
 * Created by lvzishen on 16/12/5.
 */
public abstract class WXAbActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        WeChatCore.getInstance(this).handleResponse(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        int type = resp.getType();
        switch (type) {
            case 1: // SendAuth.Resp 1为登录
                SendAuthWechat sendAuthWechat = new SendAuthWechat();
                sendAuthWechat.errCode = resp.errCode;
                sendAuthWechat.errStr = resp.errStr;
                sendAuthWechat.openId = resp.openId;
                sendAuthWechat.code = ((SendAuth.Resp) resp).code;
                sendAuthWechat.transaction = resp.transaction;
                Intent intent = new Intent();
                intent.putExtra(LoginManager.WECHAT_LOGIN_RESP, sendAuthWechat);
                intent.setAction(LoginManager.WECHAT_LOGIN_RESULT);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                break;

            case 2: // SendMessageToWX.Resp 2为分享
                handlerSendMessage(resp);
                break;

            case 3: // GetMessageFromWX.Resp
                break;

            case 4: // ShowMessageFromWX.Resp
                break;
        }
        finish();
    }


    private void handlerSendMessage(BaseResp resp) {
        String result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享失败";
                break;

            default:
                result = "分享失败";
                break;
        }
        Intent intent = new Intent();
        intent.putExtra(ShareManager.WECHAT_SHARE_RESULT, result);
        intent.setAction(ShareManager.WECHAT_SHARE);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
