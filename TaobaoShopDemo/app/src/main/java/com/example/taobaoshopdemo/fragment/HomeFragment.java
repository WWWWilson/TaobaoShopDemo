package com.example.taobaoshopdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.taobaoshopdemo.Contents;
import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.WareListActivity;
import com.example.taobaoshopdemo.adapter.HomeCategoryAdapter;
import com.example.taobaoshopdemo.adapter.decoration.CartViewtemDecoration;
import com.example.taobaoshopdemo.bean.Banner;
import com.example.taobaoshopdemo.bean.Campaign;
import com.example.taobaoshopdemo.bean.HomeCampaign;
import com.example.taobaoshopdemo.http.BaseCallback;
import com.example.taobaoshopdemo.http.OkHttpHelper;
import com.example.taobaoshopdemo.http.SpotsCallBack;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends BaseFragment {

    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;

    private HomeCategoryAdapter mAdapter;

    private static final String TAG = "HomeFragment";

    private Gson mGson = new Gson();

    private List<Banner> mBanner;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.home_frag_rv);
        initRecyclerView();
        mSliderLayout = view.findViewById(R.id.home_frag_slider);

       return view;
    }

    @Override
    public void init() {
        requestImages();

    }

    private void requestImages() {

        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";


        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getActivity()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                mBanner = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initRecyclerView() {

        httpHelper.get(Contents.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
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
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {

                initData(homeCampaigns);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private  void initData(List<HomeCampaign> homeCampaigns){


        mAdapter = new HomeCategoryAdapter(getActivity(),homeCampaigns);

        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {


                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contents.COMPAINGAIN_ID,campaign.getId());

                startActivity(intent);

            }
        });

        mRecyclerView.addItemDecoration(new CartViewtemDecoration());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSlider(){

        if(mBanner !=null){

            for (Banner banner : mBanner){

                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);

            }
        }
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSliderLayout.stopAutoCycle();
    }
}
