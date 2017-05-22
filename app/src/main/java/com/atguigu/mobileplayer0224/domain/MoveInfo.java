package com.atguigu.mobileplayer0224.domain;

import java.util.List;

/**
 * 作者：杨光福 on 2017/5/22 17:00
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class MoveInfo {

    private List<TrailersBean> trailers;

    public List<TrailersBean> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<TrailersBean> trailers) {
        this.trailers = trailers;
    }

    public static class TrailersBean {
        /**
         * id : 65732
         * movieName : 《表情奇幻冒险》预告
         * coverImg : http://img5.mtime.cn/mg/2017/05/18/155628.79082982.jpg
         * movieId : 233547
         * url : http://vfx.mtime.cn/Video/2017/05/17/mp4/170517091619822234.mp4
         * hightUrl : http://vfx.mtime.cn/Video/2017/05/17/mp4/170517091619822234.mp4
         * videoTitle : 表情奇幻冒险 中文预告片
         * videoLength : 119
         * rating : -1
         * type : ["动画","冒险","喜剧","家庭","科幻"]
         * summary : 表情小伙伴穿行手机拯救世界
         */

        private int id;
        private String movieName;
        private String coverImg;
        private int movieId;
        private String url;
        private String hightUrl;
        private String videoTitle;
        private int videoLength;
        private double rating;
        private String summary;
        private List<String> type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHightUrl() {
            return hightUrl;
        }

        public void setHightUrl(String hightUrl) {
            this.hightUrl = hightUrl;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public int getVideoLength() {
            return videoLength;
        }

        public void setVideoLength(int videoLength) {
            this.videoLength = videoLength;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<String> getType() {
            return type;
        }

        public void setType(List<String> type) {
            this.type = type;
        }
    }
}
