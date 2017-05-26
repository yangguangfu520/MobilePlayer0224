package com.atguigu.mobileplayer0224.domain;

/**
 * 作者：杨光福 on 2017/5/26 11:48
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：代表一句歌词
 * [01:03.35]我在这里哭泣
 */

public class Lyric {
    /**
     * 这一句歌词内容
     */
    private String content;
    /**
     * 时间戳
     */
    private long timePoint;
    /**
     * 高亮持续时间
     */
    private long sleepTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(long timePoint) {
        this.timePoint = timePoint;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "content='" + content + '\'' +
                ", timePoint=" + timePoint +
                ", sleepTime=" + sleepTime +
                '}';
    }
}
