package com.androidpotato.network;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import retrofit2.Response;

/**
 * ClassName HttpResponse
 * Function. Http响应包装类
 * @author David
 * @version V1.0
 * @date 2018/10/31
 */
public abstract class HttpResponse<T> implements Subscriber<Response<T>>{
    public abstract void subscribe(Subscription s);
    public abstract void next(Response<T> t);
    public abstract void error(Throwable e);
    public abstract void complete();

    @Override
    public void onSubscribe(Subscription s) {
        subscribe(s);
    }

    @Override
    public void onNext(Response<T> t) {
        next(t);
    }

    @Override
    public void onError(Throwable t) {
        error(t);
    }

    @Override
    public void onComplete() {
        complete();
    }
}
