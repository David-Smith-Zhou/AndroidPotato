package com.androidpotato.network;

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
 * ClassName ApiService
 * Function. 基础通信相关模块
 *
 * @author David
 * @version V1.0
 * @date 2018/10/31
 */
public class ApiService {
    private static final String TAG = "ApiServiceMoudle";
    private static final String HOST_TEST = "osiovdev.changan.com.cn/";
    private static final String HOST_PRE_PRODUCT = "osiovprep.changan.com.cn/";
    private static final String HOST_PRODUCT = "osiov.changan.com.cn/";

    public enum Scheme {
        NONE(""),
        HTTP("http://"),
        HTTPS("https://");

        private String scheme;

        Scheme(String scheme) {
            this.scheme = scheme;
        }

        public String getScheme() {
            return scheme;
        }
    }

    public ApiService() {
    }

    /**
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new AddCookiesInterceptor())
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     *
     * @param url
     * @param okHttpClient
     * @return
     */
    public static Retrofit getRetrofit(String url, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     *
     * @return
     */
    public static String getHOST() {
        return HOST_TEST;
    }

    /**
     *
     */
    private static class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("User-Agent", "-")
                    .addHeader("Charset", "UTF-8")
                    .build();
            return chain.proceed(request);
        }
    }
}
