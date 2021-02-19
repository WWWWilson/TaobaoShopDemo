package com.example.taobaoshopdemo.adapter;

import android.content.Context;

import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.bean.Category;

import java.util.List;

public class CategoryAdapter extends SimpleAdapter<Category>{
    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {

        viewHoder.getTextView(R.id.temp_single_tv).setText(item.getName());

    }
}
