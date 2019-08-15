package com.yunduo.wisdom.network;

import com.yunduo.wisdom.BuildConfig;
import com.yunduo.wisdom.base.BaseApplication;
import com.yunduo.wisdom.constant.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
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

    private static OkHttpClient getHttpClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(true);

        HttpLoggingInterceptor loggingInterceptor = null;
        if (BuildConfig.DEBUG){
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLog());
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                String token = BaseApplication.app.getDataCenter().getToken();//有疑问
                Request request = original.newBuilder()
                        .header("Token", token == null ? "" : token)
                        .header("Client", "ANDROID")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
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
