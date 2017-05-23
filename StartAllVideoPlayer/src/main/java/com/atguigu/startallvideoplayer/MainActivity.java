package com.atguigu.startallvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startAllVideoPlayer(View v) {
        //把系统的播放器调起来
                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse("http://192.168.31.168:8080/yellow.mp4"),"video/*");
//                intent.setDataAndType(Uri.parse("http://vf1.mtime.cn/Video/2017/05/17/mp4/170517102706759383.mp4"),"video/*");
//                intent.setDataAndType(Uri.parse("http://192.168.31.168:8080/oppo.mp4"),"video/*");
                intent.setDataAndType(Uri.parse("http://cctv13.live.cntv.dnion.com/live/cctv13hls_/index.m3u8?ptype=1&amode=1&AUTH=cntv000120170323hjvqbX25hcXD0Uv8+LuCv97jFbrSV1VYzmcIeADyxyLbnCha/lfK+x+cJlpEhm61Ln3/AGtn26Tde/9rtoiZeg=="),"video/*");
//                intent.setDataAndType(Uri.parse("http://vf2.mtime.cn/Video/2017/05/17/mp4/170517091619822234.mp4"),"video/*");
                startActivity(intent);

    }
}
