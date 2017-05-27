package com.atguigu.mobileplayer0224.utils;

import com.atguigu.mobileplayer0224.domain.Lyric;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 作者：杨光福 on 2017/5/26 15:31
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class LyricsUtils {

    private ArrayList<Lyric> lyrics;

    /**
     * 得到歌词列表
     *
     * @return
     */
    public ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    /**
     * 是否歌词存在
     * @return
     */
    public boolean isLyric() {
        return isLyric;
    }

    private boolean isLyric = false;

    /**
     * 解析歌词，把解析好的放入集合中
     *
     * @param file
     */
    public void readFile(File file) {
        if (file == null || !file.exists()) {
            //文件不存在
            isLyric = false;
        } else {
            //歌词文件存在
            lyrics = new ArrayList<>();
            isLyric = true;

            //读取文件，并且一行一行的读取
            FileInputStream fis = null;
            try {
                //文件输入流
                fis = new FileInputStream(file);
                InputStreamReader streamReader = new InputStreamReader(fis, getCharset(file));
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    //解析每一行歌词,并且把解析好的歌词加入到集合里面
                    analyzeLyric(line);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            //把解析好的歌词排序
            Collections.sort(lyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric o1, Lyric o2) {
                    if (o1.getTimePoint() < o2.getTimePoint()) {
                        return -1;
                    } else if (o1.getTimePoint() > o2.getTimePoint()) {
                        return 1;
                    } else {
                        return 0;
                    }

                }
            });

            //计算每一句个高亮显示的时间
            for (int i = 0; i < lyrics.size(); i++) {
                Lyric oneLyric = lyrics.get(i);//高亮时间

                if (i + 1 < lyrics.size()) {
                    Lyric twoLyric = lyrics.get(i + 1);
                    //后一句的时间戳减掉当前句的时间戳
                    oneLyric.setSleepTime(twoLyric.getTimePoint() - oneLyric.getTimePoint());
                }
            }


        }

    }

    /**
     * 判断文件编码
     * @param file 文件
     * @return 编码：GBK,UTF-8,UTF-16LE
     */
    public String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * 解析歌词-某一行
     *
     * @param line [02:04.12][03:37.32][00:59.73]我在这里欢笑
     */
    private void analyzeLyric(String line) {
        int pos1 = line.indexOf("[");//0
        int pos2 = line.indexOf("]");//9//如果没有就返回-1

        if (pos1 == 0 && pos2 != -1) {//至少有一句歌词
            //装long类型的时间戳
            long[] timeLongs = new long[getCountTag(line)];
            String timeStr = line.substring(pos1 + 1, pos2);//02:04.12
            //解析第0句
            timeLongs[0] = stringToLong(timeStr);//02:04.12转换成long的毫秒类型

            if (timeLongs[0] == -1) {
                return;
            }

            int i = 1;
            //[02:04.12][03:37.32][00:59.73]我在这里欢笑
            String content = line;
            while (pos1 == 0 && pos2 != -1) {
                content = content.substring(pos2 + 1);//[03:37.32][00:59.73]我在这里欢笑-->[00:59.73]我在这里欢笑-->我在这里欢笑
                pos1 = content.indexOf("[");//0->-1
                pos2 = content.indexOf("]");//9//如果没有就返回-1

                if (pos1 == 0 && pos2 != -1) {//至少还有一句

                    timeStr = content.substring(pos1 + 1, pos2);//03:37.32-->00:59.73
                    //解析第1句
                    timeLongs[i] = stringToLong(timeStr);//02:04.12转换成long的毫秒类型-->00:59.73转换成毫秒

                    if (timeLongs[i] == -1) {
                        return;
                    }
                    i++;//2->3

                }
            }


            ////装long类型的时间戳

            for (int j = 0; j < timeLongs.length; j++) {
                if (timeLongs[j] != 0) {
                    Lyric lyric = new Lyric();
                    //设置内容
                    lyric.setTimePoint(timeLongs[j]);
                    //我在这里欢笑
                    lyric.setContent(content);

                    lyrics.add(lyric);
                }


            }

        }

    }

    /**
     * 把02:04.12转换成long的毫秒类型
     *
     * @param timeStr 02:04.12
     * @return
     */
    private long stringToLong(String timeStr) {
        long result = -1;
        try {
            //1.根据: 把02:04.12切换成02和04.12
            String[] s1 = timeStr.split(":");
            //2.根据.把04.12切成04和12
            String[] s2 = s1[1].split("\\.");
            //3.把他们转换成毫秒
            //分
            long min = Long.valueOf(s1[0]);//02
            //秒
            long second = Long.valueOf(s2[0]);//04
            //毫秒
            long mil = Long.valueOf(s2[1]);//12

            result = min * 60 * 1000 + second * 1000 + mil * 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断有多少句歌词
     *
     * @param line [02:04.12][03:37.32][00:59.73]我在这里欢笑
     * @return
     */
    private int getCountTag(String line) {
        int result = 1;
        String[] s1 = line.split("\\[");
        String[] s2 = line.split("\\]");
        if (s1.length == 0 && s2.length == 0) {
            result = 1;
        } else if (s1.length > s2.length) {
            result = s1.length;
        } else {
            result = s2.length;
        }
        return result;
    }
}
