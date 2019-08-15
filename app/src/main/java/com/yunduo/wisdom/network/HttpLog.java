package com.yunduo.wisdom.network;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by parade岁月 on 2019/8/15 15:08
 */
public class HttpLog implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(@NotNull String s) {
        Log.d("HttpLogInfo", s);
    }
}
