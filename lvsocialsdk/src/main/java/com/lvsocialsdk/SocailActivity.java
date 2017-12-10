package com.lvsocialsdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lvsocialsdk.listener.ActivityLifeListener;

/**
 * 登录Activity 为了设置回调接口 获得回调信息
 */
public class SocailActivity extends AppCompatActivity {
    ActivityLifeListener activityLifeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setActivityLife(ActivityLifeListener activityLifeListener) {
        this.activityLifeListener = activityLifeListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityLifeListener != null) {
            activityLifeListener.onActivityResult(requestCode, resultCode, data);
        }
    }
}
