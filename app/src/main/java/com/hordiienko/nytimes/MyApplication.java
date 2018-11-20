package com.hordiienko.nytimes;

import android.app.Application;

import com.hordiienko.nytimes.sqlite.SqliteController;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SqliteController.init(this);
    }
}
