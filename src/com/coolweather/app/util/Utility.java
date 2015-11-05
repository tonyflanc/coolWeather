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
	 * �����ʹ�����������ص�ʡ�����ݣ����������ص����ݶ���  "����|���У�����|����"���������͡�
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
       public synchronized static boolean handleProvincesResponse
       (CoolWeatherDB coolWeatherDB,String response){
          if(!TextUtils.isEmpty(response)){
        	  /**
        	   * split()����˼�ǰ���ָ�����ַ�����ַ���������ָ�����ַ���","
        	   */
           String[] allProvinces=response.split(",");
           if(allProvinces!=null&&allProvinces.length>0){
        	   for(String p:allProvinces){
        		   String[] array=p.split("\\|");// "\\"��ת���ַ���ʵ������˼��ָ����"|"����ַ�
        		   Province province=new Province();
        		   province.setProvinceCode(array[0]);
        		   province.setProvinceName(array[1]);
        		   //���������������ݴ浽province��
        		   coolWeatherDB.saveProvince(province);
        	   }
        	   return true;
            }
        	          	  
          }
    	  return false;
      }
       
       /**
        * �����ʹ�����������ص��м�����
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
        * �����ʹ�����������ص��ؼ�����
        */
       public static boolean handleCountiesResponse(CoolWeatherDB 
    		   coolWeatherDB,String response,int cityId){
	       Log.e("alert", "�����ʹ�����������ص��ؼ�����");
               if(!TextUtils.isEmpty(response)){
               String[] allCounties=response.split(",");
            	   if(allCounties!=null&&allCounties.length>0){
            		   for(String c:allCounties){
            			   String[] array=c.split("\\|");
            			   County county=new County();
            			   county.setCountyCode(array[0]);
            			   county.setCountyName(array[1]);
            			   county.setCityId(cityId);
            			   //���������������ݴ洢��County��
            			   coolWeatherDB.saveCounty(county);
            			   Log.e("alert","�����ݴ浽County��");
            		   }
            		   
            		   return true;
            	   }   
               }
    	   return false;
       }
       
       /**
        * �������������ص�JSON���ݣ����������������ݴ洢������
        * weatherCode������д��룿
        * weatherDesp������������Ϣ
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
 *�����������ص�����������Ϣ�洢��SharedPreferences
 */
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
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
