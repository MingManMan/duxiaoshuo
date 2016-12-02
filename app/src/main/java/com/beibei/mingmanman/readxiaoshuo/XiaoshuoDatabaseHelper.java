package com.beibei.mingmanman.readxiaoshuo;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.beibei.mingmanman.readxiaoshuo.model.Zhandian_info;
import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;
/**
 * Created by mymac on 2016/10/24.
 */

public class XiaoshuoDatabaseHelper extends SQLiteOpenHelper {
    private static  final  String DATABASE_NAME="kanxiaoshuo.db";//数据库名称
    private static  final  int DATABASE_VERSION=1;//版本
    public  XiaoshuoDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    static{
        cupboard().register(Xiaoshuo_info.class);
        cupboard().register(Zhandian_info.class);
        //cupboard().register(Read_info.class);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级表
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
    }
}
