package com.kyana.projectmanager;

import android.app.Application;

import io.realm.Realm;

public class ProjectManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
    }
}