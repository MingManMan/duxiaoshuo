package com.beibei.mingmanman.readxiaoshuo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;
import com.beibei.mingmanman.readxiaoshuo.model.Zhandian_info;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {
    private RecyclerView Rv_xiaoshuo;
    private List<Xiaoshuo_info> mDatas = new ArrayList<Xiaoshuo_info>();
    private LinearLayout mycontainer;
    private List<String> xiaoshu_link_list = new ArrayList<String>();
    private List<String> zhandian = new ArrayList<String>();
    private  Integer zhandian_jishu=0;
    private List<Zhandian_info> zhandian_list=new ArrayList<Zhandian_info>();
    private MyAdapter recycleAdapter;//非瀑布流使用这个
    private GridLayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private static SQLiteDatabase db;
    private XiaoshuoDatabaseHelper dbHelper;
    private CompositeSubscription mCompositeSubscription;
    private Toolbar toolbar;
    private Myapp myapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //如果是继承Activity就应该调用
        //requestWindowFeature(Window.FEATURE_NO_TITLE) ）；
        setContentView(R.layout.activity_main);

        myapp=(Myapp)getApplication();

        initData();//正在阅读小说的信息加载
        initview();
        checkupdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("testcrab", "onResume");
        initData();
        recycleAdapter.notifyDataSetChanged();
    }

    private void initview() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mycontainer=(LinearLayout) findViewById(R.id.activity_main);
        // 如果你想修改标题和子标题的字体大小、颜色等，可以调用 setTitleTextColor 、
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        // setTitleTextAppearance 、 setSubtitleTextColor 、 setSubtitleTextAppearance 这些API；
        // 自定义的View位于 title 、 subtitle 和 actionmenu 之间，这意味着，如果 title 和 subtitle
        // 都在，且 actionmenu选项 太多的时候，留给自定义View的空间就越小；
        // 导航图标和 app logo 的区别在哪？如果你只设置 导航图标 （ or app logo ） 和
        // title 、 subtitle ，会发现 app logo 和 title 、 subtitle 的间距比较小，看起来不如
        // 导航图标 与 它们两搭配美观；

        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);//设置导航栏图标
        //toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("慢慢追书");//设置主标题
        //toolbar.setSubtitle("Subtitle");//设置子标题
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {  //搜索
                    Log.i("testcrab","mainActivity--- R.id.action_search"+myapp.Xuanzhe_zhandian);
                    Intent intent3 = new Intent();
                    //intent3.putExtra("Xuanzhe_zhandian", Xuanzhe_zhandian);  //放入目前选择的站点
                    intent3.setClass(MainActivity.this, Search.class);
                    startActivity(intent3);  //开始跳转

                } else if (menuItemId == R.id.action_notification) {
                    Snackbar.make(mycontainer, "SnackbarTest", Snackbar.LENGTH_LONG).show();
                  //  Toast.makeText(MainActivity.this, R.string.menu_notifications, Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_item1) {//添加站点信息
                    updatezhandian_info();
                    Toast.makeText(MainActivity.this, "站点信息添加成功", Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_item2) {//检查数据库内容
                    dbHelper = new XiaoshuoDatabaseHelper(MainActivity.this);
                    db = dbHelper.getWritableDatabase();
                    QueryResultIterable<Zhandian_info> iterable =
                            cupboard().withDatabase(db).query(Zhandian_info.class).query();
                    Log.i("testcrab","开始站点测试");
                    for (Zhandian_info bbb : iterable) {
                        Log.i("testcrab", bbb.zhandian_ming + "|" + bbb.zhandian_url + "|" + bbb.search_url);
                    }

                } else if (menuItemId == R.id.action_item3) {//站点反应速度检查
                    //========================站点反应速度检查======================================
                    Log.i("testcrab", "开始站点测试");
                    checkwebsite();
                }
                //========================切换站点======================================
                else if (menuItemId == R.id.action_zhandian1) {//笔趣阁1
                   myapp.Xuanzhe_zhandian="笔趣阁1";
                    switchzhandian(myapp.Xuanzhe_zhandian);
                    Snackbar.make(mycontainer, "笔趣阁1", Snackbar.LENGTH_LONG).show();

                }
                else if (menuItemId == R.id.action_zhandian2) {//笔趣阁2
                    myapp.Xuanzhe_zhandian="笔趣阁2";
                    Snackbar.make(mycontainer, "笔趣阁2", Snackbar.LENGTH_LONG).show();
                }
                else if (menuItemId == R.id.action_zhandian3) {//爱上书屋
                    myapp.Xuanzhe_zhandian="爱上书屋";
                    Snackbar.make(mycontainer, "爱上书屋", Snackbar.LENGTH_LONG).show();
                }
                else if (menuItemId == R.id.action_zhandian4) {//新八一中文网
                    //myapp.Xuanzhe_zhandian="新八一中文网";
                    Snackbar.make(mycontainer, "新八一中文网", Snackbar.LENGTH_LONG).show();
                }
                else if (menuItemId == R.id.action_zhandian5) {//八一中文网
                    //myapp.Xuanzhe_zhandian="八一中文网";
                    Snackbar.make(mycontainer, "八一中文网", Snackbar.LENGTH_LONG).show();

                }
                return true;
            }
        });
        Rv_xiaoshuo = (RecyclerView) findViewById(R.id.rv_xiaoshuo);
        //  布局管理器
        mLayoutManager = new GridLayoutManager(this, 2, OrientationHelper.VERTICAL, false);
        Rv_xiaoshuo.setLayoutManager(mLayoutManager);
        //适配器
        recycleAdapter = new MyAdapter(MainActivity.this, mDatas);
        Rv_xiaoshuo.setAdapter(recycleAdapter);
        //动画
        Rv_xiaoshuo.setItemAnimator(new DefaultItemAnimator());
        //分割线
        Rv_xiaoshuo.addItemDecoration(new DividerGridItemDecoration(this));
        //滑动，拖动处理
        mItemTouchHelper = new ItemTouchHelper(mItemTouchCallBack);
        mItemTouchHelper.attachToRecyclerView(Rv_xiaoshuo);
        //点击和长按处理
        recycleAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            //------------显示目录--------------------------------------------
            public void onItemClick(View view, int position) {
                myapp.xiaoshuo=mDatas.get(position);
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, MuluActivity.class);
                startActivity(intent2);  //开始跳转
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void switchzhandian(String xuanzhe_zhandian) {

    }

    //对付同步问题
    public synchronized  Integer get_web_count(){  return zhandian_jishu;  }
    public synchronized  void set_web_count(){ zhandian_jishu++;  }
    public synchronized  void set_zhandian_list(Zhandian_info z){
        //zhandian_list.add(z);
        Zhandian_info zd_info = cupboard().withDatabase(db).query(Zhandian_info.class).withSelection("zhandian_ming = ?", z.zhandian_ming).get();
        if (zd_info == null) {
            cupboard().withDatabase(db).put(z);
        } else {
            zd_info.sudu_paixv=z.sudu_paixv;
            cupboard().withDatabase(db).put(zd_info);
        }
    }

    //====================================站点活性检查==============================================
    private void checkwebsite() {
        mCompositeSubscription = new CompositeSubscription();
        QueryResultIterable<Zhandian_info> iterable =
                cupboard().withDatabase(db).query(Zhandian_info.class).query();
        zhandian_jishu=0;
        for (Zhandian_info zd : iterable) {
            mCompositeSubscription.add(Observable.just(zd)
                    .map(s->getwebpage(s))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createSubscriber()));
        }
    }
    //生成一个subscriber对象
    private  Subscriber<Zhandian_info>  createSubscriber(){
        return new Subscriber<Zhandian_info>() {
            @Override
            public void onCompleted() {
                //   mCompositeSubscription.unsubscribe();
                setZhandianData();
            }
            @Override
            public void onError(Throwable throwable) {
                setZhandianData();
            }
            @Override
            public void onNext(Zhandian_info s) {
                Log.i("testcrab","------>"+s.zhandian_ming);
                if(s.sudu_paixv>-1){
                    if(get_web_count()<10){  //6个站点
                        set_web_count();
                        s.sudu_paixv=get_web_count();
                        set_zhandian_list(s);
                    }else{
                        Log.i("testcrab","名额已满");
                        mCompositeSubscription.unsubscribe();
                    }
                }
            }
        };
    }

    private Zhandian_info getwebpage(Zhandian_info zd) {
        Connection conn = Jsoup.connect(zd.zhandian_url).timeout(5000);
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
        Document doc = null;
        Log.i("testcrab", "get-->" + zd.zhandian_ming+"  "+zd.zhandian_url);
        try {
            doc = conn.get();
            zd.sudu_paixv=0;
        } catch (IOException e) {
            zd.sudu_paixv =-1;
            e.printStackTrace();
            Log.i("testcrab", "error: ---" + e.toString());
        }
        return zd;
    }
