package com.example.taobaoshopdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.taobaoshopdemo.Contents;
import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.WareDetailActivity;
import com.example.taobaoshopdemo.adapter.BaseAdapter;
import com.example.taobaoshopdemo.adapter.CategoryAdapter;
import com.example.taobaoshopdemo.adapter.WaresAdapter;
import com.example.taobaoshopdemo.adapter.decoration.DividerItemDecoration;
import com.example.taobaoshopdemo.bean.Banner;
import com.example.taobaoshopdemo.bean.Category;
import com.example.taobaoshopdemo.bean.Page;
import com.example.taobaoshopdemo.bean.Wares;
import com.example.taobaoshopdemo.http.OkHttpHelper;
import com.example.taobaoshopdemo.http.SimpleCallBack;
import com.example.taobaoshopdemo.http.SpotsCallBack;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import okhttp3.Response;

public class CategoryFragment extends BaseFragment{

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerviewWares;

    private SmartRefreshLayout mRefreshLaout;

    private SliderLayout mSliderLayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdatper;


    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private long category_id=0;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category,container,false);
        initView(view);
        return view;
    }
    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.category_frag_rv_category);
        mRecyclerviewWares = view.findViewById(R.id.category_frag_rv_wares);
        mRefreshLaout = view.findViewById(R.id.category_frag_refresh_layout);
        mSliderLayout = view.findViewById(R.id.category_frag_slider);
    }

    @Override
    public void init() {
        requestCategoryData();
        requestBannerData();

        initRefreshLayout();
    }

    private  void initRefreshLayout(){
        mRefreshLaout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
            }
        });

        mRefreshLaout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(currPage <=totalPage)
                    loadMoreData();
                else {
//                    Toast.makeText()
                    mRefreshLaout.finishLoadMore();
                }
            }
        });
    }

    private  void refreshData(){

        currPage =1;

        state=STATE_REFREH;
        requestWares(category_id);
    }
    private void loadMoreData(){

        currPage = ++currPage;
        state = STATE_MORE;
        requestWares(category_id);
    }

    private  void requestCategoryData(){

        mHttpHelper.get(Contents.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getActivity()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {

                showCategoryData(categories);

                if(categories !=null && categories.size()>0)
                    category_id = categories.get(0).getId();
                requestWares(category_id);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private  void showCategoryData(List<Category> categories){


        mCategoryAdapter = new CategoryAdapter(getActivity(),categories);

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Category category = mCategoryAdapter.getItem(position);

                category_id = category.getId();
                currPage=1;
                state=STATE_NORMAL;

                requestWares(category_id);


            }
        });

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));

    }

    private void requestBannerData() {

        String url = Contents.API.BANNER+"?type=1";

        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getActivity()){


            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                showSliderViews(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private void showSliderViews(List<Banner> banners){
        if(banners !=null){

            for (Banner banner : banners){


                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);

            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        mSliderLayout.setDuration(3000);

    }

    private void requestWares(long categoryId){

        String url = Contents.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currPage+"&pageSize="+pageSize;

        mHttpHelper.get(url, new SimpleCallBack<Page<Wares>>(getActivity()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {


                currPage = waresPage.getCurrentPage();
                totalPage =waresPage.getTotalPage();

                showWaresData(waresPage.getList());

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private  void showWaresData(List<Wares> wares){

        switch (state){

            case  STATE_NORMAL:

                if(mWaresAdatper ==null) {
                    mWaresAdatper = new WaresAdapter(getActivity(), wares);
                    mWaresAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Wares wares = mWaresAdatper.getItem(position);

                            Intent intent = new Intent(getActivity(), WareDetailActivity.class);

                            intent.putExtra(Contents.WARE,wares);
                            startActivity(intent);
                        }
                    });

                    mRecyclerviewWares.setAdapter(mWaresAdatper);

                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
//                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }
                else{
                    mWaresAdatper.clear();
                    mWaresAdatper.addData(wares);
                }
                break;

            case STATE_REFREH:
                mWaresAdatper.clear();
                mWaresAdatper.addData(wares);

                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mWaresAdatper.addData(mWaresAdatper.getDatas().size(),wares);
                mRecyclerviewWares.scrollToPosition(mWaresAdatper.getDatas().size());
                mRefreshLaout.finishLoadMore();
                break;
        }
    }
}
