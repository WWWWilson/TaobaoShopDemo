package com.example.taobaoshopdemo.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class WaresAdapter extends SimpleAdapter<Wares>{

    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }
    @Override
    protected void convert(BaseViewHolder viewHoder, Wares item) {

        viewHoder.getTextView(R.id.temp_grid_tv_title).setText(item.getName());
        viewHoder.getTextView(R.id.temp_grid_tv_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.temp_grid_drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }

}
