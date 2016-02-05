package com.example.huanghuidong.weatherreport.util;

import android.text.TextUtils;

import com.example.huanghuidong.weatherreport.db.WeatherReportDB;
import com.example.huanghuidong.weatherreport.model.City;
import com.example.huanghuidong.weatherreport.model.County;
import com.example.huanghuidong.weatherreport.model.Province;


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

    public synchronized static boolean handleCitiesResponse(WeatherReportDB weatherReportDB,String response, int provincdId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if ((allCities!=null) && (allCities.length>0)){
                for (String c : allCities){
                    String[] array=c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provincdId);
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
                }
                return true;
            }
        }
        return false;
    }
}
