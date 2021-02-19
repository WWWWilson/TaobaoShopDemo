package com.example.taobaoshopdemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.bean.Wares;
import com.example.taobaoshopdemo.utils.CartProvider;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class HWAdapter extends SimpleAdapter<Wares> {

    CartProvider provider ;

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);

        provider = new CartProvider(context);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.temp_hot_drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.temp_hot_tv_title).setText(wares.getName());
        viewHolder.getTextView(R.id.temp_hot_tv_price).setText("￥ "+wares.getPrice());

        Button button =viewHolder.getButton(R.id.temp_hot_btn_add);
        if(button !=null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    provider.put(wares);

                    Toast.makeText(context, "已添加到购物车", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void  resetLayout(int layoutId){

        this.layoutResId  = layoutId;

        notifyItemRangeChanged(0,getDatas().size());

    }
}
