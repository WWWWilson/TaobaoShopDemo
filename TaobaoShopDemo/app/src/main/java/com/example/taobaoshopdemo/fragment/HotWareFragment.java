package com.example.taobaoshopdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaoshopdemo.Contents;
import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.WareDetailActivity;
import com.example.taobaoshopdemo.adapter.BaseAdapter;
import com.example.taobaoshopdemo.adapter.HWAdapter;
import com.example.taobaoshopdemo.adapter.decoration.DividerItemDecoration;
import com.example.taobaoshopdemo.bean.Page;
import com.example.taobaoshopdemo.bean.Wares;
import com.example.taobaoshopdemo.utils.Pager;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

public class HotWareFragment extends BaseFragment implements Pager.OnPageListener<Wares> {
    private HWAdapter mAdatper;

    private RecyclerView mRecyclerView;

    private SmartRefreshLayout mRefreshLaout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRefreshLaout = getActivity().findViewById(R.id.frag_hot_refresh_layout);
        return inflater.inflate(R.layout.fragment_hot,container,false);
    }

    @Override
    public void init() {
        Pager pager = Pager.newBuilder()
                .setUrl(Contents.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLaout)
                .build(getActivity(), new TypeToken<Page<Wares>>() {}.getType());


        pager.request();
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mRecyclerView = getActivity().findViewById(R.id.frag_hot_rv);
        mAdatper = new HWAdapter(getActivity(),datas);

        mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Wares wares = mAdatper.getItem(position);

                Intent intent = new Intent(getActivity(), WareDetailActivity.class);

                intent.putExtra(Contents.WARE,wares);
                startActivity(intent);


            }
        });


        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }


    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdatper.refreshData(datas);

        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {

        mAdatper.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
    }
}
