package com.lvsocialsdk.wechat;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * The wechat authorize result entity.
 * <p>
 * Created by lvzishen on 16/12/2.
 */
public class WechatLoginResult implements Parcelable {
    public String access_token;
    public long expiresIn;
    public String refreshToken;
    public String openid;
    public String scope;
    public String unionid;
    public int errcode;
    public String errmsg;


    protected WechatLoginResult(Parcel in) {
        access_token = in.readString();
        expiresIn = in.readLong();
        refreshToken = in.readString();
        openid = in.readString();
        scope = in.readString();
        unionid = in.readString();
        errcode = in.readInt();
        errmsg = in.readString();
    }

    public static final Creator<WechatLoginResult> CREATOR = new Creator<WechatLoginResult>() {
        @Override
        public WechatLoginResult createFromParcel(Parcel in) {
            return new WechatLoginResult(in);
        }

        @Override
        public WechatLoginResult[] newArray(int size) {
            return new WechatLoginResult[size];
        }
    };

    @Override
    public String toString() {
        return "WechatLoginResult{" +
                "accessToken='" + access_token + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeLong(expiresIn);
        dest.writeString(refreshToken);
        dest.writeString(openid);
        dest.writeString(scope);
        dest.writeString(unionid);
        dest.writeInt(errcode);
        dest.writeString(errmsg);
    }
}
