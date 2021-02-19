package com.example.taobaoshopdemo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taobaoshopdemo.AddressListActivity;
import com.example.taobaoshopdemo.Contents;
import com.example.taobaoshopdemo.LoginActivity;
import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.WilsonApplication;
import com.example.taobaoshopdemo.bean.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment{

    private CircleImageView mImageHead;
    private TextView mTvUserName;
    private Button mBtnLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        initView(view);
        setClickEvents();
        showHeadImage(WilsonApplication.getInstance().getUser().getLogo_url());
        return view;
    }

    private void initView(View view) {
        mImageHead = view.findViewById(R.id.mine_frag_img_head);
        mTvUserName = view.findViewById(R.id.mine_frag_tv_username);
        mBtnLogout = view.findViewById(R.id.mine_frag_btn_logout);
    }

    @Override
    public void init() {
        showUser();
    }

    private  void showUser(){

        User user = WilsonApplication.getInstance().getUser();
        if(user ==null){
            mBtnLogout.setVisibility(View.GONE);
            mTvUserName.setText(R.string.to_login);

        }
        else{

            mBtnLogout.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(user.getLogo_url()))
                Picasso.with(getActivity()).load(Uri.parse(user.getLogo_url())).into(mImageHead);

            mTvUserName.setText(user.getUsername());
        }
    }

    private void setClickEvents(){
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddressActivity(v);
            }
        });

        mImageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginActivity(v);
            }
        });

        mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginActivity(v);
            }
        });
    }

    private void showHeadImage(String url){
        Picasso.with(getActivity()).load(url).into(mImageHead);
    }

    //如果没有登录跳转到登录页面
    public void toLoginActivity(View view){

        Intent intent = new Intent(getActivity(), LoginActivity.class);

        startActivityForResult(intent, Contents.REQUEST_CODE);
    }

    //退出登录
    public void toAddressActivity(View view){

        startActivity(new Intent(getActivity(), AddressListActivity.class),true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        showUser();
    }
}
