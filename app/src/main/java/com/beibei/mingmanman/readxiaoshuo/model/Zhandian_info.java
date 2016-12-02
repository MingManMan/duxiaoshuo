package com.beibei.mingmanman.readxiaoshuo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mingmanman on 2016/10/25.
 */

public class Zhandian_info implements Parcelable {
    public Long _id;
    public String zhandian_ming; //站点名
    public String zhandian_url ;//站点地址
    public String search_url;    //搜索基础地址
    public int sudu_paixv;  //站点访问速度排序

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.zhandian_ming);
        dest.writeString(this.zhandian_url);
        dest.writeString(this.search_url);
        dest.writeInt(this.sudu_paixv);
    }

    public Zhandian_info() {
    }

    protected Zhandian_info(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.zhandian_ming = in.readString();
        this.zhandian_url = in.readString();
        this.search_url = in.readString();
        this.sudu_paixv = in.readInt();
    }

    public static final Parcelable.Creator<Zhandian_info> CREATOR = new Parcelable.Creator<Zhandian_info>() {
        @Override
        public Zhandian_info createFromParcel(Parcel source) {
            return new Zhandian_info(source);
        }

        @Override
        public Zhandian_info[] newArray(int size) {
            return new Zhandian_info[size];
        }
    };
}
