package com.example.taobaoshopdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaoshopdemo.CreateOrderActivity;
import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.adapter.CartAdapter;
import com.example.taobaoshopdemo.adapter.decoration.DividerItemDecoration;
import com.example.taobaoshopdemo.bean.ShoppingCart;
import com.example.taobaoshopdemo.http.OkHttpHelper;
import com.example.taobaoshopdemo.utils.CartProvider;
import com.example.taobaoshopdemo.widget.MyToolBar;

import java.util.List;

public class CartFragment extends BaseFragment implements View.OnClickListener {

    public static final int ACTION_EDIT=1;
    public static final int ACTION_COMPLETE=2;
    private static final String TAG = "CartFragment";

    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTextTotal;
    private Button mBtnOrder;
    private Button mBtnDelete;
    private MyToolBar mToolbar;

    private View view;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }

    @Override
    public void init() {
        cartProvider = new CartProvider(getActivity());
        initView();
        changeToolbar();
        showData();
    }

    private void initView() {
        mRecyclerView = view.findViewById(R.id.cart_frag_rv);
        mCheckBox = view.findViewById(R.id.cart_frag_check_box);
        mTextTotal = view.findViewById(R.id.cart_frag_tv_total);
        mBtnOrder = view.findViewById(R.id.cart_frag_btn_order);

        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOrder(v);
            }
        });

        mBtnDelete = view.findViewById(R.id.cart_frag_btn_del);

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCart(v);
            }
        });
    }

    public void toOrder(View view){

        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);

        startActivity(intent,true);
    }

    public void deleteCart(View view){

        mAdapter.deleteCart();
    }

    private void showData(){


        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getActivity(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));

    }


    public void refreshData(){

        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();

    }

    public void changeToolbar(){

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDelete.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    private void  hideDelControl(){

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDelete.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        int action = (int) v.getTag();
        if(ACTION_EDIT == action){

            showDelControl();
        }
        else if(ACTION_COMPLETE == action){

            hideDelControl();
        }
    }

}
