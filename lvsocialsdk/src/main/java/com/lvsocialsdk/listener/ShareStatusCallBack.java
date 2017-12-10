package com.lvsocialsdk.listener;

/**
 * Created by lvzishen on 16/12/8.
 */
public interface ShareStatusCallBack {

    /**
     * 分享成功回调
     */
    void onShareSuccess();

    /**
     * 分享失败回调
     */
    void onShareFailed();

    /**
     * 分享取消回调
     */
    void onShareCanceled();
}
