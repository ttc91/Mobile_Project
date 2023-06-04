package com.android.mobile_project.data.local;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.mobile_project.data.local.pref.MySharedPreferences;

public class DataLocalManager {

    private static final String USER_TOKEN = "PREF_USER_TOKEN";
    private static final String USER_NAME = "PREF_USER_NAME";
    private static final String USER_ID = "PREF_USER_ID";
    private static final String LONGEST_STEAK = "PREF_LONGEST_STEAK";
    private static final String USER_STATE_CHANGE_DATA = "PREF_USER_STATE_CHANGE_DATA";
    private static final String COUNT_TO_SYNCHRONIZE_SERVER = "PREF_COUNT_TO_SYNCHRONIZE_SERVER";
    private static final String LOGIN_STATE = "PREF_LOGIN_STATE";
    private static final String COUNTER_STEP_VALUE = "PREF_COUNTER_STEP_VALUE";
    private static final String COUNTER_STEP_PER_DAY_VALUE = "PREF_COUNTER_STEP_PER_DAY_VALUE";

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

    public void setToken(String value){
        mySharedPreferences.putStringValue(USER_TOKEN, value);
    }

    public String getToken(){
        return mySharedPreferences.getStringValue(USER_TOKEN);
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

    public void setLongestTeak(Long value){
        DataLocalManager.mySharedPreferences.putLongValue(LONGEST_STEAK, value);
    }

    public Long getLongestTeak(){
        return DataLocalManager.mySharedPreferences.getLongValue(LONGEST_STEAK);
    }

    public void setCountToSynchronizeServer(Long value){
        DataLocalManager.mySharedPreferences.putLongValue(COUNT_TO_SYNCHRONIZE_SERVER, value);
    }

    public Long getCountToSynchronizeServer(){
        return DataLocalManager.mySharedPreferences.getLongValue(COUNT_TO_SYNCHRONIZE_SERVER);
    }

    public void setUserStateChangeData(String value){
        DataLocalManager.mySharedPreferences.putStringValue(USER_STATE_CHANGE_DATA, value);
    }

    public String getUserStateChangeData(){
        return DataLocalManager.mySharedPreferences.getStringValue(USER_STATE_CHANGE_DATA);
    }

    public void setLoginState(String value){
        DataLocalManager.mySharedPreferences.putStringValue(LOGIN_STATE, value);
    }

    public String getLoginState(){
        return DataLocalManager.mySharedPreferences.getStringValue(LOGIN_STATE);
    }

    public void setCounterStepValue(Integer value){
        DataLocalManager.mySharedPreferences.putIntValue(COUNTER_STEP_VALUE, value);
    }

    public Integer getCounterStepValue(){
        return DataLocalManager.mySharedPreferences.getIntValue(COUNTER_STEP_VALUE);
    }

    public void setCounterStepPerDayValue(){
        if(DataLocalManager.mySharedPreferences.getLongValue(COUNTER_STEP_PER_DAY_VALUE) == null){
            DataLocalManager.mySharedPreferences.putLongValue(COUNTER_STEP_PER_DAY_VALUE, 0L);
        }else {
            Long value = DataLocalManager.mySharedPreferences.getLongValue(COUNTER_STEP_PER_DAY_VALUE) + 1L;
            DataLocalManager.mySharedPreferences.putLongValue(COUNTER_STEP_PER_DAY_VALUE, value);
        }
    }

    public void resetCounterStepPerDayValue(){
            DataLocalManager.mySharedPreferences.putLongValue(COUNTER_STEP_PER_DAY_VALUE, 0L);
    }

    public Long getCounterStepPerDayValue(){
        return DataLocalManager.mySharedPreferences.getLongValue(COUNTER_STEP_PER_DAY_VALUE);
    }

}
