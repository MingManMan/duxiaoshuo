package com.beibei.mingmanman.readxiaoshuo;

import android.app.Application;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;

/**
 * Created by mingmanman on 2017/2/1.
 */

public class Myapp extends Application {
    public String Xuanzhe_zhandian;
    public Xiaoshuo_info xiaoshuo=new Xiaoshuo_info();

    public void onCreate() {
        super.onCreate();
        Xuanzhe_zhandian = "笔趣阁2";
        xiaoshuo.xiaoshuo_ming="";
        xiaoshuo.xiaoshuo_mulu_url="";
        xiaoshuo.xiaoshuo_base_url="";
        xiaoshuo.zhandian_ming="";
    }

   /* public String getXuanzhe_zhandian() {
        return Xuanzhe_zhandian;
    }

    public void setXuanzhe_zhandian(String Xuanzhe_zhandian) {
        this.Xuanzhe_zhandian = Xuanzhe_zhandian;
    }*/

    private static final String NAME = "慢慢追书";
}

