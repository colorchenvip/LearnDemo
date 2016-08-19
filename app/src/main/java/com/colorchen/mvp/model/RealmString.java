package com.colorchen.mvp.model;

import io.realm.RealmObject;

/**
 * To make realmObject 'support' List<String>
 */
public class RealmString extends RealmObject{

    private String val;

    public RealmString() {
    }

    public RealmString(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
