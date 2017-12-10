package com.lvsocialsdk.wechat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvzishen on 16/12/5.
 */
public class SendAuthWechat implements Parcelable {
    public int errCode;
    public String errStr;
    public String transaction;
    public String openId;
    public String code;

    public SendAuthWechat() {
    }

    protected SendAuthWechat(Parcel in) {
        errCode = in.readInt();
        errStr = in.readString();
        transaction = in.readString();
        openId = in.readString();
        code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(errCode);
        dest.writeString(errStr);
        dest.writeString(transaction);
        dest.writeString(openId);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SendAuthWechat> CREATOR = new Creator<SendAuthWechat>() {
        @Override
        public SendAuthWechat createFromParcel(Parcel in) {
            return new SendAuthWechat(in);
        }

        @Override
        public SendAuthWechat[] newArray(int size) {
            return new SendAuthWechat[size];
        }
    };
}
