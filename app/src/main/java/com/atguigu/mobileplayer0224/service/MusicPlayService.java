package com.atguigu.mobileplayer0224.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.atguigu.mobileplayer0224.IMusicPlayService;
import com.atguigu.mobileplayer0224.R;
import com.atguigu.mobileplayer0224.activity.AudioPlayerActivity;
import com.atguigu.mobileplayer0224.domain.MediaItem;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 作者：杨光福 on 2017/5/24 11:41
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class MusicPlayService extends Service {

    //.aidl文件生成的类
    private IMusicPlayService.Stub stub = new IMusicPlayService.Stub() {
        //得到服务实例的引用
        MusicPlayService service = MusicPlayService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return service.getArtistName();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mediaPlayer.isPlaying();
        }

        @Override
        public int getPlaymode() throws RemoteException {
            return service.getPlaymode();
        }

        @Override
        public void setPlaymode(int playmode) throws RemoteException {
            service.setPlaymode(playmode);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return mediaPlayer.getAudioSessionId();
        }
    };

    private ArrayList<MediaItem> mediaItems;
    private MediaPlayer mediaPlayer;
    /**
     * 音频列表的下标位置
     */
    private int position;
    /**
     * 代表一个音频信息类
     */
    private MediaItem mediaItem;

    /**
     * 当播放准备好的时候要发的广播
     */
    public static final String OPEN_COMPLETE = "com.atguigu.mobileplayer.OPEN_COMPLETE";


    //通知服务管理
    private NotificationManager nm;

    /**
     * 顺序播放
     */
    public static final int REPEAT_NORMAL = 1;

    /**
     * 单曲循环播放
     */
    public static final int REPEAT_SINGLE = 2;

    /**
     * 全部循环播放
     */
    public static final int REPEAT_ALL = 3;

    /**
     * 播放模式
     */
    private int playmode = REPEAT_NORMAL;
    /**
     * true:正常播放完成
     * false:人为手动点击下一个
     */
    private boolean isCompletion = false;

    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "MusicPlayService--onCreate()");
        //加载列表数据
        sp = getSharedPreferences("atguigu", MODE_PRIVATE);
        playmode = sp.getInt("playmode", getPlaymode());
        getData();
    }


    /**
     * 得到数据
     */
    private void getData() {
        new Thread() {
            public void run() {
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频在sdcard上的名称
                        MediaStore.Audio.Media.DURATION,//视频时长
                        MediaStore.Audio.Media.SIZE,//视频文件的大小
                        MediaStore.Audio.Media.DATA,//视频播放地址
                        MediaStore.Audio.Media.ARTIST//艺术家
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                        Log.e("TAG", "name==" + name + ",duration==" + duration + ",data===" + data + ",artist==" + artist);

                        if (duration > 10 * 1000) {
                            mediaItems.add(new MediaItem(name, duration, size, data, artist));
                        }

                    }

                    cursor.close();
                }

            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    public static long startTime = 0;

    /**
     * 根据位置播放一个音频
     *
     * @param position
     */
    private void openAudio(int position) {
        this.position = position;
        if (mediaItems != null && mediaItems.size() > 0) {

            if (position < mediaItems.size()) {
                mediaItem = mediaItems.get(position);

                //如果不为空释放之前的播放音频的资源
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer = null;
                }
                try {
                    mediaPlayer = new MediaPlayer();
                    //设置播放地址
                    mediaPlayer.setDataSource(mediaItem.getData());
                    //设置最基本的三个监听：准备完成，播放出错，播放完成
                    mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                    mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                    mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                    //准备
                    mediaPlayer.prepareAsync();

                    if (playmode == MusicPlayService.REPEAT_SINGLE) {
                        isCompletion = false;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(MusicPlayService.this, "音频还没有加载完成", Toast.LENGTH_SHORT).show();
        }


        //开始时间
        startTime = SystemClock.uptimeMillis();


    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {

            //发广播
            //notifyChange(OPEN_COMPLETE);
            start();
            //3.发消息
            EventBus.getDefault().post(mediaItem);



        }
    }

    /**
     * 发送广播
     *
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();//播放下一个
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            //播放下一个
            isCompletion = true;
            next();
        }
    }

    /**
     * 播放音频
     */
    private void start() {
        mediaPlayer.start();//开始播放
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("notification", true);//是否来自状态栏
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notifation = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("321音乐")
                .setContentText("正在播放：" + getAudioName())
                .setContentIntent(pi)
                .build();
        nm.notify(1, notifation);
    }

    /**
     * 暂停音频
     */
    private void pause() {
        mediaPlayer.pause();
        //取消通知
        nm.cancel(1);
    }

    /**
     * 得到演唱者
     *
     * @return
     */
    private String getArtistName() {
        return mediaItem.getArtist();

    }

    /**
     * 得到歌曲名
     *
     * @return
     */
    private String getAudioName() {
        return mediaItem.getName();
    }


    /**
     * 得到歌曲路径
     *
     * @return
     */
    private String getAudioPath() {
        return mediaItem.getData();
    }

    /**
     * 得到总时长
     *
     * @return
     */
    private int getDuration() {
        return mediaPlayer.getDuration();
    }


    /**
     * 得到当前播放进度
     *
     * @return
     */
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 音频拖动
     *
     * @param position
     */
    private void seekTo(int position) {
        //MediaPlayer的seekTo
        mediaPlayer.seekTo(position);
    }

    /**
     * 播放下一个
     */
    private void next() {
        //1.根据不同的播放模式设置不同的下标位置
        setNextPosition();

        //2.根据不同的下标位置打开对应的音频并且播放，边界处理
        openNextPosition();
    }

    //根据不同的下标位置打开对应的音频并且播放，边界处理
    private void openNextPosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            if (position < mediaItems.size()) {
                //合法范围
                openAudio(position);

            } else {
                //变为合法
                position = mediaItems.size() - 1;
            }
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (position < mediaItems.size()) {
                //合法范围
                openAudio(position);
            } else {
                //变为合法
                position = mediaItems.size() - 1;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            openAudio(position);
        }
    }

    /**
     * 根据不同的播放模式设置不同的下标位置
     */
    private void setNextPosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            //还没有越界处理
            position++;
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (!isCompletion) {
                position++;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            //合法的位置
            position++;
            if (position > mediaItems.size() - 1) {
                position = 0;
            }
        }
    }

    /**
     * 播放上一个
     */
    private void pre() {
        //1.根据不同的播放模式设置不同的下标位置
        setPrePosition();

        //2.根据不同的下标位置打开对应的音频并且播放，边界处理
        openPrePosition();
    }

    private void openPrePosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            if (position >= 0) {
                //合法范围
                openAudio(position);

            } else {
                //变为合法
                position = 0;
            }
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (position >= 0) {
                //合法范围
                openAudio(position);
            } else {
                //变为合法
                position = 0;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            openAudio(position);
        }
    }

    private void setPrePosition() {
        int playmode = getPlaymode();

        if (playmode == MusicPlayService.REPEAT_NORMAL) {
            //还没有越界处理
            position--;
        } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
            if (!isCompletion) {
                position--;
            }

        } else if (playmode == MusicPlayService.REPEAT_ALL) {
            //合法的位置
            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }
        }
    }

    /**
     * 得到播放模式
     *
     * @return
     */
    public int getPlaymode() {
        return playmode;
    }

    /**
     * 设置播放模式
     *
     * @param playmode
     */
    public void setPlaymode(int playmode) {
        this.playmode = playmode;
        sp.edit().putInt("playmode", playmode).commit();
    }
}
