package com.example.huanghuidong.weatherreport.model;

/**
 * Created by huanghuidong on 2016/1/31.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }
}