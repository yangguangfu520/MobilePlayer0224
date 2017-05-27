package com.atguigu.mobileplayer0224.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.atguigu.mobileplayer0224.domain.Lyric;
import com.atguigu.mobileplayer0224.utils.DensityUtil;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2017/5/26 11:39
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class LyricShowView extends TextView {
    private final Context context;
    private Paint paintGreen;
    private Paint paintWhite;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;
    /**
     * 表示的是歌词列表中的哪一句
     */
    private int index = 0;
    private float textHeight = 20;
    private float currentPosition;
    /**
     * 时间戳
     */
    private long timePoint;
    /**
     * 高亮显示时间
     */
    private long sleepTime;

    public LyricShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        textHeight = DensityUtil.dip2px(context,20);
        paintGreen = new Paint();
        //设置画笔颜色
        paintGreen.setColor(Color.GREEN);
        //设置抗锯齿
        paintGreen.setAntiAlias(true);
        //设置文字大小
        paintGreen.setTextSize(DensityUtil.dip2px(context,16));
        //设置居中
        paintGreen.setTextAlign(Paint.Align.CENTER);

        paintWhite = new Paint();
        //设置画笔颜色
        paintWhite.setColor(Color.WHITE);
        //设置抗锯齿
        paintWhite.setAntiAlias(true);
        //设置文字大小
        paintWhite.setTextSize(DensityUtil.dip2px(context,16));
        //设置居中
        paintWhite.setTextAlign(Paint.Align.CENTER);

//        //准备歌词
//        lyrics = new ArrayList<>();
//        Lyric lyric = new Lyric();
//        for (int i = 0; i < 10000; i++) {
//            //不同歌词
//            lyric.setContent("aaaaaaaaaaaa_" + i);
//            lyric.setSleepTime(2000);
//            lyric.setTimePoint(2000 * i);
//            //添加到集合
//            lyrics.add(lyric);
//            //重新创建新对象
//            lyric = new Lyric();
//        }

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
        if (lyrics != null && lyrics.size() > 0) {

            if(index != lyrics.size()-1){
                float push = 0;

                if (sleepTime == 0) {
                    push = 0;
                } else {
                    // 这一句花的时间： 这一句休眠时间  =  这一句要移动的距离：总距离(行高)
                    //这一句要移动的距离 = （这一句花的时间/这一句休眠时间） * 总距离(行高)
                    push = ((currentPosition - timePoint) / sleepTime) * textHeight;
                }
                canvas.translate(0, -push);
            }

            //有歌词
            //当前句-中心的哪一句
            String currentContent = lyrics.get(index).getContent();
            canvas.drawText(currentContent, width / 2, height / 2, paintGreen);

            //得到中间句的坐标
            float tempY = height / 2;

            //绘制前面部分
            for (int i = index - 1; i >= 0; i--) {

                //得到前一部分多月的歌词内容
                String preContent = lyrics.get(i).getContent();

                tempY = tempY - textHeight;
                if (tempY < 0) {
                    break;
                }

                //绘制内容
                canvas.drawText(preContent, width / 2, tempY, paintWhite);

            }

            tempY = height / 2;

            //绘制后面部分
            for (int i = index + 1; i < lyrics.size(); i++) {
                //得到后一部分多月的歌词内容
                String nextContent = lyrics.get(i).getContent();

                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }

                //绘制内容
                canvas.drawText(nextContent, width / 2, tempY, paintWhite);
            }


        } else {
            canvas.drawText("没有找到歌词...", width / 2, height / 2, paintGreen);
        }


    }

    /**
     * 根据播放的位置查找或者计算出当前该高亮显示的是哪一句
     * 并且得到这一句对应的相关信息
     *
     * @param currentPosition
     */
    public void setNextShowLyric(int currentPosition) {
        this.currentPosition = currentPosition;
        if (lyrics == null || lyrics.size() == 0)
            return;

        for (int i = 1; i < lyrics.size(); i++) {

            if (currentPosition < lyrics.get(i).getTimePoint()) {
                int tempIndex = i - 1;
                if (currentPosition >= lyrics.get(tempIndex).getTimePoint()) {
                    //中间高亮显示的哪一句
                    index = tempIndex;
                    //时间戳
                    timePoint = lyrics.get(index).getTimePoint();
                    //高亮显示时间
                    sleepTime = lyrics.get(index).getSleepTime();

                }
            }else {
                index  = i;
            }

        }

        //什么方法导致onDraw
        invalidate();


    }

    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;

    }
}
