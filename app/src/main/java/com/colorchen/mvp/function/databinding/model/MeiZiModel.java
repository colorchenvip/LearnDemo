package com.colorchen.mvp.function.databinding.model;

import java.util.List;

/**
 * des
 * results: [
 * {
 * _id: "57bc5238421aa9125fa3ed70",
 * createdAt: "2016-08-23T21:40:08.159Z",
 * desc: "8.24",
 * publishedAt: "2016-08-24T11:38:48.733Z",
 * source: "chrome",
 * type: "福利",
 * url: "http://ww3.sinaimg.cn/large/610dc034jw1f740f701gqj20u011hgo9.jpg",
 * used: true,
 * who: "daimajia"
 * },
 * ]
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public class MeiZiModel {
    private String error;
    private List<Result> results;
    public static class Result{
        private String _id;
        private String desc;
        private String publishedAt;
        private String createdAt;
        private String source;
        private String url;
        private String used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }

    public List<Result> getResults() {
        return results;
    }
}
