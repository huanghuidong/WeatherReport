package com.example.huanghuidong.weatherreport.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.huanghuidong.weatherreport.db.WeatherReportDB;
import com.example.huanghuidong.weatherreport.model.City;
import com.example.huanghuidong.weatherreport.model.County;
import com.example.huanghuidong.weatherreport.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by huanghuidong on 2016/1/31.
 */
public class Utility {

    public synchronized static boolean handleProvincesResponse(WeatherReportDB weatherReportDB, String response){

        if (!TextUtils.isEmpty(response)){
            String [] allProvinces = response.split(",");
            if ((allProvinces!=null) && (allProvinces.length>0)){
                for (String p:allProvinces){
                    String[] array=p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    weatherReportDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(WeatherReportDB weatherReportDB,String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if ((allCities!=null) && (allCities.length>0)){
                for (String c : allCities){
                    String[] array=c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    weatherReportDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountiesResponse(WeatherReportDB weatherReportDB,String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");

            if ((allCounties!=null) && (allCounties.length>0)){

                for (String c1 : allCounties){
                    String[] array = c1.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    weatherReportDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName= weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String tempHigh = weatherInfo.getString("temp1");
            String tempLow = weatherInfo.getString("temp2");
            String weatherDesp=weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,tempHigh,tempLow,weatherDesp,publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String cityName, String weatherCode,String tempHigh, String tempLow,String weatherDesp, String publishTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("tempHigh", tempHigh);
        editor.putString("tempLow", tempLow);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }

}
