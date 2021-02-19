package com.example.taobaoshopdemo.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaoshopdemo.R;
import com.example.taobaoshopdemo.bean.Campaign;
import com.example.taobaoshopdemo.bean.HomeCampaign;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{

    //这两个常量用于getItemViewType()方法中判断template_home_cardview是在左边还是在右边
    private  static int VIEW_TYPE_LEFT=0;
    private  static int VIEW_TYPE_RIGHT=1;

    private LayoutInflater mInflater;

    private List<HomeCampaign> mDatas;

    private Context mContext;


    private  OnCampaignClickListener mListener;

    public HomeCategoryAdapter(Context context,List<HomeCampaign> datas){
        this.mContext = context;
        mDatas = datas;
    }

    public void setOnCampaignClickListener(OnCampaignClickListener listener){

        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {

        mInflater = LayoutInflater.from(parent.getContext());
        if(type == VIEW_TYPE_RIGHT){

            return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,parent,false));
        }
        return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        HomeCampaign homeCampaign = mDatas.get(position);
        viewHolder.textTitle.setText(homeCampaign.getTitle());

        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(viewHolder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(viewHolder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(viewHolder.imageViewSmallBottom);
    }

    @Override
    public int getItemCount() {
        if(mDatas==null || mDatas.size()<=0)
            return 0;

        return mDatas.size();
    }

    //计算是左边视图还是右边视图
    @Override
    public int getItemViewType(int position) {

        if(position % 2==0){
            return  VIEW_TYPE_RIGHT;
        }
        else return VIEW_TYPE_LEFT;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.temp_home_tv_title);
            imageViewBig = itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = itemView.findViewById(R.id.imgview_small_bottom);


            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(mListener !=null){

                anim(v);

            }
        }

        private  void anim(final View v){

            ObjectAnimator animator =  ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    HomeCampaign campaign = mDatas.get(getLayoutPosition());

                    switch (v.getId()){

                        case  R.id.imgview_big:
                            mListener.onClick(v, campaign.getCpOne());
                            break;

                        case  R.id.imgview_small_top:
                            mListener.onClick(v, campaign.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mListener.onClick(v,campaign.getCpThree());
                            break;

                    }
                }
            });
            animator.start();
        }
    }

        public  interface OnCampaignClickListener{

        void onClick(View view, Campaign campaign);
    }
}
