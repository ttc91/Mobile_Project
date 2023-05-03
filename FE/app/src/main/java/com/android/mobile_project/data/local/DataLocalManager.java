package com.android.mobile_project.data.local;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.mobile_project.data.local.pref.MySharedPreferences;

public class DataLocalManager {

    private static final String USER_NAME = "PREF_USER_NAME";
    private static final String USER_ID = "PREF_USER_ID";

    private static DataLocalManager instance;

    @SuppressLint("StaticFieldLeak")
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

    public void setUserName(String value){
        mySharedPreferences.putStringValue(USER_NAME, value);
    }

    public String getUserName(){
        return mySharedPreferences.getStringValue(USER_NAME);
    }

    public void setUserId(Long value){
        DataLocalManager.mySharedPreferences.putLongValue(USER_ID, value);
    }

    public Long getUserId(){
        return DataLocalManager.mySharedPreferences.getLongValue(USER_ID);
    }

}