//=================================================================================================
    //=======================向数据库添加站点信息
    private void updatezhandian_info() {
        database_data();

        for (String s : zhandian) {
            Zhandian_info zd = new Zhandian_info();
            String[] strarray = s.split("[|]");
            zd.zhandian_ming = strarray[0];
            zd.zhandian_url = strarray[1];
            zd.search_url = strarray[2];
            Zhandian_info zd_info = cupboard().withDatabase(db).query(Zhandian_info.class).withSelection("zhandian_ming = ?", zd.zhandian_ming).get();
            if (zd_info == null) {
                cupboard().withDatabase(db).put(zd);
            } else {
                zd = zd_info;
                cupboard().withDatabase(db).put(zd);
            }
        }
    }

    //初始化用的站点信息
    private void database_data() {
        zhandian.clear();
        zhandian.add("武林中文网|http://www.50zw.la|http://zhannei.baidu.com/cse/search?s=13049992925692302651&q=");
        zhandian.add("书客小说网|http://www.shuke.la|http://zhannei.baidu.com/cse/search?s=12268274549339958031&q=");
        zhandian.add("笔趣阁1|http://www.qu.la|http://zhannei.baidu.com/cse/search?s=7138806708853866527&q=");
        zhandian.add("笔趣阁2|http://www.biquge.tw|http://zhannei.baidu.com/cse/search?s=16829369641378287696&q=");
        zhandian.add("爱上书屋|http://www.23sw.net|http://zhannei.baidu.com/cse/search?s=3762194758325881047&q=");
        zhandian.add("盗梦人小说网|http://www.daomengren.com|http://zhannei.baidu.com/cse/search?s=12191870739823161026&q=");
        zhandian.add("品书网|http://www.vodtw.com|http://zhannei.baidu.com/cse/search?s=1101330821780029220&q=");
        zhandian.add("一本读|http://www.ybdu.com|http://zhannei.baidu.com/cse/search?s=6637491585052650179&q=");
        zhandian.add("三七中文|http://www.37zw.com|http://zhannei.baidu.com/cse/search?s=2041213923836881982&q=");
        zhandian.add("新八一中文网|http://www.x81zw.com|http://zhannei.baidu.com/cse/search?s=2988433831094058597&q=");
        zhandian.add("八一中文网|http://www.81zw.com|http://zhannei.baidu.com/cse/search?s=3975864432584690275&q=");
        zhandian.add("紫幽阁|http://www.ziyouge.com/|http://zhannei.baidu.com/cse/search?s=8978823581021836093&q=");

    }
