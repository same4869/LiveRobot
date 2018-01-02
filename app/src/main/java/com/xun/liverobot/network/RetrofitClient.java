package com.xun.liverobot.network;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xun.liverobot.sp.CommSetting;
import com.xun.liverobot.utils.AppUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xunwang on 2017/12/20.
 */

public class RetrofitClient {
    private File httpCacheDirectory;
    private Context mContext;
    private Cache cache;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private long DEFAULT_TIMEOUT = 20;
    private static RetrofitClient instance;

    private RetrofitClient(Context context, String baseUrl) {
        this.mContext = context;
        //缓存地址
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "app_cache");
        }
        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //okhttp创建了
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(cache)
                .addInterceptor(new GlobalInterceptor())
                .addInterceptor(new CacheInterceptor(context))
                .addNetworkInterceptor(new CacheInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        //retrofit创建了
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public static RetrofitClient getInstance(Context context,String baseUrl){
        if (instance == null) {
            synchronized(RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient(context,baseUrl);
                }
            }
        }
        return instance;
    }

    public <T> T create(Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    static final class GlobalInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            String token = CommSetting.getToken();
            if (token != null) {
                builder.addHeader("SESSIONID", token);
            }
            builder.addHeader("WAWA-VERSION", "-1");
            builder.addHeader("WAWA-BUILD", "200");//后面要改  后端没时间临时加的
            builder.addHeader("Content-Type", "application/json");
            builder.addHeader("WAWA-PLATEFORM", "android");
            builder.addHeader("WAWA-DEVICEID", AppUtil.getDeviceId());
            builder.addHeader("channel", "robot");
            Request request = builder.build();

            Response response = chain.proceed(request);

            return response;
        }
    }
}
