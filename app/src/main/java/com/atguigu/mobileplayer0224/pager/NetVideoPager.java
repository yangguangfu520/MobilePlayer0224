package com.atguigu.mobileplayer0224.pager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.activity.SystemVideoPlayerActivity;
import com.atguigu.mobileplayer0224.adapter.NetVideoAdapter;
import com.atguigu.mobileplayer0224.domain.MediaItem;
import com.atguigu.mobileplayer0224.domain.MoveInfo;
import com.atguigu.mobileplayer0224.fragment.BaseFragment;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：杨光福 on 2017/5/19 11:47
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class NetVideoPager extends BaseFragment {
    private NetVideoAdapter adapter;

    private ListView lv;
    private TextView tv_nodata;
    private ArrayList<MediaItem> mediaItems;
    //重写视图
    @Override
    public View initView() {
        Log.e("TAG","NetVideoPager-initView");
        View view = View.inflate(context, R.layout.fragment_net_video_pager,null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoveInfo.TrailersBean item = adapter.getItem(position);

//                Intent intent = new Intent(context, SystemVideoPlayerActivity.class);
//                intent.setDataAndType(Uri.parse(item.getUrl()),"video/*");
//                startActivity(intent);

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
        Log.e("TAG","NetVideoPager-initData");
        getDataFromNet();
    }

    private void getDataFromNet() {

        //配置联网请求地址
        final RequestParams request = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","xUtils联网成功=="+result);
                processData(result);


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","xUtils联网失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 解析json数据和显示数据
     * @param json
     */
    private void processData(String json) {
        MoveInfo moveInfo = new Gson().fromJson(json, MoveInfo.class);
        List<MoveInfo.TrailersBean> datas = moveInfo.getTrailers();



        if(datas != null && datas.size() >0){
            //集合数据MediaItem
            mediaItems = new ArrayList<>();
           for(int i = 0; i <datas.size() ; i++) {
               MediaItem mediaItem = new MediaItem();
               mediaItem.setData(datas.get(i).getUrl());
               mediaItem.setName(datas.get(i).getMovieName());
               mediaItems.add(mediaItem);

           }

            tv_nodata.setVisibility(View.GONE);
            //有数据-适配器
            adapter = new NetVideoAdapter(context,datas);
            lv.setAdapter(adapter);
        }else{
            tv_nodata.setVisibility(View.VISIBLE);
        }

    }
}
