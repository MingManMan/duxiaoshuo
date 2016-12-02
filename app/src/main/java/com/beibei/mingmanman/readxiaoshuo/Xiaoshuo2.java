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
 * Created by mingmanman on 2016/11/2.
 */
public class Xiaoshuo2 implements XiaoshuoInfterface {
    private Xiaoshuo_info xiaoshuo;
    private String zhandian_ming = "读零零小说网";
    private String base_search_url = "http://zhannei.baidu.com/cse/search?s=6162748167861710953&q=";
    List<Mulu_info> mulu_list = new ArrayList<Mulu_info>();

    public Xiaoshuo2() {
        this.mulu_list = new ArrayList<Mulu_info>();
    }

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
            Element l1 = doc.select("#pagecontent").first();
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
        List<Searchinfo> searchinfolist = new ArrayList<Searchinfo>();
        Document doc = getweb(base_search_url + s);
        if (doc != null) {
            Elements dds = doc.select(".result-game-item");
            for (Element element : dds) {
                Searchinfo aaa = new Searchinfo();
                aaa.zhandian_ming = zhandian_ming;
                Element pic = element.select(".result-game-item-pic-link-img").first();
                aaa.img_url = pic.attr("src");
                aaa.xiaoshuo_name = element.select(".result-game-item-title").first().text();
                aaa.xiaoshuo_base_url = element.select(".result-game-item-title-link").first().attr("href");
                aaa.xiaoshuo_mulu_url = aaa.xiaoshuo_base_url;
                Elements tmp_info = element.select(".result-game-item-info-tag");
                aaa.xiaoshuo_info = "";

                for (Element e : tmp_info) {
                    aaa.xiaoshuo_info = aaa.xiaoshuo_info + e.text() + "  ";
                }
                searchinfolist.add(aaa);
            }
        }
        return searchinfolist;
    }

    private Document getweb(String url) {
        Log.i("testcrab","Xiaoshuo2  getweb "+url);
        Connection conn = Jsoup.connect(url).timeout(5000);
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
        Document doc = null;
        try {
            doc = conn.get();
        } catch (IOException e) {
            Log.i("testcrab","error:"+e.toString());
        }
        return doc;
    }
}
