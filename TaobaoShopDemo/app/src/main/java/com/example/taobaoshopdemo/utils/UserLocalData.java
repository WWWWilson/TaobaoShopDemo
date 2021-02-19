package com.example.taobaoshopdemo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.taobaoshopdemo.Contents;
import com.example.taobaoshopdemo.bean.User;

public class UserLocalData {

    public static void putUser(Context context, User user){

        //把传进来的user对象转化成json格式
        String user_json = JSONUtil.toJSON(user);
        //把user_json保存到SharedPreferencesUtils
        PreferencesUtils.putString(context, Contents.USER_JSON,user_json);
    }

    public static void putToken(Context context,String token){

        //把token保存到SharedPreferencesUtils
        PreferencesUtils.putString(context, Contents.TOKEN,token);
    }

    public static User getUser(Context context){

        //从SharedPreferencesUtils中取出user_json
        String user_json= PreferencesUtils.getString(context,Contents.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){
            //返回从JSONUTil类中GSON通过user_json解析的User对象
            return JSONUtil.fromJson(user_json,User.class);
        }
        return  null;
    }

    public static  String getToken(Context context){
        //从SharedPreferencesUtils中取出token
        return  PreferencesUtils.getString(context,Contents.TOKEN);

    }

    //清除登录人的信息
    public static void clearUser(Context context){
        //删除SharedPreferencesUtils中的User所解析成String的数据
        PreferencesUtils.putString(context, Contents.USER_JSON,"");

    }
    //清除token
    public static void clearToken(Context context){
        //删除SharedPreferencesUtils中token的数据
        PreferencesUtils.putString(context, Contents.TOKEN,"");
    }
}
