package com.xun.liverobot.mvp.presenter;

import com.xun.liverobot.mvp.contract.PatrolingContract;
import com.xun.liverobot.mvp.model.PatrolingModel;
import com.xun.liverobot.mvp.model.bean.EmailLoginBean;
import com.xun.liverobot.utils.StreamLogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xunwang on 2017/12/29.
 */

public class PatrolingPresenter implements PatrolingContract.Presenter {
    private PatrolingContract.View mView;
    private PatrolingModel mModel = new PatrolingModel();

    public PatrolingPresenter(PatrolingContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void requestLogin(String username, String password) {
        Observable<EmailLoginBean> observable = mModel.login(username, password);
        observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<EmailLoginBean>() {
            @Override
            public void accept(EmailLoginBean emailLoginBean) throws Exception {
                if (mView != null) {
                    mView.onLoginData(emailLoginBean);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                StreamLogUtil.putLog("登录接口有问题 throwable " + throwable.getMessage());
            }
        });
    }
}
