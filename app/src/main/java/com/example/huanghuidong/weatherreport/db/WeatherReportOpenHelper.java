package com.example.huanghuidong.weatherreport.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huanghuidong on 2016/1/31.
 */
public class WeatherReportOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_PROVINCE = "create table Province(" +
            " id integer primary key autoincrement, " +
            " province_name text, " +
            " province_code text)";

    public static final String CREATE_CITY = "create table City(" +
            " id integer primary key autoincrement, " +
            " city_name text, " +
            " city_code text)";

    public static final String CREATE_COUNTY = "create table County(" +
            "id integer primary key autoincrement, " +
            "county_name text," +
            "county_code text)";

    public WeatherReportOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    // 执行db.getWritableDatabase()方法时执行此Oncreate()
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);

    }

    @Override
    //实例化对象的时候判断版本号，如果newVersion比现有版本高，则会执行此upgrade方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 0:           //oldVersion=0，表示原来没有，重新建立DB
                db.execSQL("drop table if exists Province");
                db.execSQL("drop table if exists City");
                db.execSQL("drop table if exists County");
                break;
//           case 1:         //oldVersion=1，表示原来有，改变一个字段即可
//                db.execSQL("alter table City add column Remark text");
            default:

        }

    }
}
