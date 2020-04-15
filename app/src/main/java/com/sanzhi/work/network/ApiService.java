package com.sanzhi.work.network;

import com.sanzhi.work.api.ProjectApi;

import retrofit2.Retrofit;

/**
 * Created by parade岁月 on 2019/8/15 15:41
 */
public class ApiService {

    private static ProjectApi projectApi;

    public static ProjectApi getProjectApi(){
        if (projectApi == null){
            Retrofit retrofit = NetWorkManager.getRetrofit();
            projectApi = retrofit.create(ProjectApi.class);
        }

        return projectApi;
    }
}
