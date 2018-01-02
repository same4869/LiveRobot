package com.xun.liverobot.mvp.model;

import com.xun.liverobot.app.LiveRobotApplication;
import com.xun.liverobot.config.Constants;
import com.xun.liverobot.mvp.model.bean.EmailLoginBean;
import com.xun.liverobot.network.ApiService;
import com.xun.liverobot.network.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by xunwang on 2017/12/29.
 */

public class PatrolingModel {
    public Observable<EmailLoginBean> login(String email, String passwd) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", passwd);
        params.put("type", "2");
        RequestBody body = RequestBody.create(null, new JSONObject(params).toString());
        RetrofitClient retrofitClient = RetrofitClient.getInstance(LiveRobotApplication.getInstance(), Constants.BASE_URL);
        ApiService apiService = retrofitClient.create(ApiService.class);
        return apiService.getEmailLogin(body);
    }
}
