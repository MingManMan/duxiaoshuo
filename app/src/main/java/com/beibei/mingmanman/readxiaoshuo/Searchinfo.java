package com.beibei.mingmanman.readxiaoshuo;

/**
 * Created by mingmanman on 2016/10/13.
 */

public class Searchinfo {
    public String img_url, xiaoshuo_name = "";
    public String xiaoshuo_jianjie = "";
    public String xiaoshuo_info = "";
    public String zhandian_ming = "";
    public String xiaoshuo_base_url = "";
    public String xiaoshuo_mulu_url = "";
    public String yitianjia="n";

    public Searchinfo() {
        this.img_url = "none";
        this.xiaoshuo_name = "none";
        this.xiaoshuo_info = "none";
    }

    public Searchinfo(String img_url, String xiaoshuo_name, String xiaoshuo_info) {
        this.img_url = img_url;
        this.xiaoshuo_name = xiaoshuo_name;
        this.xiaoshuo_info = xiaoshuo_info;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public String getXiaoshuo_name() {
        return this.xiaoshuo_name;
    }

    public String getXiaoshuo_info() {
        return xiaoshuo_info;
    }
}
