package com.beibei.mingmanman.readxiaoshuo;

import com.beibei.mingmanman.readxiaoshuo.model.Zhandian_info;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mymac on 1/8 0008.
 */

public class ZhandianA extends Base_zhandian implements ZhandianInfterface {
    private Zhandian_info zhandian;
    private String zhandian_ming = "笔趣阁1";
    private String base_search_url = "http://zhannei.baidu.com/cse/search?s=7138806708853866527&q=";
    List<Mulu_info> mulu_list = new ArrayList<Mulu_info>();

    public ZhandianA() {
        this.mulu_list = new ArrayList<Mulu_info>();
    }
    public ZhandianA(Zhandian_info zhandian){
        this.mulu_list = new ArrayList<Mulu_info>();
        this.zhandian=zhandian;
    }

    //=============还未完成====================

    public Geng_xin_info getmulu_number_page(String url){
        Geng_xin_info gengxin_info=new Geng_xin_info();
        Document doc = getweb(url);
        if (doc != null) {
            Element l1 = doc.select("#list").first();
            Elements links = l1.select("a");
            gengxin_info.zhangjie_shu=links.size();
        }
        return gengxin_info;
    }
    public  void getmulu_number(Subscriber<Geng_xin_info> subscriber, String url){
        Observable.just(url)
                .map(s->getmulu_number_page(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
//=======================================================

    @Override
    public void getmulu(Subscriber<List<Mulu_info>> subscriber, String url) {
        Observable.just(url)
                .map(new Func1<String, List<Mulu_info>>() {
                    @Override
                    public List<Mulu_info> call(String s) {
                        return getmulupage(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public List<Mulu_info> getmulupage(String url) {
        List<Mulu_info> tmp_list = new ArrayList<Mulu_info>();
        Document doc = getweb(url);
        if (doc != null) {
            Element l1 = doc.select("#list").first();
            Elements links = l1.select("a");
            for (Element element : links) {
                tmp_list.add(new Mulu_info(element.text(), element.attr("href")));
            }
        }
        return tmp_list;
    }

    @Override
    public void getxiaoshuoneirong(Subscriber<String> subscriber, String url) {
        Observable.just(url)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return getneirongpage(s);
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.replace("readx();", "");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public String getneirongpage(String url) {
        String neirong = "";
        Document doc = getweb(url);
        if (doc != null) {
            Element l1 = doc.select("div#content").first();
            neirong = l1.html();
        } else {
            neirong = "出错了！";
        }
        return neirong;
    }

    @Override
    public void getsearch(Subscriber<List<Searchinfo>> s_obj, String guanjianzhi) {
        Observable.just(guanjianzhi)
                .map(new Func1<String, List<Searchinfo>>() {
                    @Override
                    public List<Searchinfo> call(String s) {
                        return getsearchpage(base_search_url + s,zhandian_ming);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s_obj);
    }

}