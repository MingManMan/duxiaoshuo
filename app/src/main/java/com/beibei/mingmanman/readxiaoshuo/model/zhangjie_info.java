package com.beibei.mingmanman.readxiaoshuo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mingmanman on 2016/11/7.
 */

public class zhangjie_info implements Parcelable {
    public String zhangjie_mingzhi;
    public String zhangjie_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.zhangjie_mingzhi);
        dest.writeString(this.zhangjie_url);
    }

    public zhangjie_info() {
    }

    protected zhangjie_info(Parcel in) {
        this.zhangjie_mingzhi = in.readString();
        this.zhangjie_url = in.readString();
    }

    public static final Parcelable.Creator<zhangjie_info> CREATOR = new Parcelable.Creator<zhangjie_info>() {
        @Override
        public zhangjie_info createFromParcel(Parcel source) {
            return new zhangjie_info(source);
        }

        @Override
        public zhangjie_info[] newArray(int size) {
            return new zhangjie_info[size];
        }
    };
}
