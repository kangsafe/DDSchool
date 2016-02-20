package com.ddschool.service;

import com.ddschool.bean.BwToken;
import com.ddschool.networks.UpdateInfo;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/2/14.
 */
public interface ApiService {
    String ENDPOINT = "http://schoolapi2.wo-ish.com";
    // 获取个人信息
    @POST("/oauth/token")
    Observable<BwToken> getToken(
            @Query("appid") String appId,
            @Query("appsecret") String appSecret,
            @Query("grant_type") String grantType);
}
