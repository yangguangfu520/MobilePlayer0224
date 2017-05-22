package com.atguigu.mobileplayer0224.app;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * 作者：杨光福 on 2017/5/22 16:53
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class MyApplication extends Application {
    //这个方法，在所有的组件被初始化之前被调用
    @Override
    public void onCreate() {
        super.onCreate();
        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
