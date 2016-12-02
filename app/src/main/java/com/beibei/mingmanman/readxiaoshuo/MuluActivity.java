package com.beibei.mingmanman.readxiaoshuo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;
import com.beibei.mingmanman.readxiaoshuo.model.Zhandian_info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;
import rx.Subscriber;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MuluActivity extends AppCompatActivity {
    private Button shang_yi_ye, xia_yi_ye;
    private ListView lv;
    List<String> mDatas = new ArrayList<String>();
    String xiaoshuo_ming, xiaoshuo_mulu_dizhi, baselink;
    Integer xiaoshuo_ming_postion = 0;
    private List<String> xiaoshuo_address, xiaoshuo_mulu_show, xiaoshuo_mulu_link, adapter_show;
    ArrayAdapter<String> adapter;
    int listsize = 0;
    int all_index = 0;
    private static final int pagecount = 5000;  //每页50条记录
    private int pageindex = 0;//list显示中的起始序号
    Xiaoshuo_info xiaoshuo;

    private Toolbar toolbar;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulu);
        get_chanshu();
        initData();
        initView();
        get_mulu();
    }
    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 如果你想修改标题和子标题的字体大小、颜色等，可以调用 setTitleTextColor 、
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        // setTitleTextAppearance 、 setSubtitleTextColor 、 setSubtitleTextAppearance 这些API；
        // 自定义的View位于 title 、 subtitle 和 actionmenu 之间，这意味着，如果 title 和 subtitle
        // 都在，且 actionmenu选项 太多的时候，留给自定义View的空间就越小；
        // 导航图标和 app logo 的区别在哪？如果你只设置 导航图标 （ or app logo ） 和
        // title 、 subtitle ，会发现 app logo 和 title 、 subtitle 的间距比较小，看起来不如
        // 导航图标 与 它们两搭配美观；
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);//设置导航栏图标
        toolbar.setTitle(xiaoshuo.xiaoshuo_ming);//设置主标题
        toolbar.inflateMenu(R.menu.base_toolbar_mulu_menu);//设置右上角的填充菜单

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MuluActivity.this, "click 返回 item", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MuluActivity.this, MainActivity.class);
                startActivity(intent);  //开始跳转
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_sort) {  //排序
                    Collections.reverse(xiaoshuo_mulu_show);
                    Collections.reverse(xiaoshuo_mulu_link);
                    Collections.reverse(adapter_show);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        lv = (ListView) findViewById(R.id.lv);
        adapter = new ArrayAdapter<String>(MuluActivity.this, android.R.layout.simple_expandable_list_item_1, adapter_show);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {//点击进入阅读
                //点击后在标题上显示点击了第几行
                all_index = /*pageindex - pagecount +*/ arg2;
                //打开阅读页面
                go_dushu();
            }
        });
    }

    private void go_dushu() {
        Intent intent = new Intent();
        intent.putExtra("baselink", baselink);  //放入数据
        intent.putExtra("all_index", all_index);  //放入数据,连接位置
        intent.putExtra("xiaoshuo_mulu_link", (Serializable) xiaoshuo_mulu_link);
        intent.setClass(MuluActivity.this, ReadActivity.class);
        startActivity(intent);  //开始跳转
    }

    private void get_chanshu() {
        Intent intent = this.getIntent();    //获得当前的Intent
        Bundle bundle = intent.getExtras();  //获得全部数据
        xiaoshuo = bundle.getParcelable("xiaoshuo");
        Log.i("testcrab","get_chanshu  获得小说："+xiaoshuo.xiaoshuo_ming);
    }

    private void get_mulu() {
        //根据小说名，获得小说的站点地址
        baselink = xiaoshuo.xiaoshuo_base_url;
        xiaoshuo_mulu_dizhi = xiaoshuo.xiaoshuo_mulu_url;
        //根据地址获得目录
        Log.i("testcrab","get_mulu "+xiaoshuo_mulu_dizhi);
        get_xiaoshuo_mulu_rxjava();
    }

    Subscriber<List<Mulu_info>> S_obj = new Subscriber<List<Mulu_info>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(MuluActivity.this, "error:"+e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("testcrab",e.toString());
        }

        @Override
        public void onNext(List<Mulu_info> s) {
            //清空原有目录
            xiaoshuo_mulu_show.clear();  //显示
            xiaoshuo_mulu_link.clear();  //对应地址
            for (Mulu_info a : s) {
                xiaoshuo_mulu_show.add(a.mingzhi);
                xiaoshuo_mulu_link.add(a.dizhi);
            }
            Collections.reverse(xiaoshuo_mulu_show);
            Collections.reverse(xiaoshuo_mulu_link);
///////////////////////////
            listsize = xiaoshuo_mulu_link.size();
            //分页用
         /*   adapter_show.clear();
            if (pagecount <= listsize) {
                adapter_show.addAll(xiaoshuo_mulu_show.subList(0, pagecount));
                pageindex = pagecount;
            } else {
                adapter_show.addAll(xiaoshuo_mulu_show.subList(0, listsize));
                pageindex = listsize;
            }*/
            adapter_show.addAll(xiaoshuo_mulu_show);
            adapter.notifyDataSetChanged();
            ///////////////////////////////
        }
    };

    private void get_xiaoshuo_mulu_rxjava() {
        //启动线程获得目录页面内容
        XiaoshuoInfterface xiaoshuo1 = new Xiaoshuo1(xiaoshuo);
        xiaoshuo1.getmulu(S_obj, xiaoshuo_mulu_dizhi);
        //xiaoshuo1.getmulu(S_obj, xiaoshuo);
    }
    private List<String> get_xiaoshuo_mulu_dizhi(String xiaoshuo_ming) {

        List<String> mulu_dizhi = new ArrayList<String>();
        for (int i = 0; i < xiaoshuo_address.size(); i++) {
            String tmpstr = xiaoshuo_address.get(i);
            if (tmpstr.contains(xiaoshuo_ming + "|")) {
                String[] strarray = tmpstr.split("[|]");
                mulu_dizhi.add(strarray[0]);
                mulu_dizhi.add(strarray[1]);
                mulu_dizhi.add(strarray[2]);
                mulu_dizhi.add(strarray[3]);
                break;
            }
        }
        return mulu_dizhi;
    }

    private void initData() {
        xiaoshuo_address = new ArrayList<String>();
        xiaoshuo_mulu_show = new ArrayList<String>();
        xiaoshuo_mulu_link = new ArrayList<String>();
        adapter_show = new ArrayList<String>();
    }

}
