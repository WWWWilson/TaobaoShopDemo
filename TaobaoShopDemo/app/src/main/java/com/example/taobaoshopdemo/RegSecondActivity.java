package com.example.taobaoshopdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taobaoshopdemo.bean.User;
import com.example.taobaoshopdemo.http.OkHttpHelper;
import com.example.taobaoshopdemo.http.SpotsCallBack;
import com.example.taobaoshopdemo.msg.LoginRspMsg;
import com.example.taobaoshopdemo.utils.CountTimerView;
import com.example.taobaoshopdemo.utils.DESUtil;
import com.example.taobaoshopdemo.widget.ClearEditText;
import com.example.taobaoshopdemo.widget.MyToolBar;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

public class RegSecondActivity extends AppCompatActivity {

    private MyToolBar mToolBar;
    private TextView mTvTip;
    private Button mBtnResend;
    private ClearEditText mEtCode;

    private String phone;
    private String pwd;
    private String countryCode;

    private SpotsDialog.Builder dialog;

    private CountTimerView countTimerView;

    private OkHttpHelper okHttpHelper;
    private SMSEventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);

        initView();
        initToolBar();

        MobSDK.submitPolicyGrantResult(true, null);

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formattedPhone = "+" + countryCode + " " + splitPhoneNum(phone);

        String text = getString(R.string.smssdk_send_mobile_detail) + formattedPhone;
        mTvTip.setText(Html.fromHtml(text));

        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        eventHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(eventHandler);
    }

    //分割电话号码
    private String splitPhoneNum(String phone){
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i=4,len = builder.length();i<len;i += 5){
            builder.insert(i,' ');
        }
        builder.reverse();
        return builder.toString();
    }

    private void initView() {
        mTvTip = findViewById(R.id.reg_second_tv_Tip);
        mEtCode = findViewById(R.id.reg_second_etxt_code);
        mBtnResend = findViewById(R.id.reg_second_btn_resend);

        mBtnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSendCode(v);
            }
        });
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.reg_second_toolbar);

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });
    }

    //提交验证码
    private void submitCode(){
        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)){
            Toast.makeText(this, R.string.smssdk_write_identify_code, Toast.LENGTH_SHORT).show();
            return;
        }

        SMSSDK.submitVerificationCode(countryCode,phone,vCode);
    }

    public void reSendCode(View view){
        SMSSDK.getVerificationCode("+" + countryCode,phone);
        countTimerView = new CountTimerView(mBtnResend,R.string.smssdk_resend_identify_code);
        countTimerView.start();
        Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
    }

    //提交注册
    private void doReg(){

        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contents.DES_KEY, pwd));

        okHttpHelper.post(Contents.API.REG, params, new SpotsCallBack<LoginRspMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRspMsg<User> userLoginRespMsg) {

                if(userLoginRespMsg.getStatus()==LoginRspMsg.STATUS_ERROR){
                    Toast.makeText(RegSecondActivity.this, "注册失败:"+userLoginRespMsg.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                WilsonApplication application =WilsonApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegSecondActivity.this,MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    class SMSEventHandler extends EventHandler {

        @Override
        public void afterEvent(int event, int result, Object data) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.build();
                            //提交验证码成功
                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            //获取验证码成功
                        }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                            //返回支持发送验证码的国家列表
                        }
                    }else{
                        try {
                            ((Throwable)data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (TextUtils.isEmpty(des)) {
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}