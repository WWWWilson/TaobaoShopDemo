package com.example.taobaoshopdemo.http;

import android.content.Context;
import android.media.MediaRouter;

import okhttp3.Request;
import okhttp3.Response;

public abstract class SpotsCallBack<T> extends SimpleCallBack<T> {

    private Context mContext;
    public SpotsCallBack(Context context){
        super(context);
        this.mContext = context;

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onResponse(Response response) {

    }
}
