package com.xun.liverobot.mvp.contract;

import com.xun.liverobot.base.BasePresenter;
import com.xun.liverobot.base.BaseView;
import com.xun.liverobot.mvp.model.bean.PatrolBean;

import java.util.List;

/**
 * Created by xunwang on 2017/12/28.
 */

public interface PatrolContract {
    interface View extends BaseView<Presenter> {
        void setData(List<PatrolBean> bean);
    }

    interface Presenter extends BasePresenter {
        void requestDataFromDB();
    }
}
