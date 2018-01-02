package com.xun.liverobot.mvp.contract;

import com.xun.liverobot.base.BasePresenter;
import com.xun.liverobot.base.BaseView;
import com.xun.liverobot.mvp.model.bean.PatrolLogBean;

import java.util.List;

/**
 * Created by xunwang on 2018/1/1.
 */

public interface PatrolLogContract {
    interface View extends BaseView<PatrolLogContract.Presenter> {
        void onLogs(List<PatrolLogBean> list);
    }

    interface Presenter extends BasePresenter {
        void requestLogs();
    }
}
