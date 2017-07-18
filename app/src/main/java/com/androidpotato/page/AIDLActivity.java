package com.androidpotato.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.androidpotato.R;
import com.androidpotato.aidl.Book;
import com.androidpotato.aidl.BookManager;
import com.androidpotato.mylibrary.util.UtilLog;
import com.androidpotato.mylibrary.util.UtilToast;

import java.util.List;

/**
 * Created by David on 2017/7/18 0018.
 */

public class AIDLActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AIDLActivity";
    private BookManager manager;
    private boolean isBonded = false;
    private List<Book> books;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_aidl);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UtilLog.e(TAG, "service connected");
            manager = BookManager.Stub.asInterface(service);
            isBonded = true;
            if (manager != null) {
                getBooks();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            UtilLog.e(TAG, "service disconnected");
            isBonded = false;
        }
    };

    private void addBook() {
        if (!isBonded) {
            attempToBindService();
            UtilToast.ToastShort(this, "当前与服务未连接，正在尝试连接，请稍后再试");
            return;
        }
        if (manager != null) {
            Book book = new Book();
            book.setName("十万个不为什么");
            book.setPrice(20);
            try {
                manager.addBook(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void getBooks() {
        try {
            books = manager.getBooks();
            UtilLog.i(TAG, "get Books: " + books.toString());
            for (Book each : books) {
                UtilLog.i(TAG, "name: " + each.getName() + ", price: " + each.getPrice());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void attempToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.androidpotato.aidl");
        intent.setPackage("com.androidpotato");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aidl_bind:
                if (!isBonded) {
                    attempToBindService();
                }
                break;
            case R.id.aidl_add:
                addBook();
                break;
            case R.id.aidl_get:
                getBooks();
                break;
            case R.id.aidl_debug:

                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBonded) {
            unbindService(serviceConnection);
            isBonded = false;
        }
    }
}
