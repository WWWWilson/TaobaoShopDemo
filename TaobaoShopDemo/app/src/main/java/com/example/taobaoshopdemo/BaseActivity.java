package com.example.taobaoshopdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {


//    public void startActivity(Intent intent, boolean isNeedLogin) {
//
//        if (isNeedLogin) {
//
//            User user = CniaoApplication.getInstance().getUser();
//            if (user != null) {
//                super.startActivity(intent);
//            } else {
//
//                CniaoApplication.getInstance().putIntent(intent);
//                Intent loginIntent = new Intent(this
//                        , LoginActivity.class);
//                super.startActivity(intent);
//
//            }
//
//        } else {
//            super.startActivity(intent);
//        }
//
//    }
}