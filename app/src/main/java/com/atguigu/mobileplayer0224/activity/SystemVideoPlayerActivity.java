package com.atguigu.mobileplayer0224.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.domain.MediaItem;
import com.atguigu.mobileplayer0224.utils.Utils;
import com.atguigu.mobileplayer0224.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemVideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    //视频进度更新
    private static final int PROGRESS = 0;
    //隐藏控制面板
    private static final int HIDE_MEDIACONTROLLER = 1;
    //显示网速
    private static final int SHOW_NET_SPEED = 2;

    //默认视频画面
    private static final int DEFUALT_SCREEN = 0;
    //全屏视频画面
    private static final int FULL_SCREEN = 1;

    private VideoView vv;
    private Uri uri;
    private ArrayList<MediaItem> mediaItems;
    ;

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
    private LinearLayout ll_buffering;
    private LinearLayout ll_loading;
    private TextView tv_loading_net_speed;
    private TextView tv_net_speed;
    private Utils utils;
    private MyBroadCastReceiver receiver;
    /**
     * 视频列表的位置
     */
    private int position;
    //手势识别器
    private GestureDetector detector;

    /**
     * 是否全屏
     */
    private boolean isFullScreen = false;
    /**
     * 屏幕的高
     */
    private int screenHeight;
    private int screenWidth;
    //视频的原生的宽和高
    private int videoWidth;
    private int videoHeight;

    //当前的音量：0~15之间
    private int currentVoice;
    private AudioManager am;
    //最大音量
    private int maxVoice;
    //是否静音
    private boolean isMute = false;
    /**
     * 是否是网络资源
     */
    private boolean isNetUri = true;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-20 11:01:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwitchPlayer = (Button) findViewById(R.id.btn_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnStartPause = (Button) findViewById(R.id.btn_start_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSwitchScreen = (Button) findViewById(R.id.btn_switch_screen);
        vv = (VideoView) findViewById(R.id.vv);
        ll_buffering = (LinearLayout) findViewById(R.id.ll_buffering);
        tv_net_speed = (TextView) findViewById(R.id.tv_net_speed);
        ll_loading = (LinearLayout)findViewById(R.id.ll_loading);
        tv_loading_net_speed = (TextView)findViewById(R.id.tv_loading_net_speed);
        btnVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSwitchScreen.setOnClickListener(this);

        //关联最大音量
        seekbarVoice.setMax(maxVoice);
        //设置当前进度
        seekbarVoice.setProgress(currentVoice);

        //发消息开始显示网速
        handler.sendEmptyMessage(SHOW_NET_SPEED);
    }


    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-20 11:01:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            // Handle clicks for btnVoice
            isMute = !isMute;

            updateVoice(isMute);


        } else if (v == btnSwitchPlayer) {
            // Handle clicks for btnSwitchPlayer
            switchPlayer();
        } else if (v == btnExit) {
            finish();
            // Handle clicks for btnExit
        } else if (v == btnPre) {
            setPreVideo();
            // Handle clicks for btnPre
        } else if (v == btnStartPause) {
            setStartOrPause();
            // Handle clicks for btnStartPause
        } else if (v == btnNext) {
            setNextVideo();
            // Handle clicks for btnNext
        } else if (v == btnSwitchScreen) {
            // Handle clicks for btnSwitchScreen
            if (isFullScreen) {
                //默认
                setVideoType(DEFUALT_SCREEN);
            } else {
                //全屏
                setVideoType(FULL_SCREEN);
            }
        }

        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
    }

    private void switchPlayer() {
        new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前使用系统播放器播放，当播放有声音没有画面，请切换到万能播放器播放")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startVitamioPlayer();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
    }

    private void updateVoice(boolean isMute) {
        if (isMute) {
            //静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        } else {
            //非静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoice, 0);
            seekbarVoice.setProgress(currentVoice);
        }
    }

    /**
     * 设置视频的全屏和默认
     *
     * @param videoType
     */
    private void setVideoType(int videoType) {
        switch (videoType) {
            case FULL_SCREEN:
                isFullScreen = true;
                //按钮状态-默认
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_default_selector);
                //设置视频画面为全屏显示
                vv.setVideoSize(screenWidth, screenHeight);

                break;
            case DEFUALT_SCREEN:
                isFullScreen = false;
                //按钮状态-全屏
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_full_selector);
                //视频原生的宽和高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                //计算好的要显示的视频的宽和高
                int width = screenWidth;
                int height = screenHeight;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                vv.setVideoSize(width, height);

                break;
        }
    }

    private void setStartOrPause() {
        if (vv.isPlaying()) {
            //暂停
            vv.pause();
            //按钮状态-播放
            btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
        } else {
            //播放
            vv.start();
            //按钮状态-暂停
            btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
        }
    }

    private int preCurrentPosition;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_NET_SPEED:
                    if(isNetUri){
                        String netSpeed = utils.getNetSpeed(SystemVideoPlayerActivity.this);
                        tv_loading_net_speed.setText("正在加载中...."+netSpeed);
                        tv_net_speed.setText("正在缓冲...."+netSpeed);
                        sendEmptyMessageDelayed(SHOW_NET_SPEED,1000);
                    }
                    break;
                case PROGRESS:
                    //得到当前进度
                    int currentPosition = vv.getCurrentPosition();
                    //让SeekBar进度更新
                    seekbarVideo.setProgress(currentPosition);

                    //设置文本当前的播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //得到系统时间
                    tvSystemTime.setText(getSystemTime());

                    //设置视频缓存效果
                    if (isNetUri) {
                        int bufferPercentage = vv.getBufferPercentage();//0~100;
                        int totalBuffer = bufferPercentage * seekbarVideo.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    } else {
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    if(isNetUri && vv.isPlaying()){

                        int duration = currentPosition - preCurrentPosition;
                        if(duration <500){
                            //卡
                            ll_buffering.setVisibility(View.VISIBLE);
                        }else{
                            //不卡
                            ll_buffering.setVisibility(View.GONE);
                        }

                        preCurrentPosition = currentPosition;
                    }

                    //循环发消息
                    sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
                case HIDE_MEDIACONTROLLER://隐藏控制面板
                    hideMediaController();
                    break;
            }
        }
    };

    /**
     * 得到系统时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        findViews();//初始化视图
        getData();

        setListener();
        setData();

        //设置控制面板
//        vv.setMediaController(new MediaController(this));


    }

    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {

            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());
            vv.setVideoPath(mediaItem.getData());
            isNetUri = utils.isNetUri(mediaItem.getData());

        } else if (uri != null) {
            //设置播放地址
            vv.setVideoURI(uri);
            tvName.setText(uri.toString());
            isNetUri = utils.isNetUri(uri.toString());
        }

        setButtonStatus();

    }

    private void getData() {
        //得到播放地址
        uri = getIntent().getData();//获取从外界传入的播放地址-一般只有一个
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);

    }

    private void initData() {
        utils = new Utils();

        //注册监听电量变化广播
        receiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //监听电量变化
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, intentFilter);


        //实例化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
//                Toast.makeText(SystemVideoPlayerActivity.this, "长按了", Toast.LENGTH_SHORT).show();
                setStartOrPause();
                super.onLongPress(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
//                Toast.makeText(SystemVideoPlayerActivity.this, "双击了", Toast.LENGTH_SHORT).show();
                if (isFullScreen) {
                    //默认
                    setVideoType(DEFUALT_SCREEN);
                } else {
                    //全屏
                    setVideoType(FULL_SCREEN);
                }
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Toast.makeText(SystemVideoPlayerActivity.this, "单击了", Toast.LENGTH_SHORT).show();
                if (isShowMediaController) {
                    hideMediaController();
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                } else {
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });


        //得到屏幕的宽和高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        //初始化声音相关
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);




    }

    //记录坐标
    private float dowY;
    //滑动的初始声音
    private int mVol;
    //滑动的最大区域
    private float touchRang;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把事件交给手势识别器解析
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录相关参数
                dowY = event.getY();
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRang = Math.min(screenHeight, screenWidth);//screenHeight
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                //2.滑动的时候来到新的位置
                float endY = event.getY();
                //3.计算滑动的距离
                float distanceY = dowY - endY;
                //原理：在屏幕滑动的距离： 滑动的总距离 = 要改变的声音： 最大声音
                //要改变的声音 = （在屏幕滑动的距离/ 滑动的总距离）*最大声音;
                float delta = (distanceY / touchRang) * maxVoice;


                if (delta != 0) {
                    //最终声音 = 原来的+ 要改变的声音
                    int mVoice = (int) Math.min(Math.max(mVol + delta, 0), maxVoice);
                    //0~15

                    updateVoiceProgress(mVoice);
                }


                //注意不要重新赋值
//                dowY = event.getY();


                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否显示控制面板
     */
    private boolean isShowMediaController = false;

    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        llBottom.setVisibility(View.INVISIBLE);
        llTop.setVisibility(View.GONE);
        isShowMediaController = false;
    }

    public void showMediaController() {
        llBottom.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.VISIBLE);
        isShowMediaController = true;
    }


    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//主线程
            Log.e("TAG", "level==" + level);
            setBatteryView(level);

        }
    }

    private void setBatteryView(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setListener() {
        //设置播放器三个监听：播放准备好的监听，播放完成的监听，播放出错的监听
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            //底层准备播放完成的时候回调
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                //得到视频的总时长
                int duration = vv.getDuration();
                seekbarVideo.setMax(duration);
                //设置文本总时间
                tvDuration.setText(utils.stringForTime(duration));
                //vv.seekTo(100);
                vv.start();//开始播放

                //发消息开始更新播放进度
                handler.sendEmptyMessage(PROGRESS);

                //隐藏加载效果画面
                ll_loading.setVisibility(View.GONE);

                //默认隐藏
                hideMediaController();

                //设置默认屏幕
                setVideoType(DEFUALT_SCREEN);

//                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//                    @Override
//                    public void onSeekComplete(MediaPlayer mp) {
//                        Toast.makeText(SystemVideoPlayerActivity.this, "拖动完成", Toast.LENGTH_SHORT).show();
//                    }
//                });

                if(vv.isPlaying()){
                    //设置暂停
                    btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
                }else {
                    btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
                }
            }
        });

        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
               // Toast.makeText(SystemVideoPlayerActivity.this, "播放出错了哦", Toast.LENGTH_SHORT).show();
                //一进来播放就会报错-视频格式不支持 --- 跳转到万能播放器
                startVitamioPlayer();
                //播放过程中网络中断导致播放异常--重新播放-三次重试
                //文件中间部分损坏或者文件不完整-把下载做好
                return true;
            }
        });

        //设置监听播放完成
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Toast.makeText(SystemVideoPlayerActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
//                finish();//退出当前页面
                setNextVideo();
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
                if (fromUser) {
                    vv.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            }
        });



