package com.beibei.mingmanman.readxiaoshuo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

import static android.view.View.VISIBLE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Xiaoshuo_info> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private MyAdapter.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick( View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }

    public MyAdapter(Context context, List<Xiaoshuo_info> datas){
        this. mContext=context;
        this. mDatas=datas;
        inflater=LayoutInflater. from(mContext);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_rv_item,parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    public void move(int fromPosition, int toPosition) {
        Xiaoshuo_info prev = mDatas.remove(fromPosition);
        mDatas.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void onBindViewHolder(final  ViewHolder holder, int position) {
        holder.tv.setText( mDatas.get(position).xiaoshuo_ming);
        holder.xin_zhangjie_xian_shi.setVisibility(VISIBLE);
        holder.xin_zhangjie_shu.setText("9");
        if ( onItemClickListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                    //表示此事件已经消费，不会触发单击事件
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void deleteItem(int position) {
        if(mDatas == null || mDatas.isEmpty()) {
            return;
        }
        mDatas.remove( position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv,xin_zhangjie_shu;
        FrameLayout xin_zhangjie_xian_shi;
         public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.xiaoshuo_ming);
            xin_zhangjie_shu = (TextView) itemView.findViewById(R.id.xin_zhangjie_shu);
            xin_zhangjie_xian_shi=(FrameLayout)itemView.findViewById(R.id.xin_zhangjie_xian_shi);
        }
    }
}
