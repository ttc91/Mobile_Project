package com.android.mobile_project.ui.activity.create;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.ui.activity.create.service.InitService;

public class CreateHabitViewModel extends ViewModel {

    protected static boolean selectSunDate = false;
    protected static boolean selectMonDate = false;
    protected static boolean selectTueDate = false;
    protected static boolean selectWedDate = false;
    protected static boolean selectThuDate = false;
    protected static boolean selectFriDate = false;
    protected static boolean selectSatDate = false;

    protected static boolean selectAnytime = false;
    protected static boolean selectNight = false;
    protected static boolean selectMorning = false;
    protected static boolean selectAfternoon = false;

    InitService initService;

}
