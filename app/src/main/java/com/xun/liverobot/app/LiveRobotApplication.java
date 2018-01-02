package com.xun.liverobot.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xunwang on 2017/12/28.
 */

public class LiveRobotApplication extends Application{
    protected static LiveRobotApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }

    public static LiveRobotApplication getInstance() {
        return instance;
    }

    public SharedPreferences getCommSharedPreferences(String tbl) {
        return getSharedPreferences(tbl, Context.MODE_PRIVATE);
    }
}
