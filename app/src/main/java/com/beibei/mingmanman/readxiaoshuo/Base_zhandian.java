package com.beibei.mingmanman.readxiaoshuo;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mymac on 1/8 0008.
 */

public class Base_zhandian {
    public Document getweb(String url) {
        Connection conn = Jsoup.connect(url).timeout(5000);
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
        Document doc = null;
        try {
            doc = conn.get();
        } catch (IOException e) {

        }
        return doc;
    }
    public List<Searchinfo> getsearchpage(String search_url,String zhandian_ming) {
        List<Searchinfo> searchinfolist = null;
        Document doc = getweb(search_url);
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

                    try{
                        aaa.xiaoshuo_jianjie = element.select(".result-game-item-desc").first().text();
                    }catch (Exception e) {
                        aaa.xiaoshuo_jianjie="无";
                    }
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
}
