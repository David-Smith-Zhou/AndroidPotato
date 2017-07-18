package com.androidpotato.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David on 2017/7/18 0018.
 */

public class Book implements Parcelable {
    public Book() {

    }
    public Book(Parcel in) {
        this.name = in.readString();
        this.price = in.readInt();
    }
    private String name;
    private int price;

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.price);
    }

    public void readFromParcel(Parcel dest) {
        this.name = dest.readString();
        this.price = dest.readInt();
    }
    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
