package com.androidpotato.page;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidpotato.page.test.TestTemplateActivity;
import com.androidpotato.common.CommonTestCallback;
import com.androidpotato.utils.HashMapToFileUtil;
import com.davidzhou.library.util.ULog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Random;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FileTestActivity extends TestTemplateActivity implements CommonTestCallback {
    private static final String TAG = "FileTestActivity";
    private String[] KEYS = new String[]{
            "audio",
            "media"
    };
    private HashMap<String, Boolean> mHashMap;
    private HashMapToFileUtil mHashMapToFileUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCommonTestCallback(this);
        mHashMapToFileUtil = HashMapToFileUtil.getInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onBtnClicked(BtnIndex position) {
        switch (position) {
            case BTN_INDEX_1:
                mHashMap = mHashMapToFileUtil.getHashMap(this);
                ULog.i(TAG, "get hashMap from file: " + mHashMap.toString());
                break;
            case BTN_INDEX_2:
                Random random = new Random();
                String key = KEYS[random.nextInt(2)];
                boolean value = random.nextBoolean();
                ULog.i(TAG, "key: " + key + ", value: " + value);
                saveToHashMap(key, value);
                ULog.i(TAG, "get hashMap from ram: " + mHashMap.toString());
                break;
            case BTN_INDEX_3:
                mHashMapToFileUtil.writeToFile(this, mHashMap);
                break;
            case BTN_INDEX_4:
                test();
                break;
        }
    }

    private void saveToHashMap(String key, boolean value) {
        mHashMap.put(key, value);
    }

    private void test() {
        String[] names = {"123", "132123", "14321323", "1123", "43123", "112323"};
        FlowableSubscriber<String> subscriber = new FlowableSubscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                ULog.i(TAG, "onSubscribe: " + s);
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                ULog.i(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable t) {
                ULog.e(TAG, "onError: " + t.getMessage());

            }

            @Override
            public void onComplete() {
                ULog.i(TAG, "onComplete");
            }
        };
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                emitter.onNext("123");
                emitter.onNext("321");
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
        flowable.subscribe(subscriber);
        Disposable disposable = flowable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                ULog.i(TAG, "accept: s: " + s);
            }
        });

        Observer<Integer> subscriber1 = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                ULog.i(TAG, "onNext in subscriber1: " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable<Integer> a = Observable.just(1, 2, 3, 4, 5, 6);
        a.subscribe(subscriber1);

    }
}
