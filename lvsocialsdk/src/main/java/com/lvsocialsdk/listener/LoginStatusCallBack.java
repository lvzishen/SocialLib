package com.lvsocialsdk.listener;

/**
 * Created by lvzishen on 16/12/2.
 */
public interface LoginStatusCallBack {

    /**
     * 登录成功回调 只有mRequestInfoEnable设置为true,使用第三方数据才会回调
     *
     * @param data 登录成功回调 只有mRequestInfoEnable设置为true,使用第三方数据才会回调
     */
    void onLoginSuccess(Object data);

    /**
     * 登录失败回调 只有mRequestInfoEnable设置为true,使用第三方数据才会回调
     */
    void onLoginFailed();

    /**
     * 正在登录回调 只有mRequestInfoEnable设置为true,使用第三方数据才会回调
     */
    void onLoginIng();

    /**
     * 授权认证成功回调 只有mRequestInfoEnable设置为false,使用自己app登录接口才会回调
     *
     * @param data
     */
    void onAuthSuccess(Object data);

    /**
     * 授权认证失败回调
     */
    void onAuthFailed();

    /**
     * 授权取消回调
     */
    void onAuthCancel();

    /**
     * @param e 授权出现异常回调
     */
    void onAuthException(Exception e);

    /**
     * 未安装
     */
    void onNotInstall(String message);
}
