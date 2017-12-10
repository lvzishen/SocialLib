package com.lvsocialsdk.listener;

import android.content.Intent;

/**
 * Created by lvzishen on 16/12/2.
 *
 * 第三方登录分享 接口
 *
 */
public interface ActivityLifeListener {

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
