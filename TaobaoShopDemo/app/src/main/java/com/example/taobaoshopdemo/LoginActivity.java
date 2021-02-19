package com.example.taobaoshopdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.taobaoshopdemo.bean.User;
import com.example.taobaoshopdemo.http.OkHttpHelper;
import com.example.taobaoshopdemo.http.SpotsCallBack;
import com.example.taobaoshopdemo.msg.LoginRspMsg;
import com.example.taobaoshopdemo.utils.DESUtil;
import com.example.taobaoshopdemo.widget.ClearEditText;
import com.example.taobaoshopdemo.widget.MyToolBar;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private MyToolBar mToolBar;
    private ClearEditText mEtxtPhone;
    private ClearEditText mEtxtPwd;
    private Button mBtnLogin;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initToolBar();
    }
    private void initView(){
        mEtxtPhone = findViewById(R.id.login_etxt_phone);
        mEtxtPwd = findViewById(R.id.login_etxt_psw);
        mBtnLogin = findViewById(R.id.login_btn_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
    }

    private void initToolBar(){
        mToolBar = findViewById(R.id.login_toolbar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });

    }

    public void login(View view){


        String phone = mEtxtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contents.DES_KEY,pwd));

        okHttpHelper.post(Contents.API.LOGIN, params, new SpotsCallBack<LoginRspMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRspMsg<User> userLoginRspMsg) {


                WilsonApplication application =  WilsonApplication.getInstance();
                application.putUser(userLoginRspMsg.getData(), userLoginRspMsg.getToken());

                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else{

                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();

                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
}