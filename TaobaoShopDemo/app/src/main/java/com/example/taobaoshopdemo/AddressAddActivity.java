package com.example.taobaoshopdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.taobaoshopdemo.city.XmlParserHandler;
import com.example.taobaoshopdemo.city.modle.CityModel;
import com.example.taobaoshopdemo.city.modle.DistrictModel;
import com.example.taobaoshopdemo.city.modle.ProvinceModel;
import com.example.taobaoshopdemo.http.OkHttpHelper;
import com.example.taobaoshopdemo.http.SpotsCallBack;
import com.example.taobaoshopdemo.msg.BaseRespMsg;
import com.example.taobaoshopdemo.widget.ClearEditText;
import com.example.taobaoshopdemo.widget.MyToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Response;

public class AddressAddActivity extends BaseActivity {

    private OptionsPickerView mCityPickerView;
    private TextView mTvAddress;
    private ClearEditText mEtConsignee;
    private ClearEditText mEtPhone;
    private ClearEditText mEtAddr;
    private LinearLayout mOpenCityPicker;

    private MyToolBar mToolBar;

    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();

    private OkHttpHelper mHttpHelper= OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);

        initView();
        initToolbar();
    }

    private void initView() {
        mTvAddress = findViewById(R.id.addr_add_tv_address);
        mEtConsignee = findViewById(R.id.addr_add_cet_consignee);
        mEtPhone = findViewById(R.id.addr_add_cet_phone);
        mEtAddr = findViewById(R.id.addr_add_et);
        mOpenCityPicker.findViewById(R.id.ll_city_picker);

        setOptionPickerView();
        initProvinceDatas();
        showCityPickerView();
    }

    private void initToolbar(){

        mToolBar = findViewById(R.id.addr_add_toolbar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createAddress();
            }
        });

    }

    //设置地区选择器
    private void setOptionPickerView(){
        mCityPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String address = mProvinces.get(options1).getName()
                        + mCities.get(options1).get(options2) + " "
                        + mDistricts.get(options1).get(options2).get(options3);
                mTvAddress.setText(address);
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {     //自定义各种属性，具体看文档
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {

            }
        }).setOutSideCancelable(false)
                .setCyclic(false,false,false)
                .build();

        mCityPickerView.setPicker(mProvinces,mCities,mDistricts);
        mCityPickerView.setTitleText("选择城市");
        mCityPickerView.show();
    }

    //初始省数据
    private void initProvinceDatas() {
//        这个类提供了一个较低级别的API，
//        允许您打开和读取作为简单字节流与应用程序捆绑在一起的原始文件。
        AssetManager input = getAssets();
        //创建一个解析sml的工厂对象
        SAXParserFactory spf = SAXParserFactory.newInstance();
        //解析sml

        try {

            //一旦获得此类的实例，
            // 就可以从各种输入源解析XML。
            // 这些输入源是InputStreams、文件、url和SAX输入源。
            SAXParser parser = spf.newSAXParser();
            //创建的继承DefaultHandler的类
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(String.valueOf(input),handler);
            input.close();
            //获取解析出来的资源
            mProvinces = handler.getDataList();

        } catch (Throwable e) {
            e.printStackTrace();
        }finally {

        }

        if (mProvinces!=null) {
            for (ProvinceModel p : mProvinces){

                List<CityModel> cities = p.getCityList();
                ArrayList<String> cityStrs = new ArrayList<>(cities.size());

                //把城市名称放入cityStrs
                for (CityModel c : cities){
                    cityStrs.add(c.getName());

                    //地区List
                    ArrayList<ArrayList<String>> dts = new ArrayList<>();

                    List<DistrictModel> districts = c.getDistrictList();
                    ArrayList<String> districtsStrs = new ArrayList<>(districts.size());

                    for (DistrictModel d : districts){

                        //把城市名称放入 districtsStrs
                        districtsStrs.add(d.getName());
                    }
                    dts.add(districtsStrs);

                    mDistricts.add(dts);
                }
                //组装城市数据
                mCities.add(cityStrs);
            }
        }
    }

    //创建地址
    private void createAddress() {

        //获取收件人，电话，地址
        String consignee = mEtConsignee.getText().toString();
        String phone = mEtPhone.getText().toString();
        String address = mTvAddress.getText().toString() + mEtAddr.getText().toString();

        Map<String,Object> params = new HashMap<>(1);
        params.put("user_id",WilsonApplication.getInstance().getUser().getId());
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",address);
        params.put("zip_code","000000");  //传入邮编

        mHttpHelper.post(Contents.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    //打开地区选择器
    public void showCityPickerView(){
        mOpenCityPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCityPickerView.show();
            }
        });
    }
}