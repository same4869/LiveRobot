package com.xun.liverobot.mvp.presenter;

import com.xun.liverobot.mvp.contract.PatrolLogContract;
import com.xun.liverobot.mvp.model.PatrolLogModel;

/**
 * Created by xunwang on 2018/1/1.
 */

public class PatrolLogPresenter implements PatrolLogContract.Presenter {
    private PatrolLogContract.View mView;
    private PatrolLogModel mModel = new PatrolLogModel();

    public PatrolLogPresenter(PatrolLogContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void requestLogs() {
        if (mView != null) {
            mView.onLogs(mModel.loadData());
        }
    }
}
