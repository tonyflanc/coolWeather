package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据，服务器返回的数据都是  "代号|城市，代号|城市"这样的类型。
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
       public synchronized static boolean handleProvincesResponse
       (CoolWeatherDB coolWeatherDB,String response){
          if(!TextUtils.isEmpty(response)){
        	  /**
        	   * split()的意思是按照指定的字符拆分字符串，这里指定的字符是","
        	   */
           String[] allProvinces=response.split(",");
           if(allProvinces!=null&&allProvinces.length>0){
        	   for(String p:allProvinces){
        		   String[] array=p.split("\\|");// "\\"是转义字符，实际上意思是指定按"|"拆分字符
        		   Province province=new Province();
        		   province.setProvinceCode(array[0]);
        		   province.setProvinceName(array[1]);
        		   //讲解析出来的数据存到province表
        		   coolWeatherDB.saveProvince(province);
        	   }
        	   return true;
            }
        	          	  
          }
    	  return false;
      }
       
       /**
        * 解析和处理服务器返回的市级数据
        */
       public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB
    		   ,String response,int provinceId){
           if(!TextUtils.isEmpty(response)){
        	   String[] allCities=response.split(",");
             if(allCities!=null&&allCities.length>0){
            	 for(String c:allCities){
            		 String[] array=c.split("\\|");
            		 City city=new City();
            		 city.setCityCode(array[0]);
            		 city.setCityName(array[1]);
            		 city.setProvinceId(provinceId);
            		 coolWeatherDB.saveCity(city);
            	 }
            	 return true;
             }
             
           }
    	   return false;
       }
       /**
        * 解析和处理服务器返回的县级数据
        */
       public static boolean handleCountiesResponse(CoolWeatherDB 
    		   coolWeatherDB,String response,int cityId){
	       Log.e("alert", "解析和处理服务器返回的县级数据");
               if(!TextUtils.isEmpty(response)){
               String[] allCounties=response.split(",");
            	   if(allCounties!=null&&allCounties.length>0){
            		   for(String c:allCounties){
            			   String[] array=c.split("\\|");
            			   County county=new County();
            			   county.setCountyCode(array[0]);
            			   county.setCountyName(array[1]);
            			   county.setCityId(cityId);
            			   //将解析出来的数据存储到County表
            			   coolWeatherDB.saveCounty(county);
            			   Log.e("alert","将数据存到County表");
            		   }
            		   
            		   return true;
            	   }   
               }
    	   return false;
       }
       
       /**
        * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
        * weatherCode代表城市代码？
        * weatherDesp是天气描述信息
        */
       public static void handleWeatherResponse(Context context,String response){
    	   try{
    		   JSONObject jsonObject=new JSONObject(response);
    		   JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
    		   
    		   String cityName=weatherInfo.getString("city");
    		   String weatherCode=weatherInfo.getString("cityId");
    		   String temp1=weatherInfo.getString("temp1");
    		   String temp2=weatherInfo.getString("temp2");
    		   String weatherDesp=weatherInfo.getString("weather");
    		   String publishTime=weatherInfo.getString("ptime");
    		   saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);	   
    	   }catch(JSONException e){
    		   e.printStackTrace();
    	   }
       }
/**
 * 
 *将服务器返回的所有天气信息存储到SharedPreferences
 */
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.
				getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_time", sdf.format(new Date()));
		editor.commit();
	}




}
