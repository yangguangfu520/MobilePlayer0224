package com.atguigu.mobileplayer0224.pager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.domain.MediaItem;
import com.atguigu.mobileplayer0224.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2017/5/19 11:47
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class LocalVideoPager extends BaseFragment {

    private ListView lv;
    private TextView tv_nodata;
    private ArrayList<MediaItem> mediaItems;

    //重写视图-返回View
    @Override
    public View initView() {
        Log.e("TAG","LocalVideoPager-initView");
        View  view = View.inflate(context, R.layout.fragment_local_video_pager,null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","LocalVideoPager-initData");
        //加载本地所有的视频
        getData();
    }

    /**
     * 得到数据
     */
    private void getData() {
        new Thread(){
            public void run(){
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频在sdcard上的名称
                        MediaStore.Video.Media.DURATION,//视频时长
                        MediaStore.Video.Media.SIZE,//视频文件的大小
                        MediaStore.Video.Media.DATA//视频播放地址
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if(cursor != null ){
                    while (cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        Log.e("TAG","name=="+name+",duration=="+duration+",data==="+data);

                        mediaItems.add(new MediaItem(name,duration,size,data));
                    }

                    cursor.close();
                }
            }
        }.start();
    }
}
