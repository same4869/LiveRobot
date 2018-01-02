package com.xun.liverobot.mvp.model.bean;

import io.realm.RealmObject;

/**
 * Created by xunwang on 2018/1/1.
 */

public class PatrolLogBean extends RealmObject {
    private String date;
    private String logStr;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogStr() {
        return logStr;
    }

    public void setLogStr(String logStr) {
        this.logStr = logStr;
    }
}
