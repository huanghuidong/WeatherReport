package com.example.huanghuidong.weatherreport.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huanghuidong.weatherreport.R;
import com.example.huanghuidong.weatherreport.db.WeatherReportDB;
import com.example.huanghuidong.weatherreport.model.City;
import com.example.huanghuidong.weatherreport.model.County;
import com.example.huanghuidong.weatherreport.model.Province;
import com.example.huanghuidong.weatherreport.util.HttpCallBackListener;
import com.example.huanghuidong.weatherreport.util.HttpUtil;
import com.example.huanghuidong.weatherreport.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuidong on 2016/1/31.
 */
public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE =0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEl_COUNTY=2;

    private ProgressDialog progressDialog;
    private TextView tv_title;
    private ListView lv_area;
    private ArrayAdapter<String> adapter;
    private WeatherReportDB weatherReportDB;
    private List<String> dataList = new ArrayList<String>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);

//        Log.d("onCreate","oncreate");

        lv_area=(ListView)findViewById(R.id.lv_Area);
        tv_title=(TextView)findViewById(R.id.tvTitle);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
        lv_area.setAdapter(adapter);

        weatherReportDB = WeatherReportDB.getInstance(this);

        lv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currentLevel){
                    case LEVEL_PROVINCE:
                        selectedProvince = provinceList.get(position);
                        Log.d("LV Prov Item ", selectedProvince.toString());
                        queryCities();
                        break;
                    case LEVEL_CITY:
                        selectedCity = cityList.get(position);
//                        Log.d("LV City Item ", selectedCity.toString());
                        queryCounties();
                        break;
                }
            }
        });

        queryProvinces();
    }

    private void queryProvinces(){
        provinceList = weatherReportDB.loadProvinces();

//        Log.d("QueryProvinces",provinceList.toString());

        if (provinceList.size()>0){
            dataList.clear();
            for (Province province: provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            lv_area.setSelection(0);
            tv_title.setText("Chinese");
            currentLevel=LEVEL_PROVINCE;
        }else{
            queryFromServer(null,"province");
        }
    }

    private void queryCities(){
        cityList =weatherReportDB.loadCities(selectedProvince.getId());
        //where to set selectedProvince?
//        Log.d("QueryCities",cityList.toString());

        if(cityList.size()>0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            lv_area.setSelection(0);
            tv_title.setText(selectedProvince.getProvinceName());
            currentLevel=LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }

    private void queryCounties(){
        countyList = weatherReportDB.loadCounties(selectedCity.getId());
        Log.d("QueryCounties",countyList.toString());

        if (countyList.size()>0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            lv_area.setSelection(0);
            tv_title.setText(selectedCity.getCityName());
        }else{
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    private void queryFromServer(final String code, final String type){
        String address;

        if(TextUtils.isEmpty(code)){
            address="http://www.weather.com.cn/data/list3/city.xml";
        }else{
            address="http://www.weather.com.cn/data/list3/city"+code+".xml";
        }

        Log.d("queryServer",address);

        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {

            @Override
            public void onFinish(String response) {
//                Log.d("qFromS.onFinish resp=", response);
                boolean result =false;
                if("province".equals(type)){
                    result = Utility.handleProvincesResponse(weatherReportDB,response);
                }else if ("city".equals(type)){
                    result = Utility.handleCitiesResponse(weatherReportDB,response,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountiesResponse(weatherReportDB,response,selectedCity.getId());
                }
                if (result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }

                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"load failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void showProgressDialog(){
        if (progressDialog ==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
      if (currentLevel==LEVEl_COUNTY){
          queryCities();
      }else if(currentLevel==LEVEL_CITY){
          queryProvinces();
      }else{
          finish();
      }
    }
}
