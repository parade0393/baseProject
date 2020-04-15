package com.sanzhi.work.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by parade岁月 on 2019/8/15 15:42
 */
public interface ProjectApi {

    @GET("news/article/publish")
    Observable<Object> getNewsArticlePageListByItem(@Query("itemId") int itemId, @Query("pageIndex") int pageIndex, @Query("pageSize") int size, @Query("categoryId") Integer categoryId);
}
