package com.androidpotato.network;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * ClassName Api
 * Function. 通信API
 *
 * @author David
 * @version V1.0
 * @date 2018/11/2
 */
public interface Api {
    @POST
    @FormUrlEncoded
    Observable<Response<Object>> post(@Url String url, @FieldMap Map<String, Object> map);

    @GET
    Observable<Response<Object>> get(@Url String url, @QueryMap Map<String, Object> map);
}
