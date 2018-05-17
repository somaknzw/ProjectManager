package com.lifeistech.android.projectmanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmObject;

public class Project extends RealmObject {

    public String title;
    public String comment;
    public float satisfaction;
    public String updateDate;
    public int achievement;
    public String logdate;
    public String dayid;
    public String fb_title;

}