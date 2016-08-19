package com.colorchen.mvp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yons on 16/3/1.
 */
public class HDetail extends RealmObject {
    @PrimaryKey
    private String url;
    private RealmList<Image> images;

    public HDetail(String url, RealmList<Image> images) {
        this.url = url;
        this.images = images;
    }

    public HDetail() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }
}
