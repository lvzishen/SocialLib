package com.lvsocialsdk.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.lvsocialsdk.R;
import com.lvsocialsdk.ShareManager;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;


/**
 * 分享微博回调页面
 * <p>
 * Created by lvzishen on 16/12/10.
 */
public class WeiBoShareActivity extends Activity implements IWeiboHandler.Response {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        WeiboShare.getInstance(this).handleIntent(getIntent(), this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        String result;
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                result ="分享成功";
                break;

            case WBConstants.ErrorCode.ERR_CANCEL:
                result = "取消分享";
                break;

            case WBConstants.ErrorCode.ERR_FAIL:
                result = "分享失败";
                break;

            default:
                result = "分享失败";
                break;
        }
        Intent intent = new Intent();
        intent.putExtra(ShareManager.WEIBO_SHARE_RESULT, result);
        intent.setAction(ShareManager.WEIBO_SHARE);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        Log.e("error", baseResponse.errMsg + "");
        finish();
    }

}
