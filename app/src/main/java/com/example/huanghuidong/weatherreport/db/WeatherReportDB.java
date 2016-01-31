package com.example.huanghuidong.weatherreport.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.huanghuidong.weatherreport.model.City;
import com.example.huanghuidong.weatherreport.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuidong on 2016/1/31.
 */
public class WeatherReportDB {
    public static final String DB_NAME="WeatherReport";
    public static final int CURRENT_VERSION = 1;
    private static WeatherReportDB weatherReportDB;
    private SQLiteDatabase db;


    private WeatherReportDB(Context context) {
        WeatherReportOpenHelper dbHelper = new WeatherReportOpenHelper(context, DB_NAME, null, CURRENT_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static WeatherReportDB getInstance(Context context){
        if (weatherReportDB==null){
            weatherReportDB = new WeatherReportDB(context);
        }
        return weatherReportDB;
    }

    public void saveProvince(Province province){
        if (province!=null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name",province.getProvinceName());
            contentValues.put("province_code",province.getProvinceCode());
            db.insert("Province", null, contentValues);
        }
    }

    public List<Province> loadProvinces(){
        List<Province> provinceList = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null,null,null,null,null,"province_name");

        if (cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                provinceList.add(province);
            }while(cursor.moveToNext());
        }

        if (cursor!=null){
            cursor.close();
        }

        return provinceList;
    }

    public void saveCity(City city){
        if (city!=null){
            ContentValues contentValues= new ContentValues();
            contentValues.put("city_name",city.getCityName());
            contentValues.put("city_coee",city.getCityCode());
            contentValues.put("province_id",city.getProvinceId());
            db.insert("City",null,contentValues);
        }
    }

//    public List<City> loadCities(int provinceId){
//
//    }
}
