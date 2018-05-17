package com.lifeistech.android.projectmanager;

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