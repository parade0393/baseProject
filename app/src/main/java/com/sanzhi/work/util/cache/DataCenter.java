package com.sanzhi.work.util.cache;

import android.content.Context;

/**
 * Created by jerry on 2018/2/22.
 */

public class DataCenter {
    private Context context;
    private static DataCenter instance;
    ACache cache = null;
    /** 用户token */
    private String token;

    public static DataCenter getDataCenter(Context context) {
        if (instance == null) instance = new DataCenter(context);
        return instance;
    }

    private DataCenter(Context context) {
        this.context = context;
        cache = ACache.get(context);
        token = cache.getAsString("token");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void persistSession() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cache.put("token", token);
            }
        }).start();
    }


    public void clearData() {
        token = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                cache.remove("token");
            }
        }).start();
    }
    //清除缓存
    public void clearCache() {
        token = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
               cache.clear();
            }
        }).start();
    }
}
