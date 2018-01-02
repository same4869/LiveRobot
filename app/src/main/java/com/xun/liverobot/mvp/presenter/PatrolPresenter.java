package com.xun.liverobot.mvp.presenter;

import com.xun.liverobot.mvp.contract.PatrolContract;
import com.xun.liverobot.mvp.model.PatrolModel;

/**
 * Created by xunwang on 2017/12/28.
 */

public class PatrolPresenter implements PatrolContract.Presenter {
    private PatrolContract.View mView;
    private PatrolModel mModel = new PatrolModel();

    public PatrolPresenter(PatrolContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {
        requestDataFromDB();
    }

    @Override
    public void requestDataFromDB() {
        if (mView != null) {
            mView.setData(mModel.loadData());
        }
    }
}
