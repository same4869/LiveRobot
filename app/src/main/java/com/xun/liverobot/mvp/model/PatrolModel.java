package com.xun.liverobot.mvp.model;

import com.xun.liverobot.mvp.model.bean.PatrolBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xunwang on 2017/12/28.
 */

public class PatrolModel {
    public List<PatrolBean> loadData() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<PatrolBean> dogs = mRealm.where(PatrolBean.class).findAll();
        return mRealm.copyFromRealm(dogs);
    }
}
