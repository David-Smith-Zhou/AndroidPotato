package com.androidpotato.dto;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Person {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String birthday;
    private boolean male;

    @Generated(hash = 1844557989)
    public Person(Long id, String name, String birthday, boolean male) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.male = male;
    }

    @Generated(hash = 1024547259)
    public Person() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getName() {
        return name;
    }

    public boolean getMale() {
        return this.male;
    }
}
