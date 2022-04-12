package com.android.mobile_project.data.local;

import android.content.Context;

import com.android.mobile_project.data.local.pref.MySharedPreferences;

public class DataLocalManager {

    private static final String USER_NAME = "PREF_USER_NAME";

    private static DataLocalManager instance;

    private static MySharedPreferences mySharedPreferences;

    public static void init(Context context){

        instance = new DataLocalManager();
        mySharedPreferences = new MySharedPreferences(context);

    }

    private DataLocalManager () {}

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }

        return instance;
    }

    public static void setUser(String value){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(USER_NAME, value);
    }

    public static void getUser(){
        DataLocalManager.getInstance().mySharedPreferences.getStringValue(USER_NAME);
    }

}
