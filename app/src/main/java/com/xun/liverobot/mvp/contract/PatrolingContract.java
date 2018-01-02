package com.xun.liverobot.mvp.contract;

import com.xun.liverobot.base.BasePresenter;
import com.xun.liverobot.base.BaseView;
import com.xun.liverobot.mvp.model.bean.EmailLoginBean;

/**
 * Created by xunwang on 2017/12/29.
 */

public interface PatrolingContract {
    interface View extends BaseView<PatrolingContract.Presenter> {
        void onLoginData(EmailLoginBean bean);
    }

    interface Presenter extends BasePresenter {
        void requestLogin(String username, String password);
    }
}
