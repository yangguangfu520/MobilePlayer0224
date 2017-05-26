package com.atguigu.mobileplayer0224.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.domain.Lyric;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2017/5/26 11:39
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class LyricShowView extends TextView {
    private Paint paint;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;

    public LyricShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        paint = new Paint();
        //设置画笔颜色
        paint.setColor(Color.GREEN);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置文字大小
        paint.setTextSize(16);
        //设置居中
        paint.setTextAlign(Paint.Align.CENTER);

        //准备歌词
        lyrics = new ArrayList<>();
        Lyric lyric = new Lyric();
        for (int i = 0; i < 10000; i++) {
            //不同歌词
            lyric.setContent("aaaaaaaaaaaa_" + i);
            lyric.setSleepTime(2000);
            lyric.setTimePoint(2000*i);
            //添加到集合
            lyrics.add(lyric);
            //重新创建新对象
            lyric = new Lyric();
        }

//        for (int i = 0; i < 10000; i++) {
//            Lyric lyric = new Lyric();
//            //不同歌词
//            lyric.setContent("aaaaaaaaaaaa_" + i);
//            lyric.setSleepTime(2000);
//            lyric.setTimePoint(2000*i);
//            //添加到集合
//            lyrics.add(lyric);
//            //重新创建新对象
//        }
    }


    /**
     * 绘制歌词
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("没有找到歌词...", width / 2, height / 2, paint);
    }
}
