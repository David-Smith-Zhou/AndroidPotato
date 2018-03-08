package com.davidzhou.library.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DavidSmith on 2016/6/9 0009.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "DatabaseHelper";
    public static final String KEY_ID = "_id";
    public static final String KEY_GOLD_HOARD_NAME_COLUMN =
            "GOLD_HOARD_NAME_COLUMN";
    public static final String KEY_GOLD_HOARD_ACCESSIBLE_COLUMN =
            "GOLD_HOARD_ACCESSIBLE_COLUMN";
    public static final String KEY_GOLD_HOARDED_COLUMN =
            "GOLD_HOARDED_COLUMN";

    private static final String DATABASE_NAME = "mDatabase.db";
    private static final String DATABASE_TABLE = "GoldHoards";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREAT = "creat table" +
            DATABASE_TABLE + "(" + KEY_ID +
            " interger primary key autoincrement, " +
            KEY_GOLD_HOARD_NAME_COLUMN + " text not null, " +
            KEY_GOLD_HOARDED_COLUMN + " float, " +
            KEY_GOLD_HOARD_ACCESSIBLE_COLUMN + " integer);";
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREAT);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading from version " + oldVersion + " to " + newVersion +
        ", which will destroy all old data");
//        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
//        onCreate(db);

    }
}
