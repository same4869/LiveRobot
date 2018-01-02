package com.xun.liverobot.mvp.model;

import com.xun.liverobot.mvp.model.bean.PatrolLogBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xunwang on 2018/1/1.
 */

public class PatrolLogModel {
    public List<PatrolLogBean> loadData() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<PatrolLogBean> dogs = mRealm.where(PatrolLogBean.class).findAll();
        return mRealm.copyFromRealm(dogs);
    }
}
