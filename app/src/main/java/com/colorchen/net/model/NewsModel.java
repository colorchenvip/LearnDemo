package com.colorchen.net.model;

import java.io.Serializable;
import java.util.List;

public class NewsModel implements Serializable {

    private static final long serialVersionUID = 6753210234564872868L;

    public PageBean pagebean;
    public int ret_code;

    public static class PageBean implements Serializable {
        private static final long serialVersionUID = -7782857008135312889L;

        public int allNum;
        public int allPages;
        public int currentPage;
        public int maxResult;
        public List<ContentList> contentlist;
    }

    public static class ContentList implements Serializable {
        private static final long serialVersionUID = -7041713994407568209L;

        public String channelId;
        public String channelName;
        public String content;
        public String desc;
        public String html;
        public String link;
        public String nid;
        public String pubDate;
        public int sentiment_display;
        public String source;
        public String title;
        public List<NewsImage> imageurls;
    }

    public static class NewsImage implements Serializable {
        private static final long serialVersionUID = -7441255403568397400L;

        public int width;
        public int height;
        public String url;
    }
}