//        //设置监听卡
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//                @Override
//                public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                    switch (what) {
//                        //拖动卡，缓存卡
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                            ll_buffering.setVisibility(View.VISIBLE);
//                            break;
//                        //拖动卡，缓存卡结束
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                            ll_buffering.setVisibility(View.GONE);
//                            break;
//                    }
//
//                    return true;
//                }
//            });
//        }

        //监听拖动声音
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateVoiceProgress(progress);
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

    private void startVitamioPlayer() {
        if(vv != null){
            vv.stopPlayback();
        }
        Intent intent = new Intent(this, VitamioVideoPlayerActivity.class);
        if(mediaItems != null && mediaItems.size() >0){
            Bundle bunlder = new Bundle();
            bunlder.putSerializable("videolist",mediaItems);
            intent.putExtra("position",position);
            //放入Bundler
            intent.putExtras(bunlder);
        }else if(uri != null){
            intent.setData(uri);
        }
        startActivity(intent);
        finish();//关闭系统播放器
    }

    /**
     * 设置滑动改变声音
     *
     * @param progress
     */
    private void updateVoiceProgress(int progress) {
        currentVoice = progress;
        //真正改变声音
        am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoice, 0);
        //改变进度条
        seekbarVoice.setProgress(currentVoice);
        if (currentVoice <= 0) {
            isMute = true;
        } else {
            isMute = false;
        }

    }

    private void setPreVideo() {
        position--;
        if (position > 0) {
            //还是在列表范围内容
            MediaItem mediaItem = mediaItems.get(position);
            isNetUri = utils.isNetUri(mediaItem.getData());
            ll_loading.setVisibility(View.VISIBLE);
            vv.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());


            //设置按钮状态
            setButtonStatus();


        }

    }

    private void setNextVideo() {
        position++;
        if (position < mediaItems.size()) {
            //还是在列表范围内容
            MediaItem mediaItem = mediaItems.get(position);
            isNetUri = utils.isNetUri(mediaItem.getData());
            ll_loading.setVisibility(View.VISIBLE);

            vv.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());

            //设置按钮状态
            setButtonStatus();


        } else {
            Toast.makeText(this, "退出播放器", Toast.LENGTH_SHORT).show();
            finish();


        }

    }

    private void setButtonStatus() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //有视频播放
            setEnable(true);

            if (position == 0) {
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
            }

            if (position == mediaItems.size() - 1) {
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnNext.setEnabled(false);
            }

        } else if (uri != null) {
            //上一个和下一个不可用点击
            setEnable(false);
        }
    }

    /**
     * 设置按钮是否可以点击
     *
     * @param b
     */
    private void setEnable(boolean b) {
        if (b) {
            //上一个和下一个都可以点击
            btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
            btnNext.setBackgroundResource(R.drawable.btn_next_selector);
        } else {
            //上一个和下一个灰色，并且不可用点击
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
        }
        btnPre.setEnabled(b);
        btnNext.setEnabled(b);
    }

    @Override
    protected void onDestroy() {

        if (handler != null) {
            //把所有消息移除
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        //取消注册
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }

        super.onDestroy();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            currentVoice--;
            updateVoiceProgress(currentVoice);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            currentVoice++;
            updateVoiceProgress(currentVoice);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
