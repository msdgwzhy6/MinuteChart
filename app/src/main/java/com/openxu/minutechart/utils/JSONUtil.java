package com.openxu.minutechart.utils;
import com.google.gson.Gson;

/**
 * Created by openxu on 16/7/28.
 */
public class JSONUtil {
    private static Gson gson=null;

    public static <T> T jsonToBean(String jsonString,Class<T> cls){
        if(jsonString==null || cls == null)return null;
        if(gson==null)gson=new Gson();
        try {
            return gson.fromJson(jsonString, cls);
        }catch (Exception e){
            return null;
        }
    }
}