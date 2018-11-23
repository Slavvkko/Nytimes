package com.hordiienko.nytimes;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

//        SqliteController.init(this);
        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder().name("articles.realm").build();
        Realm.setDefaultConfiguration(configuration);
    }
}
