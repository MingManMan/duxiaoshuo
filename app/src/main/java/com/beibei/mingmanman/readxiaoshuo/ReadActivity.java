package com.beibei.mingmanman.readxiaoshuo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

import rx.Subscriber;
import rx.Subscription;

public class ReadActivity extends AppCompatActivity {
    TextView textView2;
    Button btn_prepage,btn_index,btn_nextpage;
    String baselink="";
    String url = "";
    String neirong = "no!!";
    EditText et_neirong;
    List<String> linkaddress = new ArrayList<String>();
    Subscription sbtion;
    int all_index=0;
    int xiaoshuolistsize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        get_chanshu();
        initView();
        get_zhangjie_leirong_rxjava( baselink + linkaddress.get(all_index));
    }
    private  Subscriber<String> CreateSubscriber(){
        return   new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable  e) {
                et_neirong.setText(e.toString());
            }
            @Override
            public void onNext(String s) {
                CharSequence mytext = Html.fromHtml(s);
                //String tmp_a=htmlreplace(s);
                et_neirong.setText(mytext);
            }
        };
    }
    public void get_zhangjie_leirong_rxjava( String url){
        Log.i("testcrab","ReadActivity get_zhangjie_leirong_rxjava url:"+url);
        ZhandianInfterface xiaoshuo1 = new ZhandianC();
        xiaoshuo1.getxiaoshuoneirong(CreateSubscriber(), url);
    }

    String htmlreplace(String in_string) {
        Pattern pattern = Pattern.compile("<br />");
        Matcher matcher = pattern.matcher(in_string);
        if( matcher.find()){
            Log.v("Testcrab","找到了");
            return matcher.replaceAll("\r\n");
        }else
        {
            Log.v("Testcrab","没找到");
            return  in_string;
        }

    }
       private void initView(){
        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setMovementMethod(ScrollingMovementMethod.getInstance());
        et_neirong=(EditText)findViewById(R.id.editText);

        btn_prepage=(Button)findViewById(R.id.btn_prepage);
        btn_prepage.setOnClickListener(listener);

        btn_index=(Button)findViewById(R.id.btn_index);
        btn_index.setOnClickListener(listener);

        btn_nextpage=(Button)findViewById(R.id.btn_nextpage);
        btn_nextpage.setOnClickListener(listener);

    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button view = (Button) v;
            switch (view.getId()) {
                case R.id.btn_prepage:
                    all_index=all_index-1;
                    if(all_index>=0) {
                       // url = linkaddress.get(all_index);
                       /* url = baselink + linkaddress.get(all_index);
                        new Thread(runnable).start();*/
                        get_zhangjie_leirong_rxjava( baselink + linkaddress.get(all_index));
                    }
                    break;
                case R.id.btn_index:
                    break;
                case R.id.btn_nextpage:
                    all_index=all_index+1;
                    if(all_index<xiaoshuolistsize) {
                       // url = linkaddress.get(all_index);
                        /*url = baselink + linkaddress.get(all_index);
                        new Thread(runnable).start();*/
                        get_zhangjie_leirong_rxjava( baselink + linkaddress.get(all_index));
                    }
                    break;
            }
        }
    };
    private  void  get_chanshu(){
        Intent intent = this.getIntent();    //获得当前的Intent
        Bundle bundle = intent.getExtras();  //获得全部数据
        baselink = bundle.getString("baselink");  //获得名为name的值
        all_index = bundle.getInt("all_index");
        linkaddress = (List<String>) bundle.getSerializable("xiaoshuo_mulu_link");
        xiaoshuolistsize=linkaddress.size();

    }
}
