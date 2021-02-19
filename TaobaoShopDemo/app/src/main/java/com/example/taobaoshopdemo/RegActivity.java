package com.example.taobaoshopdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taobaoshopdemo.widget.ClearEditText;
import com.example.taobaoshopdemo.widget.MyToolBar;
import com.mob.MobSDK;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class RegActivity extends AppCompatActivity {
    private static final String TAG = "RegActivity";

    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    private MyToolBar mToolBar;
    private TextView mTvCountry;
    private TextView mTvCountryCode;
    private ClearEditText mEtPhone;
    private ClearEditText mEtPwd;

    private  SMSEvenHandler evenHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();
        initToolBar();

        MobSDK.submitPolicyGrantResult(true, null);

        evenHandler = new SMSEvenHandler();
        SMSSDK.registerEventHandler(evenHandler);

        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {
            mTvCountryCode.setText("+" + country[1]);
            mTvCountry.setText(country[0]);
        }
    }

    private void initView() {
        mToolBar = findViewById(R.id.reg_toolbar);
        mTvCountry = findViewById(R.id.reg_tv_country);
        mTvCountryCode = findViewById(R.id.reg_tv_country_code);
        mEtPhone = findViewById(R.id.reg_etxt_phone);
        mEtPwd = findViewById(R.id.reg_etxt_psw);
    }

    class SMSEvenHandler extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                            onCountryListGot((ArrayList<HashMap<String,Object>>) data);

                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面

                            afterVerificationCodeRequested((Boolean) data);

                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        }
                    } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(RegActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }
                }
            });
            SMSSDK.registerEventHandler(evenHandler);
        }

    }

    private void initToolBar() {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });
    }
    private void getCode() {
        String phone = mEtPhone.getText().toString().trim().replace("\\s*", "");
        String code = mTvCountryCode.getText().toString().trim();
        String psw = mEtPwd.getText().toString().trim();

        checkPhoneNum(phone,code);

        SMSSDK.getVerificationCode(code,phone);
    }

    private void checkPhoneNum(String phone,String code){
        if (code.startsWith("+")){
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (code == "86") {
            if (phone.length() != 11) {
                Toast.makeText(this, "号码长度不对", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            Toast.makeText(this, "你输入的手机格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void onCountryListGot(ArrayList<HashMap<String,Object>> countries){
        //解析国家列表
        for (HashMap<String,Object> country : countries){
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }
        }
    }

    //请求验证码后，跳转到验证码填写界面
    private void afterVerificationCodeRequested(boolean smart){

        String phone = mEtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTvCountryCode.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent = new Intent(this,RegSecondActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("pwd",pwd);
        intent.putExtra("countryCode",code);

        startActivity(intent);
    }

    private String[] getCurrentCountry(){
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if (country == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country;
    }

    private String getMCC(){
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }
        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSSDK.unregisterEventHandler(evenHandler);
    }


}