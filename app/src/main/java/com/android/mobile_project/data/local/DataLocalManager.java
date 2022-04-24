package com.android.mobile_project.data.local;

import android.content.Context;

import com.android.mobile_project.data.local.pref.MySharedPreferences;

public class DataLocalManager {

    private static final String USER_NAME = "PREF_USER_NAME";
    private static final String USER_ID = "PREF_USER_ID";

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

    public static void setUserName(String value){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(USER_NAME, value);
    }

    public static String getUserName(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(USER_NAME);
    }

    public static void setUserId(Long value){
        DataLocalManager.mySharedPreferences.putLongValue(USER_ID, value);
    }

    public static Long getUserId(){
        return DataLocalManager.mySharedPreferences.getLongValue(USER_ID);
    }

}
