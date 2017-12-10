package com.lvsocialsdk.qq;


/**
 * The qq user entity.
 *
 * Created by lvzishen on 16/12/2.
 *
 */

import android.os.Parcel;
import android.os.Parcelable;

public class QQUserInfo implements Parcelable {
    public String is_yellow_year_vip;
    public int ret;
    public String figureurl_qq_1;
    public String figureurl_qq_2;
    public String nickname;
    public String yellow_vip_level;
    public int is_lost;
    public String msg;
    public String city;
    public String figureurl_1;
    public String vip;
    public String level;
    public String figureurl_2;
    public String province;
    public String gender;
    public String is_yellow_vip;
    public String figureurl;


    protected QQUserInfo(Parcel in) {
        is_yellow_year_vip = in.readString();
        ret = in.readInt();
        figureurl_qq_1 = in.readString();
        figureurl_qq_2 = in.readString();
        nickname = in.readString();
        yellow_vip_level = in.readString();
        is_lost = in.readInt();
        msg = in.readString();
        city = in.readString();
        figureurl_1 = in.readString();
        vip = in.readString();
        level = in.readString();
        figureurl_2 = in.readString();
        province = in.readString();
        gender = in.readString();
        is_yellow_vip = in.readString();
        figureurl = in.readString();
    }

    public static final Creator<QQUserInfo> CREATOR = new Creator<QQUserInfo>() {
        @Override
        public QQUserInfo createFromParcel(Parcel in) {
            return new QQUserInfo(in);
        }

        @Override
        public QQUserInfo[] newArray(int size) {
            return new QQUserInfo[size];
        }
    };

    @Override
    public String toString() {
        return "QQUserInfo{" +
                "is_yellow_year_vip='" + is_yellow_year_vip + '\'' +
                ", ret=" + ret +
                ", figureurl_qq_1='" + figureurl_qq_1 + '\'' +
                ", figureurl_qq_2='" + figureurl_qq_2 + '\'' +
                ", nickname='" + nickname + '\'' +
                ", yellow_vip_level='" + yellow_vip_level + '\'' +
                ", is_lost=" + is_lost +
                ", msg='" + msg + '\'' +
                ", city='" + city + '\'' +
                ", figureurl_1='" + figureurl_1 + '\'' +
                ", vip='" + vip + '\'' +
                ", level='" + level + '\'' +
                ", figureurl_2='" + figureurl_2 + '\'' +
                ", province='" + province + '\'' +
                ", gender='" + gender + '\'' +
                ", is_yellow_vip='" + is_yellow_vip + '\'' +
                ", figureurl='" + figureurl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(is_yellow_year_vip);
        dest.writeInt(ret);
        dest.writeString(figureurl_qq_1);
        dest.writeString(figureurl_qq_2);
        dest.writeString(nickname);
        dest.writeString(yellow_vip_level);
        dest.writeInt(is_lost);
        dest.writeString(msg);
        dest.writeString(city);
        dest.writeString(figureurl_1);
        dest.writeString(vip);
        dest.writeString(level);
        dest.writeString(figureurl_2);
        dest.writeString(province);
        dest.writeString(gender);
        dest.writeString(is_yellow_vip);
        dest.writeString(figureurl);
    }
}
