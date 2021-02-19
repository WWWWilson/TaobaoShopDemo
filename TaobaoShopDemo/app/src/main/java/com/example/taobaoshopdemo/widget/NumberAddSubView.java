package com.example.taobaoshopdemo.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.TintTypedArray;

import com.example.taobaoshopdemo.R;

public class NumberAddSubView extends LinearLayout implements View.OnClickListener {
    public static final String TAG="NumberAddSubView";
    public static final int DEFUALT_MAX=1000;

    private TextView mTVNum;
    private Button mBtnAdd;
    private Button mBtnSub;

    private OnButtonClickListener onButtonClickListener;

    private LayoutInflater mInflater;

    private  int value;
    private int minValue;
    private int maxValue=DEFUALT_MAX;

    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        initView();

        if(attrs !=null){

             final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.NumberAddSubView, defStyleAttr, 0);


            int val =  a.getInt(R.styleable.NumberAddSubView_value,0);
            setValue(val);

            int maxVal = a.getInt(R.styleable.NumberAddSubView_maxValue,0);
            if(maxVal!=0)
                setMaxValue(maxVal);

            int minVal = a.getInt(R.styleable.NumberAddSubView_minValue,0);
            setMinValue(minVal);

            Drawable etBackground = a.getDrawable(R.styleable.NumberAddSubView_editBackground);
            if(etBackground!=null)
                setEditTextBackground(etBackground);


            Drawable buttonAddBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonAddBackgroud);
            if(buttonAddBackground!=null)
                setButtonAddBackgroud(buttonAddBackground);

            Drawable buttonSubBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonSubBackgroud);
            if(buttonSubBackground!=null)
                setButtonSubBackgroud(buttonSubBackground);




            a.recycle();
        }
    }

    private void initView(){



        View view = mInflater.inflate(R.layout.widet_num_add_sub,this,true);

        mTVNum = view.findViewById(R.id.tv_num);
        mTVNum.setInputType(InputType.TYPE_NULL);
        mTVNum.setKeyListener(null);



        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnSub = view.findViewById(R.id.btn_sub);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add){

            numAdd();

            if(onButtonClickListener !=null){
                onButtonClickListener.onButtonAddClick(v,this.value);
            }
        }
        else if(v.getId()==R.id.btn_sub){
            numSub();
            if(onButtonClickListener !=null){
                onButtonClickListener.onButtonSubClick(v,this.value);
            }

        }
    }
    private void numAdd(){


        getValue();

        if(this.value<=maxValue)
            this.value=+this.value+1;

        mTVNum.setText(value+"");
    }

    private void numSub(){


        getValue();

        if(this.value>minValue)
            this.value=this.value-1;

        mTVNum.setText(value+"");
    }

    public int getValue(){

        String value = mTVNum.getText().toString();

        if(value !=null && !"".equals(value))
            this.value = Integer.parseInt(value);

        return this.value;
    }

    public void setValue(int value) {
        mTVNum.setText(value+"");
        this.value = value;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }


    public void setEditTextBackground(Drawable drawable){

        mTVNum.setBackgroundDrawable(drawable);

    }


    public void setEditTextBackground(int drawableId){

        setEditTextBackground(getResources().getDrawable(drawableId));

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud){
        this.mBtnAdd.setBackground(backgroud);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud){
        this.mBtnSub.setBackground(backgroud);
    }


    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface  OnButtonClickListener{

        public void onButtonAddClick(View view, int value);
        public void onButtonSubClick(View view, int value);

    }

}
