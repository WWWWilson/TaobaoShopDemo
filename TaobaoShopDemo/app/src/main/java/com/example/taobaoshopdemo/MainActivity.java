package com.example.taobaoshopdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.taobaoshopdemo.bean.Tab;
import com.example.taobaoshopdemo.fragment.CartFragment;
import com.example.taobaoshopdemo.fragment.CategoryFragment;
import com.example.taobaoshopdemo.fragment.HomeFragment;
import com.example.taobaoshopdemo.fragment.HotWareFragment;
import com.example.taobaoshopdemo.fragment.MineFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private CartFragment cartFragment;
    private List<Tab> mTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
    }

    private void initTab() {
        Tab tab_home = new Tab(HomeFragment.class,R.string.home,R.drawable.selector_icon_home);
        Tab tab_hot = new Tab(HotWareFragment.class,R.string.hot,R.drawable.selector_icon_hot);
        Tab tab_category = new Tab(CategoryFragment.class,R.string.category,R.drawable.selector_icon_category);
        Tab tab_cart = new Tab(CartFragment.class,R.string.cart,R.drawable.selector_icon_cart);
        Tab tab_mine = new Tab(MineFragment.class,R.string.mine,R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
        mTabHost = this.findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.real_tab_content);

        //循环加载FragmentTabHost
        for (Tab tab : mTabs){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabHost.addTab(tabSpec, tab.getFragment(),null);
        }

        //设置FragmentTabHost的点击事件
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //如果当前TabHost是购物车
                if(tabId==getString(R.string.cart)){

                    refreshData();
                }

            }
        });

        //设置FragmentTabHost中间的分隔器
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //设置默认选择第一个TabHost
        mTabHost.setCurrentTab(0);
    }

    //刷新数据
    private void refreshData(){

        if(cartFragment == null){

            Fragment fragment =  getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if(fragment !=null){
                cartFragment = (CartFragment) fragment;
                cartFragment.refreshData();
            }
        }
        else{
            cartFragment.refreshData();

        }
    }

    //创建指示器
    private View buildIndicator(Tab tab){

        View view =mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = view.findViewById(R.id.tab_indicator_icon_tab);
        TextView text = view.findViewById(R.id.tab_indicator_tv_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return  view;
    }
}