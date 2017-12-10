package com.lvsocialsdk.qq;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * QQ登录自定义类
 * <p>
 * Created by lvzishen on 16/12/2.
 */
public class QQLoginResult implements Parcelable {
    public int ret;
    public String openid;
    public String access_token;
    public String pay_token;
    public long expires_in;
    public String pf;
    public String pfkey;
    public String msg;
    public long login_cost;
    public long query_authority_cost;
    public long authority_cost;

    public QQLoginResult() {
    }

    protected QQLoginResult(Parcel in) {
        ret = in.readInt();
        openid = in.readString();
        access_token = in.readString();
        pay_token = in.readString();
        expires_in = in.readLong();
        pf = in.readString();
        pfkey = in.readString();
        msg = in.readString();
        login_cost = in.readLong();
        query_authority_cost = in.readLong();
        authority_cost = in.readLong();
    }

    public static final Creator<QQLoginResult> CREATOR = new Creator<QQLoginResult>() {
        @Override
        public QQLoginResult createFromParcel(Parcel in) {
            return new QQLoginResult(in);
        }

        @Override
        public QQLoginResult[] newArray(int size) {
            return new QQLoginResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ret);
        dest.writeString(openid);
        dest.writeString(access_token);
        dest.writeString(pay_token);
        dest.writeLong(expires_in);
        dest.writeString(pf);
        dest.writeString(pfkey);
        dest.writeString(msg);
        dest.writeLong(login_cost);
        dest.writeLong(query_authority_cost);
        dest.writeLong(authority_cost);
    }

    @Override
    public String toString() {
        return "QQLoginResult{" +
                "ret=" + ret +
                ", openid='" + openid + '\'' +
                ", access_token='" + access_token + '\'' +
                ", pay_token='" + pay_token + '\'' +
                ", expires_in=" + expires_in +
                ", pf='" + pf + '\'' +
                ", pfkey='" + pfkey + '\'' +
                ", msg='" + msg + '\'' +
                ", login_cost=" + login_cost +
                ", query_authority_cost=" + query_authority_cost +
                ", authority_cost=" + authority_cost +
                '}';
    }
}