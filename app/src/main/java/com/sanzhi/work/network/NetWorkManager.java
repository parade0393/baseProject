package com.sanzhi.work.network;

import com.sanzhi.work.BuildConfig;
import com.sanzhi.work.constant.Constant;
import com.sanzhi.work.network.interceptor.MyLoggingInterceptor;
import com.sanzhi.work.network.interceptor.RequestInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by parade岁月 on 2019/8/15 15:03
 */
public class NetWorkManager {

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 20;
    private static final int WRITE_TIMEOUT = 20;

    private static final RequestInterceptor requestInterceptor = new RequestInterceptor();
    private static final MyLoggingInterceptor loggingInterceptor = new MyLoggingInterceptor();

    /**
     * 获取httpclient
     * */
    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //设置超时时间
        httpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(true);//错误重连
        httpClient.addInterceptor(requestInterceptor);
        // Debug时才设置Log拦截器，才可以看到
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(loggingInterceptor);
        }
        return httpClient.build();
    }

    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constant.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getHttpClient())
                .build();
    }
}
