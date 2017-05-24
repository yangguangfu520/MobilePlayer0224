package com.atguigu.mobileplayer0224.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.IMusicPlayService;
import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.service.MusicPlayService;

import static com.atguigu.mobileplayer0224.R.id.iv_icon;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlTop;
    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvAudioname;
    private LinearLayout llBottom;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnPlaymode;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnLyric;
    //这个就是IMusicPlayService.Stub的实例
    private IMusicPlayService service;
    private int position;
    //连接好服务后的回调
    private ServiceConnection conon = new ServiceConnection() {

        /**
         * 当绑定服务成功后的回调
         * @param name
         * @param iBinder 就是IMusicPlayService.Stub的实例
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            //这个就是stub，stub包含很多方法，这些方法调用服务的方法
            service =  IMusicPlayService.Stub.asInterface(iBinder);
            if(service != null){
                try {
                    service.openAudio(position);//打开播放第0个音频
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当断开连接的时候回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-24 14:38:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_audio_player);
        //初始化控件
        ivIcon = (ImageView)findViewById(iv_icon);
        ivIcon.setBackgroundResource(R.drawable.animation_bg);
        AnimationDrawable background = (AnimationDrawable) ivIcon.getBackground();
        background.start();
        rlTop = (RelativeLayout)findViewById( R.id.rl_top );
        ivIcon = (ImageView)findViewById( iv_icon );
        tvArtist = (TextView)findViewById( R.id.tv_artist );
        tvAudioname = (TextView)findViewById( R.id.tv_audioname );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvTime = (TextView)findViewById( R.id.tv_time );
        seekbarAudio = (SeekBar)findViewById( R.id.seekbar_audio );
        btnPlaymode = (Button)findViewById( R.id.btn_playmode );
        btnPre = (Button)findViewById( R.id.btn_pre );
        btnStartPause = (Button)findViewById( R.id.btn_start_pause );
        btnNext = (Button)findViewById( R.id.btn_next );
        btnLyric = (Button)findViewById( R.id.btn_lyric );

        btnPlaymode.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnStartPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnLyric.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-24 14:38:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnPlaymode ) {
            // Handle clicks for btnPlaymode
        } else if ( v == btnPre ) {
            // Handle clicks for btnPre
        } else if ( v == btnStartPause ) {
            // Handle clicks for btnStartPause
            try {
                if(service.isPlaying()){
                    //暂停
                    service.pause();
                    //按钮状态-播放
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                }else{
                    //播放
                    service.start();
                    //按钮专题-暂停
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else if ( v == btnNext ) {
            // Handle clicks for btnNext
        } else if ( v == btnLyric ) {
            // Handle clicks for btnLyric
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        getData();
        startAndBindService();
    }

    private void getData() {
        position = getIntent().getIntExtra("position",0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conon != null){
            unbindService(conon);
            conon = null;
        }
    }

    /**
     * 启动服务
     */
    private void startAndBindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
//        intent.setAction("com.atguigu.mobileplayer0224.service.MUSICPLAYSERVICE");
        //绑定-得到服务的操作对象-IMusicPlayService service
        bindService(intent,conon, Context.BIND_AUTO_CREATE);
        //防止多次实例化Service
        startService(intent);
    }
}
