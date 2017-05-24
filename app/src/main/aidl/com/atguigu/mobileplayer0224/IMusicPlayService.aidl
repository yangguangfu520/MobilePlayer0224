// IMusicPlayService.aidl
package com.atguigu.mobileplayer0224;

// Declare any non-default types here with import statements

interface IMusicPlayService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

             /**
                 * 根据位置播放一个音频
                 *
                 * @param position
                 */
                 void openAudio(int position) ;

                /**
                 * 播放音频
                 */
                 void start() ;

                /**
                 * 暂停音频
                 */
                 void pause();

                /**
                 * 得到演唱者
                 *
                 * @return
                 */
                 String getArtistName();

                /**
                 * 得到歌曲名
                 *
                 * @return
                 */
                 String getAudioName() ;


                /**
                 * 得到歌曲路径
                 *
                 * @return
                 */
                 String getAudioPath();

                /**
                 * 得到总时长
                 *
                 * @return
                 */
                 int getDuration() ;


                /**
                 * 得到当前播放进度
                 *
                 * @return
                 */
                 int getCurrentPosition() ;

                /**
                 * 音频拖动
                 *
                 * @param position
                 */
                 void seekTo(int position) ;
                /**
                 * 播放下一个
                 */
                 void  next();

                /**
                 * 播放上一个
                 */
                 void  pre();

                 /**
                 * 是否正在播放
                 */
                 boolean isPlaying();

                 int getPlaymode();
                 void setPlaymode(int playmode);


}
