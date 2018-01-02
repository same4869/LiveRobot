package com.xun.liverobot.network;

import android.content.Context;


import com.xun.liverobot.utils.LogUtil;
import com.xun.liverobot.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xunwang on 2017/12/20.
 */

public class CacheInterceptor implements Interceptor {
    private Context context;

    public CacheInterceptor(Context context){
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtils.isNetConneted(context)) {
            Response response = chain.proceed(request);
            // read from cache for 60 s
            int maxAge = 60;
            String cacheControl = request.cacheControl().toString();
            LogUtil.e("CacheInterceptor", "6s load cahe" + cacheControl);
            return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control").header("Cache-Control", "public, max-age=" + maxAge).build();
        } else {
            LogUtil.e("CacheInterceptor", " no network load cahe");
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            Response response = chain.proceed(request);
            //set cahe times is 3 days
            int maxStale = 60 * 60 * 24 * 3;
            return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control").header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
        }
    }
}