//==========================================================================================================================

    //判断数据库是否存在，通过检查数据库文件是否存在来确定
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void initData() {
        dbHelper = new XiaoshuoDatabaseHelper(MainActivity.this);
        db = dbHelper.getWritableDatabase();
        setXiaoshuoData();
        setZhandianData();
    }
    private  void setZhandianData(){
        String selectionString = "sudu_paixv>=0 order by sudu_paixv";
        QueryResultIterable<Zhandian_info> iterable =
                cupboard().withDatabase(db).query(Zhandian_info.class).withSelection(selectionString).query();
        zhandian_list.clear();
        for (Zhandian_info bbb : iterable) {

            zhandian_list.add(bbb);
        }
    }
    private void setXiaoshuoData() {
        String selectionString = "list_order>=0 order by list_order";
        QueryResultIterable<Xiaoshuo_info> iterable =
                cupboard().withDatabase(db).query(Xiaoshuo_info.class).withSelection(selectionString).query();
        mDatas.clear();
        xiaoshu_link_list.clear();
        for (Xiaoshuo_info bbb : iterable) {
            mDatas.add(bbb);
        }
    }

    //====================================================================
    //判断是否数据有更新
    private void checkupdate() {

    }
    //==================================
    ItemTouchHelper.Callback mItemTouchCallBack = new ItemTouchHelper.Callback() {
        /**
         * 设置滑动类型标记
         *
         * @param recyclerView
         * @param viewHolder
         * @return
         *          返回一个整数类型的标识，用于判断Item那种移动行为是允许的
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.END);
        }

        /**
         * Item是否支持长按拖动
         *
         * @return
         *          true  支持长按操作
         *          false 不支持长按操作
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        /**
         * Item是否支持滑动
         *
         * @return
         *          true  支持滑动操作
         *          false 不支持滑动操作
         */
        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            //SPUtils.remove(MainActivity.this, mDatas.get(viewHolder.getAdapterPosition()));

            cupboard().withDatabase(db).delete(Xiaoshuo_info.class, "xiaoshuo_ming = ?", mDatas.get(viewHolder.getAdapterPosition()).xiaoshuo_ming);
            initData();
            //     recycleAdapter.deleteItem(viewHolder.getAdapterPosition());
            recycleAdapter.notifyDataSetChanged();
        }

        /**
         * 拖拽切换Item的回调
         *
         * @param recyclerView
         * @param viewHolder
         * @param target
         * @return
         *          如果Item切换了位置，返回true；反之，返回false
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            recycleAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        /**
         * Item被选中时候回调
         *
         * @param viewHolder
         * @param actionState
         *          当前Item的状态
         *          ItemTouchHelper.ACTION_STATE_IDLE   闲置状态
         *          ItemTouchHelper.ACTION_STATE_SWIPE  滑动中状态
         *          ItemTouchHelper#ACTION_STATE_DRAG   拖拽中状态
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            //  item被选中的操作
         /*   if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundResource(R.color.md_gray);
            }*/
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 用户操作完毕或者动画完毕后会被调用
         *
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 操作完毕后恢复颜色
            //viewHolder.itemView.setBackgroundResource(R.drawable.md_common_bg);
            super.clearView(recyclerView, viewHolder);
        }
    };

}
