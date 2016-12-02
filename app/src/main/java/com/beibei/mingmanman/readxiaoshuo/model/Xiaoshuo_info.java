package com.beibei.mingmanman.readxiaoshuo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mingmanman on 2016/11/1.
 */
public class Xiaoshuo_info implements Parcelable {
    public Long _id;
    public String  xiaoshuo_ming;  //小说名
    public String  zhandian_ming; //站点名
    public String  xiaoshuo_base_url; //小说章节的基础地址
    public String  xiaoshuo_mulu_url;//小说目录地址
    public Integer xiaoshuo_zhangjie=0; //小说章节总数，用来跟踪是否有新章节，大于这个总数就是有更新
    public Integer xiaoshuo_yuedu_zhangjie=0;//小说已经阅读到的章节，一遍在进入目录时显示相关章节
    public int  list_order;  //小说排位
    public Xiaoshuo_info(){
        this.xiaoshuo_ming="noName";
        this.zhandian_ming="noZhandian";
        this.xiaoshuo_base_url="noBaseUrl";
        this.xiaoshuo_mulu_url="noMuluUrl";
        this.list_order=0;
    }
    public Xiaoshuo_info(String xiaoshuo_ming,String zhandian_ming,String xiaoshuo_base_url,String xiaoshuo_mulu_url,int list_order){
        this.xiaoshuo_ming=xiaoshuo_ming;
        this.zhandian_ming=zhandian_ming;
        this.xiaoshuo_base_url=xiaoshuo_base_url;
        this.xiaoshuo_mulu_url=xiaoshuo_mulu_url;
        this.list_order=list_order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.xiaoshuo_ming);
        dest.writeString(this.zhandian_ming);
        dest.writeString(this.xiaoshuo_base_url);
        dest.writeString(this.xiaoshuo_mulu_url);
        dest.writeInt(this.list_order);
    }

    protected Xiaoshuo_info(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.xiaoshuo_ming = in.readString();
        this.zhandian_ming = in.readString();
        this.xiaoshuo_base_url = in.readString();
        this.xiaoshuo_mulu_url = in.readString();
        this.list_order = in.readInt();
    }

    public static final Parcelable.Creator<Xiaoshuo_info> CREATOR = new Parcelable.Creator<Xiaoshuo_info>() {
        @Override
        public Xiaoshuo_info createFromParcel(Parcel source) {
            return new Xiaoshuo_info(source);
        }

        @Override
        public Xiaoshuo_info[] newArray(int size) {
            return new Xiaoshuo_info[size];
        }
    };
}