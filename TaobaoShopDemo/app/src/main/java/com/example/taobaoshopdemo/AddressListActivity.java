package com.example.taobaoshopdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.taobaoshopdemo.adapter.AddressAdapter;
import com.example.taobaoshopdemo.adapter.decoration.DividerItemDecoration;
import com.example.taobaoshopdemo.bean.Address;
import com.example.taobaoshopdemo.http.OkHttpHelper;
import com.example.taobaoshopdemo.http.SpotsCallBack;
import com.example.taobaoshopdemo.msg.BaseRespMsg;
import com.example.taobaoshopdemo.widget.MyToolBar;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class AddressListActivity extends AppCompatActivity {

    private MyToolBar mToolBar;
    private RecyclerView mRecyclerview;

    private AddressAdapter mAdapter;

    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        initToolbar();
        initAddress();
    }

    private void initToolbar(){
        mToolBar = findViewById(R.id.addr_list_toolbar);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddActivity();
            }
        });
    }

    private void toAddActivity() {

        Intent intent = new Intent(this,AddressAddActivity.class);
        startActivityForResult(intent,Contents.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initAddress();
    }

    private void initAddress(){


        Map<String,Object> params = new HashMap<>(1);
        params.put("user_id",WilsonApplication.getInstance().getUser().getId());

        mHttpHelper.get(Contents.API.ADDRESS_LIST, params, new SpotsCallBack<List<Address>>(this) {


            @Override
            public void onSuccess(Response response, List<Address> addresses) {
                showAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showAddress(List<Address> addresses) {
        //使用Collections中的sort()对addresses进行排序
        //设为默认地址的必须排在第一行
        Collections.sort(addresses);
        if(mAdapter ==null) {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressLisneter() {
                @Override
                public void setDefault(Address address) {

                    updateAddress(address);

                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        }
        else{
            mAdapter.refreshData(addresses);
            mRecyclerview.setAdapter(mAdapter);
        }
    }

    public void updateAddress(Address address){

        Map<String,Object> params = new HashMap<>(1);
        params.put("id",address.getId());
        params.put("consignee",address.getConsignee());
        params.put("phone",address.getPhone());
        params.put("addr",address.getAddr());
        params.put("zip_code",address.getZipCode());
        params.put("is_default",address.getIsDefault());

        mHttpHelper.post(Contents.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){

                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}