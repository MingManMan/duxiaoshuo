package com.beibei.mingmanman.readxiaoshuo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beibei.mingmanman.readxiaoshuo.model.Xiaoshuo_info;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.AlertDialog;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by mingmanman on 2016/10/13.
 */

public class SearchlistAdapter extends RecyclerView.Adapter<SearchlistAdapter.ViewHolder>  {
    private List<Searchinfo> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private SearchlistAdapter.OnItemClickListener onItemClickListener;
    private ItemTouchHelper mItemTouchHelper;
    private static SQLiteDatabase db;
    private XiaoshuoDatabaseHelper dbHelper;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick( View view,int position);
    }
    public void setOnItemClickListener(SearchlistAdapter.OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }

    public SearchlistAdapter(Context context, List<Searchinfo> datas){
        this. mContext=context;
        this. mDatas=datas;
        inflater=LayoutInflater. from(mContext);
        Glide.with(mContext);// 绑定Context
    }
    @Override
    public SearchlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_item ,parent, false);
        SearchlistAdapter.ViewHolder holder= new SearchlistAdapter.ViewHolder(view);
        return holder;
    }

    public void move(int fromPosition, int toPosition) {
        Searchinfo prev = mDatas.remove(fromPosition);
        mDatas.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onBindViewHolder(final SearchlistAdapter.ViewHolder holder, final int position) {
        holder.tv.setText( Html.fromHtml(mDatas.get(position).getXiaoshuo_name()));
        holder.jianjie.setText(Html.fromHtml(mDatas.get(position).xiaoshuo_jianjie));
        holder.info.setText(mDatas.get(position).xiaoshuo_info);//@mipmap/noimg
        Glide.with(mContext).load(mDatas.get(position).img_url).placeholder(R.mipmap.noimg).error(R.mipmap.noimg).into(holder.img);
        holder.tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        dbHelper = new XiaoshuoDatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        Xiaoshuo_info xs_info=cupboard().withDatabase(db).query(Xiaoshuo_info.class).withSelection("xiaoshuo_ming = ? and list_order>0",mDatas.get(position).xiaoshuo_name).get();
        //判断小说是否存在，如果不存在添加按钮动作，否则不添加按钮动作
        if(xs_info==null) {
                holder.add_shujia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Xiaoshuo_info xiaoshuo_info = new Xiaoshuo_info();
                    xiaoshuo_info.xiaoshuo_ming=mDatas.get(position).xiaoshuo_name;
                    xiaoshuo_info.zhandian_ming= mDatas.get(position).zhandian_ming;
                    xiaoshuo_info.xiaoshuo_base_url=mDatas.get(position).xiaoshuo_base_url;
                    xiaoshuo_info.xiaoshuo_mulu_url=mDatas.get(position).xiaoshuo_mulu_url;
                    xiaoshuo_info.list_order=1;
                    xiaoshuo_info.isDefaultZhandian=true;
                    cupboard().withDatabase(db).put(xiaoshuo_info);
                        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                        builder.setTitle("添加小说");
                        builder.setMessage("小说已经成功添加到书架!");
                        //builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", null);
                        builder.show();
                        Log.i("testcrab", "添加小说成功");
                    holder.add_shujia.setText("已添加到书架");
                    mDatas.get(position).yitianjia="y";
                }
            });
        }else{
            holder.add_shujia.setText("已添加到书架");
        }
        if(mDatas.get(position).yitianjia=="y"){
            holder.add_shujia.setEnabled(false);
        }

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
    public void addNewItem(int position) {
        if(mDatas == null) {
            mDatas = new ArrayList<>();
        }
        Searchinfo aaa=new Searchinfo("url","xiaoshuo_ming","xiaoshuo_info");

        mDatas.add(position, aaa);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDatas.size());
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

        TextView tv,jianjie,info,add_shujia,shidu_kankan;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.xiaoshuo_ming);
            jianjie = (TextView) itemView.findViewById(R.id.xiaoshuo_jianjie);
            info=(TextView)itemView.findViewById(R.id.xiaoshuo_info);
            add_shujia=(TextView)itemView.findViewById(R.id.add_shujia);
            //shidu_kankan=(TextView)itemView.findViewById(R.id.shidu_kankan);
            img=(ImageView)itemView.findViewById(R.id.xiaoshuo_img);
        }
    }

}
