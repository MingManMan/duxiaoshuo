package com.beibei.mingmanman.readxiaoshuo;

/**
 * Created by mymac on 1/8 0008.
 */

public class Zhandian_Maker {
    //根据需要返回一个或一堆站点类
    //返回一个速度最快的站点
    public ZhandianInfterface maker_zhandian(String zhandianming) {
        ZhandianInfterface s=null;
        switch (zhandianming) {
            case "笔趣阁1":
                s = new ZhandianA();
                break;
            case "爱上书屋":
                s = new ZhandianB();
                break;
            case "笔趣阁2":
                s = new ZhandianC();

                break;
            case "新八一中文网":
                break;
            case "八一中文网":
                break;
            case "三七中文":
                break;
            case "品书网":
                break;
            case "一本读":
                break;

            default:
                s = new ZhandianA();
        }
        return s;
    }

    //返回一堆站点
}
