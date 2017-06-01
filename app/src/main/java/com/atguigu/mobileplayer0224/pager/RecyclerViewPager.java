package com.atguigu.mobileplayer0224.pager;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.adapter.MyRecyclerViewAdapter;
import com.atguigu.mobileplayer0224.domain.NetAudioBean;
import com.atguigu.mobileplayer0224.fragment.BaseFragment;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：杨光福 on 2017/6/1 14:35
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class RecyclerViewPager extends BaseFragment {
    /**
     * 联网请求的地址
     */
    private String url = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";

    @Bind(R.id.recyclerview)
    android.support.v7.widget.RecyclerView recyclerview;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;
    private MyRecyclerViewAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_recyclerview, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getDataFromNet() {
        RequestParams reques = new RequestParams(url);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e("onSuccess==" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    /**
     * 解析数据和显示数据
     * @param json
     */
    private void processData(String json) {
        //解析数据
        NetAudioBean netAudioBean = new Gson().fromJson(json, NetAudioBean.class);
        List<NetAudioBean.ListBean> datas = netAudioBean.getList();
        String text = datas.get(0).getText();
//        Toast.makeText(context, "text=="+text, Toast.LENGTH_SHORT).show();
        if(datas != null && datas.size() >0){
            //有数据
            tvNomedia.setVisibility(View.GONE);
            //设置适配器
            adapter = new MyRecyclerViewAdapter(context,datas);

            recyclerview.setAdapter(adapter);

            //设置布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));


        }else{
            //没有数据
            tvNomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);

    }
}
