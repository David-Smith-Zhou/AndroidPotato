package com.androidpotato.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.androidpotato.aidl.Book;
import com.androidpotato.aidl.BookManager;
import com.davidzhou.library.util.ULog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2017/7/18 0018.
 */

public class AIDLService extends Service {
    private static final String TAG = "AIDLService";
    private List<Book> books;

    @Override
    public void onCreate() {
        super.onCreate();
        books = new ArrayList<>();
        Book book = new Book();
        book.setName("神探夏洛克");
        book.setPrice(10);
        books.add(book);
    }

    private final BookManager.Stub bookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                ULog.i(TAG, "invoking getBooks() method, now the list is : " + books.toString());
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    books = new ArrayList<>();
                }
                if (book == null) {
                    ULog.e(TAG, "Book is null in In");
                    book = new Book();
                }
                book.setPrice(2333);
                if (!books.contains(book)) {
                    books.add(book);
                }
                ULog.i(TAG, "invoking addBooks() method, now the list is : " + books.toString());
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ULog.e(getClass().getSimpleName(), String.format("on bind, intent = %s", intent.toString()));
        return bookManager;
    }
}
