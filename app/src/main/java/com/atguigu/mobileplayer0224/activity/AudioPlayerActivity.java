package com.atguigu.mobileplayer0224.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.service.MusicPlayService;

public class AudioPlayerActivity extends AppCompatActivity {
    private ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        //初始化控件
        iv_icon = (ImageView)findViewById(R.id.iv_icon);
        iv_icon.setBackgroundResource(R.drawable.animation_bg);
        AnimationDrawable background = (AnimationDrawable) iv_icon.getBackground();
        background.start();

        Intent intent = new Intent(this, MusicPlayService.class);
        startService(intent);
    }
}
