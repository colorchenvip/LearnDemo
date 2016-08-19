package com.colorchen.mvp.net;


import com.colorchen.mvp.model.Image;
import com.colorchen.utils.Constants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Deals with cache, data
 */
public class DB {

    public static void saveOrUpdate(Realm realm, RealmObject realmObject) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> void saveList(Realm realm, List<T> realmObjects) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObjects);
        realm.commitTransaction();
    }

    public static void save(Realm realm, RealmObject realmObject) {
        realm.beginTransaction();
        realm.copyToRealm(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> T getById(Realm realm, int id, Class<T> realmObjectClass) {
        return realm.where(realmObjectClass).equalTo("id", id).findFirst();
    }

    public static <T extends RealmObject> T getByUrl(Realm realm, String url, Class<T> realmObjectClass) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        return realm.where(realmObjectClass).equalTo(Constants.URL, url).findFirst();
    }

    public static <T extends RealmObject> boolean isUrlExisted(Realm realm, String url, Class<T> realmObjectClass) {
        return getByUrl(realm, url, realmObjectClass) != null;
    }


    public static <T extends RealmObject> RealmResults<T> findAll(Realm realm, Class<T> realmObjectClass) {
        return realm.where(realmObjectClass).findAll();
    }

    public static <T extends RealmObject> RealmResults<T> findAllDateSorted(Realm realm, Class<T> realmObjectClass) {
        RealmResults<T> results = findAll(realm, realmObjectClass);
        results.sort(Constants.DATE, Sort.DESCENDING);
        return results;
    }

    public static <T extends RealmObject> void clear(Realm realm, Class<T> realmObjectClass) {
        realm.beginTransaction();
        findAll(realm, realmObjectClass).clear();
        realm.commitTransaction();
    }

    public static RealmResults<Image> getImages(Realm realm, int type) {
        RealmResults<Image> results = realm.where(Image.class).equalTo("type", type).findAll();
        results.sort("publishedAt", Sort.DESCENDING);
        return results;
    }
}
