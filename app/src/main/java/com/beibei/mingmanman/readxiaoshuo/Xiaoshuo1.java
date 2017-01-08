package com.beibei.mingmanman.readxiaoshuo;

import android.util.Log;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mingmanman on 2016/11/1.
 */
public class Xiaoshuo1 implements XiaoshuoInfterface {
    private Xiaoshuo_info xiaoshuo;
    private String zhandian_ming = "笔趣阁2";
    private String base_search_url = "http://zhannei.baidu.com/cse/search?s=16829369641378287696&q=";
    List<Mulu_info> mulu_list = new ArrayList<Mulu_info>();

    public Xiaoshuo1() {
        this.mulu_list = new ArrayList<Mulu_info>();
    }
    public Xiaoshuo1(Xiaoshuo_info xiaoshuo){
        this.mulu_list = new ArrayList<Mulu_info>();
        this.xiaoshuo=xiaoshuo;
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
                        return getsearchpage(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s_obj);
    }

    private List<Searchinfo> getsearchpage(String s) {
        List<Searchinfo> searchinfolist = null;
        Document doc = getweb(base_search_url + s);
        if (doc != null) {
            searchinfolist = new ArrayList<Searchinfo>();
            // 获取下一页的链接
            Elements dds = doc.select(".result-item");
            Log.i("testcrab", "" + dds.size());
            for (Element element : dds) {
                try {
                    Searchinfo aaa = new Searchinfo();
                    aaa.zhandian_ming = zhandian_ming;
                    Element pic = element.select(".result-game-item-pic-link-img").first();
                    aaa.img_url = pic.attr("src");
                    Log.i("testcrab", ".result-game-item-pic-link-img:" + aaa.img_url);
                    aaa.xiaoshuo_name = element.select(".result-item-title").first().text();
                    Log.i("testcrab", ".result-item-title" + aaa.xiaoshuo_name);
                    aaa.xiaoshuo_base_url = element.select(".result-game-item-title-link").first().attr("href");
                    Log.i("testcrab", ".result-game-item-title-link" + aaa.xiaoshuo_base_url);
                    aaa.xiaoshuo_mulu_url = aaa.xiaoshuo_base_url;
                    aaa.xiaoshuo_jianjie = element.select(".result-game-item-desc").first().text();
                    Log.i("testcrab", ".result-game-item-desc" + aaa.xiaoshuo_jianjie);
                    Elements tmp_info = element.select(".result-game-item-info-tag");
                    aaa.xiaoshuo_info = "";
                    for (Element e : tmp_info) {
                        Log.i("testcrab", "tmp_info");
                        aaa.xiaoshuo_info = aaa.xiaoshuo_info + e.text() + "  ";
                    }
                    searchinfolist.add(aaa);
                    Log.i("testcrab", "=============================================");

                } catch (Exception e) {

                }
            }
        }
        return searchinfolist;
    }

    private Document getweb(String url) {
        Connection conn = Jsoup.connect(url).timeout(5000);
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
        Document doc = null;
        try {
            doc = conn.get();
        } catch (IOException e) {

        }
        return doc;
    }
}
