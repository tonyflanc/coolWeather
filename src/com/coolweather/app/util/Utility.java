package com.coolweather.app.util;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.text.TextUtils;

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
               if(!TextUtils.isEmpty(response)){
               String[] allCounties=response.split(",");
            	   if(allCounties!=null&&allCounties.length>0){
            		   for(String c:allCounties){
            			   String[] array=c.split("\\|");
            			   County county=new County();
            			   county.setCountyCode(array[0]);
            			   county.setCountyName(array[1]);
            			   county.setCityId(cityId);
            			   
            		   }
            	   }
            	   
               }
    	   return false;
       }
       
}
