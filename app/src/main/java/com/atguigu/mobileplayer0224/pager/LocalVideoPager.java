package com.atguigu.mobileplayer0224.pager;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.fragment.BaseFragment;

/**
 * 作者：杨光福 on 2017/5/19 11:47
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class LocalVideoPager extends BaseFragment {
    private TextView textView;

    //重写视图
    @Override
    public View initView() {
        Log.e("TAG","LocalVideoPager-initView");
        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","LocalVideoPager-initData");
        textView.setText("本地视频的内容");
    }
}
