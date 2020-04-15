package com.sanzhi.work.network.interceptor;

import com.sanzhi.work.base.BaseApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***
 *author: parade岁月
 *date: 2020/1/12 15:44
 *description： 全局添加请求头
 */
public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = BaseApplication.app.getDataCenter().getToken();
        Request request = original.newBuilder()
                .header("Token", token == null ? "" : token)
                .header("Client", "ANDROID")
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }
}
