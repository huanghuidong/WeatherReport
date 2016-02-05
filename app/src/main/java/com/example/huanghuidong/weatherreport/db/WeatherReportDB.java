package com.example.huanghuidong.weatherreport.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.huanghuidong.weatherreport.model.City;
import com.example.huanghuidong.weatherreport.model.County;
import com.example.huanghuidong.weatherreport.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuidong on 2016/1/31.
 */
public class WeatherReportDB {
    public static final String DB_NAME="WeatherReport";
    public static final int CURRENT_VERSION = 3;
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

        if (cursor!= null){
            cursor.close();
        }
        return provinceList;
    }

    public void saveCity(City city){
        if (city!=null){
            ContentValues contentValues= new ContentValues();
            contentValues.put("city_name",city.getCityName());
            contentValues.put("city_code",city.getCityCode());
            contentValues.put("province_id",city.getProvinceId());
            db.insert("City", null, contentValues);
        }
    }

    public List<City> loadCities(int provinceId){
        List <City> cityList = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);

        if (cursor.moveToFirst()){
            City city = new City();
            do {
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(provinceId);
                cityList.add(city);
            }while(cursor.moveToNext());
        }

        if (cursor!= null){
            cursor.close();
        }

        return cityList;
    }

    public void saveCounty(County county){
        if (county !=null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("county_name",county.getCountyName());
            contentValues.put("county_code",county.getCountyCode());
            contentValues.put("city_id",county.getCityId());
            db.insert("County", null, contentValues);
        }
    }

    public List <County> loadCounties(int cityId){
        List <County> countyList = new ArrayList<County>();
        Cursor cursor = db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);

        if (cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                countyList.add(county);
            }while(cursor.moveToNext());
        }

        if (cursor!= null){
            cursor.close();
        }

        return countyList;
    }
}
