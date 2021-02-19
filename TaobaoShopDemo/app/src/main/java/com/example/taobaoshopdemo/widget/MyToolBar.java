package com.example.taobaoshopdemo.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;

import com.example.taobaoshopdemo.R;

public class MyToolBar extends Toolbar {

    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;

    public MyToolBar(@NonNull Context context) {
        this(context,null);
    }

    public MyToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    @SuppressLint("RestrictedApi")
    public MyToolBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        /**
         * 相对于布局方向设置此工具栏的内容插入。
         * 内容插入影响工具栏内容的有效区域，
         * 而不是导航按钮和菜单。
         * 插入定义了这些组件的最小边距，
         * 并可用于沿已知网格线有效地对齐工具栏内容。
         * */
        setContentInsetsRelative(10,10);

        if(attrs !=null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MyToolBar, defStyleAttr, 0);


             final Drawable rightIcon = a.getDrawable(R.styleable.MyToolBar_rightButtonIcon);
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
            }

            //定义是否显示SearchView就是有搜索功能的ToolBar
            boolean isShowSearchView = a.getBoolean(R.styleable.MyToolBar_isShowSearchView,false);

            if(isShowSearchView){

                showSearchView();
                hideTitleView();

            }

            CharSequence rightButtonText = a.getText(R.styleable.MyToolBar_rightButtonText);
            if(rightButtonText !=null){
                setRightButtonText(rightButtonText);
            }

            a.recycle();
        }
    }

    private void initView() {

        if(mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

            mTextTitle = mView.findViewById(R.id.toolbar_title);
            mSearchView = mView.findViewById(R.id.toolbar_search_view);
            mRightButton = mView.findViewById(R.id.toolbar_btn_right);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon){

        if(mRightButton !=null){

            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public void  setRightButtonIcon(int icon){

        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public  void setRightButtonOnClickListener(OnClickListener li){

        mRightButton.setOnClickListener(li);
    }

    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }

    public Button getRightButton(){

        return this.mRightButton;
    }

    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if(mTextTitle !=null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public  void showSearchView(){

        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);

    }

    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }
}
