package com.atguigu.recyclerviewdemo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：杨光福 on 2017/6/1 11:27
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private final Context context;
    private final ArrayList<String> datas;


    public MyAdapter(Context context, ArrayList<String> datas) {
        this.context = context;
        this.datas = datas;
    }


    /**
     * 得到总共有多少条数据
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * 相当于getView方法中的创建ViewHolder,包含创建布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(context, R.layout.item, null));
    }

    /**
     * 相当于getView方法中的数据和视图绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //1.根据位置得到对应的数据
        String data = datas.get(position);
        //2.绑定数据
        holder.tvText.setText(data);

    }

    public void addData(int position, String data) {
        datas.add(position,data);
        notifyItemInserted(position);//更新数据
    }

    public void remove(int position) {
        datas.remove(position);
        //刷新
        notifyItemRemoved(position);
    }


    //必须的
    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_text)
        TextView tvText;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            //设置item的点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, ""+datas.get(getLayoutPosition()), Toast.LENGTH_SHORT).show();
                    if(listener != null){
                        listener.onItemClick(getLayoutPosition());
                    }
                }
            });
        }
    }

    private OnItemClickListener listener;
    /**
     * 点击某条的监听器
     */
    public interface OnItemClickListener{
        public void onItemClick(int position);
    }

    /**
     * 设置点击item的监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
