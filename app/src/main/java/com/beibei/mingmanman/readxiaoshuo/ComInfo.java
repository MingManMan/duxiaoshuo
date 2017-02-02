package com.beibei.mingmanman.readxiaoshuo;

import java.util.List;

import rx.Subscriber;

/**
 * Created by mymac on 2016/10/24.
 */


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
    void get_xiaoshuo_mulu_url(Subscriber<Searchinfo> s_obj_get_mulu_url, String xiaoshuo_ming);
}

