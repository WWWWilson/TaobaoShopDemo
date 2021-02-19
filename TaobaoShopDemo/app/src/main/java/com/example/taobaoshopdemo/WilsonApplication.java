package com.example.taobaoshopdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.taobaoshopdemo.bean.User;
import com.example.taobaoshopdemo.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;

public class WilsonApplication extends Application {

    private User user;

    private static WilsonApplication mInstance;

    public static WilsonApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initUser();
        Fresco.initialize(this);
    }

    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }

    public User getUser(){
        return user;
    }

    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken(){
        return UserLocalData.getToken(this);
    }

    private Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent(){
        return this.intent;
    }

    //跳转到目标Activity
    public void jumpToTargetActivity(Context context){
        context.startActivity(intent);
        this.intent = null;
    }
}
