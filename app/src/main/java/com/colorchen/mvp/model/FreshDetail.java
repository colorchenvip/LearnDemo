package com.colorchen.mvp.model;

import io.realm.RealmObject;

/**
 * Fresh detail post.
 */
public class FreshDetail extends RealmObject {

    private int id;
    private String content;

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}