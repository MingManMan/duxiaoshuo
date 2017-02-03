package com.beibei.mingmanman.readxiaoshuo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MuluActivity extends AppCompatActivity {
    private Button shang_yi_ye, xia_yi_ye;
    private ListView lv;
    List<String> mDatas = new ArrayList<String>();
    private LinearLayout mycontainer;
    String xiaoshuo_ming, xiaoshuo_mulu_dizhi, baselink;
    Integer xiaoshuo_ming_postion = 0;
    private List<String> xiaoshuo_address, xiaoshuo_mulu_show, xiaoshuo_mulu_link, adapter_show;
    ArrayAdapter<String> adapter;
    int listsize = 0;
    int all_index = 0;
    private static final int pagecount = 5000;  //每页50条记录
    private int pageindex = 0;//list显示中的起始序号
    private Toolbar toolbar;
    private Myapp myapp;

    private static SQLiteDatabase db;
    private XiaoshuoDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulu);

        myapp = (Myapp) getApplication();
        initData(); //初始化，无实际操作
        initView(); //初始化View
        Rx_get_xiaoshuo_zhangjie_info();//获得小说章节信息（章节名称，章节对应连接）
    }

    private void initView() {
        mycontainer = (LinearLayout) findViewById(R.id.activity_main);
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
        toolbar.setTitle(myapp.xiaoshuo.xiaoshuo_ming);//设置主标题
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
                //========================切换站点======================================
                else {
                    if (menuItemId == R.id.action_zhandian1) {//笔趣阁1
                        myapp.Xuanzhe_zhandian = "笔趣阁1";
                        Snackbar.make(mycontainer, "笔趣阁1", Snackbar.LENGTH_LONG).show();
                        switchzhandian(myapp.Xuanzhe_zhandian);
                    } else if (menuItemId == R.id.action_zhandian2) {//笔趣阁2
                        myapp.Xuanzhe_zhandian = "笔趣阁2";
                        Snackbar.make(mycontainer, "笔趣阁2", Snackbar.LENGTH_LONG).show();
                        switchzhandian(myapp.Xuanzhe_zhandian);
                    } else if (menuItemId == R.id.action_zhandian3) {//爱上书屋
                        myapp.Xuanzhe_zhandian = "爱上书屋";
                        Snackbar.make(mycontainer, "爱上书屋", Snackbar.LENGTH_LONG).show();
                        switchzhandian(myapp.Xuanzhe_zhandian);
                    } else if (menuItemId == R.id.action_zhandian4) {//新八一中文网
                        //Xuanzhe_zhandian="新八一中文网";
                        Snackbar.make(mycontainer, "新八一中文网", Snackbar.LENGTH_LONG).show();
                        switchzhandian(myapp.Xuanzhe_zhandian);
                    } else if (menuItemId == R.id.action_zhandian5) {//八一中文网
                        //Xuanzhe_zhandian="八一中文网";
                        Snackbar.make(mycontainer, "八一中文网", Snackbar.LENGTH_LONG).show();
                        switchzhandian(myapp.Xuanzhe_zhandian);
                    }
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

    //----------------------站点切换---------------------------
    private void switchzhandian(String xuanzhe_zhandian) {
        Xiaoshuo_info xs_info =
                cupboard().withDatabase(db).query(Xiaoshuo_info.class).withSelection("xiaoshuo_ming = ? and zhandian_ming= ? ", myapp.xiaoshuo.xiaoshuo_ming, myapp.Xuanzhe_zhandian).get();
        //-----------------对应站点小说记录是否存在判断
        if (xs_info == null) {//-------- 不存在，调用对应search函数生成记录
            Log.i("testcrab","记录不存在");
            Log.i("testcrab","调用get_xiaoshuo_mulu_url_rxjava函数，进行添加");
            Rx_get_xiaoshuo_mulu_url_byname();
        } else {//------存在，用记录填充xiaoshuo对象
            Log.i("testcrab","=====================");
            Log.i("testcrab",xs_info.xiaoshuo_ming);
            Log.i("testcrab",xs_info.zhandian_ming);
            Log.i("testcrab","=====================");
            myapp.xiaoshuo.xiaoshuo_ming=xs_info.xiaoshuo_ming;
            myapp.xiaoshuo.zhandian_ming=xs_info.zhandian_ming;
            myapp.xiaoshuo.xiaoshuo_base_url=xs_info.xiaoshuo_base_url;
            myapp.xiaoshuo.xiaoshuo_mulu_url=xs_info.xiaoshuo_mulu_url;
            myapp.xiaoshuo.list_order=1;

        }

        Rx_get_xiaoshuo_zhangjie_info();
        //刷新列表
    }


    //================通过小说名获得小说对应站点的目录地址（非小说每章节的地址）================
    private void Rx_get_xiaoshuo_mulu_url_byname() {
        //启动线程获得目录页面内容
        Zhandian_Maker zm = new Zhandian_Maker();
        ZhandianInfterface xiaoshuo1 = zm.maker_zhandian(myapp.Xuanzhe_zhandian);
        xiaoshuo1.get_xiaoshuo_mulu_url_byname(S_obj_byname, myapp.xiaoshuo.xiaoshuo_ming);
    }
    Subscriber<Searchinfo> S_obj_byname = new Subscriber<Searchinfo>() {
        @Override
        public void onCompleted() {
            Log.i("testcrab","通过小说名字获得小说目录地址完成");

        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(MuluActivity.this, "error:" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("testcrab", e.toString());
        }
        @Override
        public void onNext(Searchinfo s) {
            Log.i("testcrab","==================S_obj_get_mulu_url===============");
            //数据对错判断
            if (s.xiaoshuo_name.equals("none")) {  //-----没有找到
                String msg="站点："+myapp.Xuanzhe_zhandian+"为找到小说："+myapp.xiaoshuo.xiaoshuo_ming;
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MuluActivity.this);
                builder.setTitle("搜索错误");
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                builder.show();
            } else {//----  找到了
                //添加：到数据库
                Xiaoshuo_info xiaoshuo_info = new Xiaoshuo_info();
                myapp.xiaoshuo.xiaoshuo_ming=s.xiaoshuo_name;
                myapp.xiaoshuo.zhandian_ming= s.zhandian_ming;
                myapp.xiaoshuo.xiaoshuo_base_url=s.xiaoshuo_base_url;
                myapp.xiaoshuo.xiaoshuo_mulu_url=s.xiaoshuo_mulu_url;
                myapp.xiaoshuo.list_order=1;
                myapp.xiaoshuo.xiaoshuo_zhangjie=0;
                myapp.xiaoshuo.xiaoshuo_yuedu_zhangjie=0;
                cupboard().withDatabase(db).put(myapp.xiaoshuo);
                Log.i("testcrab","数据库添加完成");

            }
        }
    };
    //==========================点击后打开阅读界面======================
    private void go_dushu() {
       /* Log.i("testcrab","baselink:"+baselink);
        Log.i("testcrab","all_index"+all_index);
        Log.i("testcrab","章节对应目录:"+xiaoshuo_mulu_link.size());
        Log.i("testcrab",myapp.xiaoshuo.xiaoshuo_ming);
        Log.i("testcrab",myapp.xiaoshuo.zhandian_ming);*/
        Intent intent = new Intent();
        intent.putExtra("baselink", myapp.xiaoshuo.xiaoshuo_base_url);  //放入数据
        intent.putExtra("all_index", all_index);  //放入数据,连接位置
        intent.putExtra("xiaoshuo_mulu_link", (Serializable) xiaoshuo_mulu_link);
        intent.putExtra("xiaoshuo", myapp.xiaoshuo);  //放入数据
        intent.setClass(MuluActivity.this, ReadActivity.class);
        startActivity(intent);  //开始跳转
    }
    //===========获得章节信息==============================
    private void Rx_get_xiaoshuo_zhangjie_info() {
        Log.i("testcrab","主要看目录切换后是否会调用该函数");
        Zhandian_Maker zm = new Zhandian_Maker();
        ZhandianInfterface xiaoshuo1 = zm.maker_zhandian(myapp.xiaoshuo.zhandian_ming);
        xiaoshuo1.getmulu(S_obj, myapp.xiaoshuo.xiaoshuo_mulu_url);
    }
    Subscriber<List<Mulu_info>> S_obj = new Subscriber<List<Mulu_info>>() {
        @Override
        public void onCompleted() {
            Log.i("testcrab","小说章节信息获得全程偶看了");
        }
        @Override
        public void onError(Throwable e) {
            Toast.makeText(MuluActivity.this, "error:" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("testcrab", e.toString());
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
            Log.i("testcrab","目录更新后----章节地址:"+xiaoshuo_mulu_link.get(0));
            Collections.reverse(xiaoshuo_mulu_show);
            Collections.reverse(xiaoshuo_mulu_link);
///////////////////////////
            listsize = xiaoshuo_mulu_link.size();
            adapter_show.addAll(xiaoshuo_mulu_show);
            adapter.notifyDataSetChanged();
            ///////////////////////////////
        }
    };

    private void initData() {
        dbHelper = new XiaoshuoDatabaseHelper(MuluActivity.this);
        db = dbHelper.getWritableDatabase();
        xiaoshuo_address = new ArrayList<String>();
        xiaoshuo_mulu_show = new ArrayList<String>();
        xiaoshuo_mulu_link = new ArrayList<String>();
        adapter_show = new ArrayList<String>();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Rx_get_xiaoshuo_zhangjie_info();
    }
}
