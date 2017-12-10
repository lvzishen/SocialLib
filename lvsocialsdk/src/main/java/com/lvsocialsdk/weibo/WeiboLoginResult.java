package com.lvsocialsdk.weibo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvzishen on 16/12/2.
 */
public class WeiboLoginResult implements Parcelable {
    public String uid;
    public String access_token;
    public String userName;
    public String expires_in;
    public String remind_in;
    public String refresh_token;

    public WeiboLoginResult() {
    }

    protected WeiboLoginResult(Parcel in) {
        uid = in.readString();
        access_token = in.readString();
        userName = in.readString();
        expires_in = in.readString();
        remind_in = in.readString();
        refresh_token = in.readString();
    }

    @Override
    public String toString() {
        return "WeiboLoginResult{" +
                "uid='" + uid + '\'' +
                ", access_token='" + access_token + '\'' +
                ", userName='" + userName + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", remind_in='" + remind_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(access_token);
        dest.writeString(userName);
        dest.writeString(expires_in);
        dest.writeString(remind_in);
        dest.writeString(refresh_token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeiboLoginResult> CREATOR = new Creator<WeiboLoginResult>() {
        @Override
        public WeiboLoginResult createFromParcel(Parcel in) {
            return new WeiboLoginResult(in);
        }

        @Override
        public WeiboLoginResult[] newArray(int size) {
            return new WeiboLoginResult[size];
        }
    };
}
