package com.beibei.mingmanman.readxiaoshuo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class Search extends AppCompatActivity {
    private SearchView sv;
    private List<Searchinfo> searchinfolist;
    private RecyclerView lv;
    private GridLayoutManager mLayoutManager;
    private SearchlistAdapter recycleAdapter;
    private  String keyword="";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);//设置导航栏图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Search.this, "click 返回 item", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(Search.this, MainActivity.class);
                startActivity(intent);  //开始跳转
            }
        });
        searchinfolist = new ArrayList<Searchinfo>();
        lv = (RecyclerView) findViewById(R.id.lv); //展现list
        mLayoutManager = new GridLayoutManager(this, 1, OrientationHelper.VERTICAL, false);
        lv.setLayoutManager(mLayoutManager);
        recycleAdapter = new SearchlistAdapter(Search.this, searchinfolist);
        lv.setAdapter(recycleAdapter);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerGridItemDecoration(this));
        recycleAdapter.setOnItemClickListener(new SearchlistAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(Search.this, "click 11111" + position + " item", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(Search.this, "long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });

        sv = (SearchView) findViewById(R.id.search);
        //设置默认是否缩小为图标
        sv.setIconifiedByDefault(false);
        //是否显示search按钮
        sv.setSubmitButtonEnabled(true);

        //设置隐藏左边搜索图标
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) sv.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        magId=getResources().getIdentifier("android:id/search_go_btn", null, null);
        ImageView btnimage=(ImageView)sv.findViewById(magId);
        btnimage.setImageResource(R.mipmap.ic_search_white_24dp);
        sv.setQueryHint("请输入小说名");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Search.this, query, Toast.LENGTH_SHORT).show();
                keyword=query;
                Log.i("testcrab", "Search ========rxjava搜索==========="+query);
                search_rxjava(keyword);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //===================================================================
    Subscriber<List<Searchinfo>> CreatSubscriber(){
        return new Subscriber<List<Searchinfo>>() {
            @Override
            public void onCompleted() {        }
            @Override
            public void onError(Throwable e) {
                Searchinfo a=new Searchinfo();
                a.xiaoshuo_name="错误";
                a.xiaoshuo_jianjie=e.toString();
                searchinfolist.add(a);  }
            @Override
            public void onNext(List<Searchinfo> s) {
                searchinfolist.clear();
                searchinfolist.addAll(s);
                recycleAdapter.notifyDataSetChanged();
            }
        };
    }

    public void search_rxjava(String s){
        ZhandianInfterface xiaoshuo = new ZhandianB();
        xiaoshuo.getsearch(CreatSubscriber(),s );
    }

}
