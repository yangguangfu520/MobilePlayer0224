package com.atguigu.mobileplayer0224.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.atguigu.mobileplayer0224.domain.NetAudioBean;

import java.util.List;

/**
 * 作者：杨光福 on 2017/5/28 14:37
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class NetAudioFragmentAdapter extends BaseAdapter {
    private final Context context;
    private final List<NetAudioBean.ListBean> datas;

    public NetAudioFragmentAdapter(Context context, List<NetAudioBean.ListBean> datas) {
        this.context = context;
        this.datas =datas;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
