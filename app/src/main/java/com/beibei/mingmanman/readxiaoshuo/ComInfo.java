package com.beibei.mingmanman.readxiaoshuo;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mymac on 2016/10/24.
 */

class Xiaoshuo_in_One{
    public Xiaoshuo_info xiaoshuo=new Xiaoshuo_info();
    List<Mulu_info> mulu_list = new ArrayList<Mulu_info>();
}

class Mulu_info {
    public String mingzhi;
    public String dizhi;

    public Mulu_info() {
        mingzhi = "noName";
        dizhi = "noDizhi";
    }

    public Mulu_info(String mingzhi, String dizhi) {
        this.mingzhi = mingzhi;
        this.dizhi = dizhi;
    }
}

class Geng_xin_info{
    public String xiaoshuo_ming="";
    public Integer zhangjie_shu=0;
}

interface ZhandianInfterface {
    void getmulu(Subscriber<List<Mulu_info>> subscriber, String url);
    void getxiaoshuoneirong(Subscriber<String> s_obj, String url);
    void getsearch(Subscriber<List<Searchinfo>> s_obj, String guanjianzhi);
    void get_xiaoshuo_muluurl_zhandianinfo_byname(Subscriber<Xiaoshuo_in_One> s_obj_get_mulu_url, String xiaoshuo_ming);
}

