package com.androidpotato.page;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidpotato.dto.Person;
import com.androidpotato.greendao.DaoMaster;
import com.androidpotato.greendao.DaoSession;
import com.androidpotato.page.test.TestTemplateActivity;
import com.androidpotato.common.CommonTestCallback;
import com.androidpotato.utils.TimeUtil;
import com.davidzhou.library.util.ULog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FileTestActivity extends TestTemplateActivity implements CommonTestCallback {
    private static final String TAG = "FileTestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCommonTestCallback(this);
        ULog.i(TAG, "currDate: " + TimeUtil.currDateStr());
    }

    @Override
    public void start() {

    }

    @Override
    public void resume() {
        test();
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
                test();
                break;
            case BTN_INDEX_2:
                debug();
                break;
            case BTN_INDEX_3:
                break;
            case BTN_INDEX_4:
                break;
        }
    }
    private void debug() {
        Person person = new Person();
        person.setBirthday(TimeUtil.currDateStr());
        person.setMale(true);
        person.setName("David");
        DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(this, "db_name");
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        daoSession.getPersonDao().insert(person);
        ULog.i(TAG, "persons: " + daoSession.getPersonDao().queryBuilder().list().size());
    }

    private void test() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    emitter.onNext("123");
                    emitter.onNext("456");
                    emitter.onNext("789");
                    emitter.onNext("123");
                    emitter.onNext("456");
                    emitter.onNext("789");
                    emitter.onNext("123");
                    emitter.onNext("456");
                    emitter.onNext("789");
                    emitter.onNext("123");
                    emitter.onNext("456");
                    emitter.onNext("789");
                    emitter.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return Integer.valueOf(s);
                    }
                })
                // 在io操作线程中执行
                .subscribeOn(Schedulers.io())
                // 在主线程中订阅观察结果
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer s) throws Exception {
                        ULog.i(TAG, "doOnNext get: " + s);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    private Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Integer s) {
                        ULog.i(TAG, "onNext get: " + s);
//                        if ("456".equals(s)) {
//                            disposable.dispose();
//                            ULog.i(TAG, "dispose");
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ULog.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        ULog.i(TAG, "onComplete");
                    }
                });
    }
}
