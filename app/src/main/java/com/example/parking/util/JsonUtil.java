package com.example.parking.util;


import android.util.Log;
import com.google.gson.Gson;
import java.lang.reflect.Type;


public class JsonUtil {



    private static final String TAG = "JsonUtil<自定义json工具>";


    public static String toJson(Object obj){

        try {

            if (obj == null){

                return null;
            }else if (obj instanceof String) {

                if ("".equals(obj)) { return null; }
                return (String) obj;
            }
                return new Gson().toJson(obj);
        }catch (Exception e){
            Log.w(TAG,e);
            return null;
        }
    }

    public static <T> T  fromJson( String json, Class<T> clazz ){

        try {

            if ( json != null && !"".equals(json) && json.length()>6){

                return (T)new Gson().fromJson(json, clazz);
            }else{
                return null;
            }
        }catch (Exception e){
            Log.w(TAG,e);
            return null;
        }
    }

    //Type typeOfT = new TypeToken<List<SelectSubPlaceBean>>(){}.getType();
    public static <T> T  fromJson( String json, Type typeOfT ){

        try {

            if ( json != null && "".equals(json) && json.length()>6){

                return (T)new Gson().fromJson(json, typeOfT);
            }else{
                return null;
            }
        }catch (Exception e){
            Log.w(TAG,e);
            return null;
        }
    }

}
