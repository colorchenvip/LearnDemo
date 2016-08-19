package com.colorchen.mvp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Post item contains title and url.
 */
public class HItem extends RealmObject {
    private String date;
    @PrimaryKey
    private String url;
    private String title;

    public HItem(String date, String url, String title) {
        this.date = date;
        this.url = url;
        this.title = title;
    }

    public HItem() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
