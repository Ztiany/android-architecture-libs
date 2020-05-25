package com.android.sdk.social.wechat;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-07 14:50
 */
class WXApiFactory {

    static ServiceApi createWXApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com/sns/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        return retrofit.create(ServiceApi.class);
    }

    interface ServiceApi {

        //获取openid、accessToken值用于后期操作
        @GET("oauth2/access_token")
        Observable<WXToken> getAccessToken(@Query("appid") String appId, @Query("secret") String appSecret, @Query("code") String code, @Query("grant_type") String granType);

        //检验授权凭证（access_token）是否有效
        @GET("auth")
        Observable<AuthResult> validateToken(@Query("access_token") String access_token, @Query("openid") String openid);

        //获取用户个人信息
        @GET("userinfo")
        Observable<WXUser> getWeChatUser(@Query("access_token") String access_token, @Query("openid") String openid);

    }

}