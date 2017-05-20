package com.atguigu.mobileplayer0224.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.utils.Utils;

public class SystemVideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    //视频进度更新
    private static final int PROGRESS = 0;
    private VideoView vv;
    private Uri uri;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnSwitchScreen;
    private Utils utils;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-20 11:01:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnPre = (Button)findViewById( R.id.btn_pre );
        btnStartPause = (Button)findViewById( R.id.btn_start_pause );
        btnNext = (Button)findViewById( R.id.btn_next );
        btnSwitchScreen = (Button)findViewById( R.id.btn_switch_screen );
        vv = (VideoView)findViewById(R.id.vv);

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnStartPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnSwitchScreen.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-20 11:01:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
        } else if ( v == btnPre ) {
            // Handle clicks for btnPre
        } else if ( v == btnStartPause ) {
            if(vv.isPlaying()){
                //暂停
                vv.pause();
                //按钮状态-播放
                btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
            }else {
                //播放
                vv.start();
                //按钮状态-暂停
                btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
            }
            // Handle clicks for btnStartPause
        } else if ( v == btnNext ) {
            // Handle clicks for btnNext
        } else if ( v == btnSwitchScreen ) {
            // Handle clicks for btnSwitchScreen
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    //得到当前进度
                    int currentPosition = vv.getCurrentPosition();
                    //让SeekBar进度更新
                    seekbarVideo.setProgress(currentPosition);

                    //设置文本当前的播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //循环发消息
                    sendEmptyMessageDelayed(PROGRESS,1000);

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils();

        findViews();


        //得到播放地址
        uri = getIntent().getData();
        setListener();
        //设置播放地址
        vv.setVideoURI(uri);

        //设置控制面板
//        vv.setMediaController(new MediaController(this));




    }

    private void setListener() {
        //设置播放器三个监听：播放准备好的监听，播放完成的监听，播放出错的监听
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            //底层准备播放完成的时候回调
            @Override
            public void onPrepared(MediaPlayer mp) {
                //得到视频的总时长
                int duration = vv.getDuration();
                seekbarVideo.setMax(duration);
                //设置文本总时间
                tvDuration.setText(utils.stringForTime(duration));
                //vv.seekTo(100);
                vv.start();//开始播放

                //发消息开始更新播放进度
                handler.sendEmptyMessage(PROGRESS);
            }
        });

        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayerActivity.this, "播放出错了哦", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //设置监听播放完成
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemVideoPlayerActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
                finish();//退出当前页面
            }
        });

        //设置Seekbar状态改变的监听
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             *
             * @param seekBar
             * @param progress
             * @param fromUser true:用户拖动改变的，false:系统更新改变的
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    vv.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //把所有消息移除
        handler.removeCallbacksAndMessages(null);
    }
}
