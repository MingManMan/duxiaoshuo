package com.beibei.mingmanman.readxiaoshuo;

import android.util.Log;

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
 * Created by mingmanman on 2017/1/11.
 */

public class ZhandianC extends Base_zhandian implements ZhandianInfterface {
    private Zhandian_info zhandian;
    private String zhandian_ming = "笔趣阁2";
    private String base_search_url = "http://zhannei.baidu.com/cse/search?s=16829369641378287696&q=";
    List<Mulu_info> mulu_list = new ArrayList<Mulu_info>();

    public ZhandianC() {
        this.mulu_list = new ArrayList<Mulu_info>();
    }
    public ZhandianC(Zhandian_info zhandian){
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

    //=========精确返回一个搜索小说在本站点的连接=======================
    @Override
    public void get_xiaoshuo_mulu_url_byname(Subscriber<Searchinfo> s_obj, String guanjianzhi) {
        Observable.just(guanjianzhi)
                .map(s -> getsearchxiaoshuo_mulu_url(base_search_url + s, zhandian_ming, s))
                .map(s-> shujuzhengli(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s_obj);
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
        Log.i("testcrab","站点C---------getmulupage函数");
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
            Log.i("testcrab","站点C---------getneirongpage");
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
                .map(s->shujuzhengli(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s_obj);
    }
    public List<Searchinfo> shujuzhengli(List<Searchinfo> s) {
        for(int i=0;i<s.size();i++){
            Searchinfo aa=s.get(i);
            aa.xiaoshuo_base_url="http://www.biquge.tw";
            s.set(i,aa);
        }
        return  s;
    }
    public Searchinfo shujuzhengli(Searchinfo s) {
            s.xiaoshuo_base_url="http://www.biquge.tw";
            return  s;
    }

}