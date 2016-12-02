package com.beibei.mingmanman.readxiaoshuo.model;

/**
 * Created by mymac on 2016/10/24.
 */

public class Read_info {
    public Long _id;
    public String xiaoshuo_ming ; //小说名
    public String zhandian_ming;//站点名
    public String zhangjie_a;//章节一
    public String zhangjie_b;//章节二
    public String zhangjie_c;//章节三
    public Integer list_order;
    public Read_info(){
        this.xiaoshuo_ming="noName";
        this.zhandian_ming="noZhandian";
        this.zhangjie_a="noZhangjie";
        this.zhangjie_b="noZhangjie";
        this.zhangjie_c="noZhangjie";
    }
}
