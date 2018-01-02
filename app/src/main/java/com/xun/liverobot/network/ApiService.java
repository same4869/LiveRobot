package com.xun.liverobot.network;

import com.xun.liverobot.mvp.model.bean.EmailLoginBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by xunwang on 2017/12/20.
 */

public interface ApiService {
    @POST("login")
    Observable<EmailLoginBean> getEmailLogin(@Body RequestBody body);
}
