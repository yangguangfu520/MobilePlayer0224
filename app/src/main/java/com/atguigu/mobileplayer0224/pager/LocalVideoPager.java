package com.atguigu.mobileplayer0224.pager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.activity.SystemVideoPlayerActivity;
import com.atguigu.mobileplayer0224.adapter.LocalVideoAdapter;
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
    private LocalVideoAdapter adapter;

    //重写视图-返回View
    @Override
    public View initView() {
        Log.e("TAG", "LocalVideoPager-initView");
        View view = View.inflate(context, R.layout.fragment_local_video_pager, null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        //设置item的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //得到点击item对应的对象
//                MediaItem mediaItem = mediaItems.get(position);

//                MediaItem item = adapter.getItem(position);
//                Toast.makeText(context, ""+item.toString(), Toast.LENGTH_SHORT).show();
                //把系统的播放器调起来
//                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse(item.getData()),"video/*");
//                startActivity(intent);

                //传递视频列表过去

                Intent intent = new Intent(context, SystemVideoPlayerActivity.class);

                Bundle bunlder = new Bundle();
                bunlder.putSerializable("videolist",mediaItems);
                intent.putExtra("position",position);
                //放入Bundler
                intent.putExtras(bunlder);
                startActivity(intent);


            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "LocalVideoPager-initData");
        //加载本地所有的视频
        getData();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mediaItems != null && mediaItems.size() > 0) {
                //有数据
                tv_nodata.setVisibility(View.GONE);
                //设置适配器
                adapter = new LocalVideoAdapter(context, mediaItems, true);
                lv.setAdapter(adapter);
            } else {
                //没有数据
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 得到数据
     */
    private void getData() {
        new Thread() {
            public void run() {
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
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        Log.e("TAG", "name==" + name + ",duration==" + duration + ",data===" + data);

                        mediaItems.add(new MediaItem(name, duration, size, data));


                    }

                    cursor.close();
                }

                //使用handler
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
