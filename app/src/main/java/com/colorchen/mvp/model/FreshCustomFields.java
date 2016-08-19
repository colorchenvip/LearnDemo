package com.colorchen.mvp.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by yons on 16/2/16.
 */
public class FreshCustomFields extends RealmObject {
    private RealmList<RealmString> thumb_c;

    public RealmList<RealmString> getThumb_c() {
        return thumb_c;
    }

    public void setThumb_c(RealmList<RealmString> thumb_c) {
        this.thumb_c = thumb_c;
    }
}