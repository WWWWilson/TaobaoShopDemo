package com.example.taobaoshopdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taobaoshopdemo.LoginActivity;
import com.example.taobaoshopdemo.WilsonApplication;
import com.example.taobaoshopdemo.bean.User;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = createView(inflater,container,savedInstanceState);

        initToolBar();

        init();

        return view;
    }

    public void  initToolBar(){

    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();

    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = WilsonApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                WilsonApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }
}
