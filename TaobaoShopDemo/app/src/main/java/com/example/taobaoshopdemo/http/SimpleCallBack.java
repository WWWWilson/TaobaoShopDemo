package com.example.taobaoshopdemo.http;

import android.content.Context;
import android.content.Intent;

import com.example.taobaoshopdemo.LoginActivity;
import com.example.taobaoshopdemo.WilsonApplication;

import okhttp3.Request;
import okhttp3.Response;

public abstract class SimpleCallBack<T> extends BaseCallback<T> {
    protected Context mContext;

    public SimpleCallBack(Context context){
        mContext = context;
    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        WilsonApplication.getInstance().clearUser();

    }
}
