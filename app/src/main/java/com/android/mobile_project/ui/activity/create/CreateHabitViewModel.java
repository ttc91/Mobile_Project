package com.android.mobile_project.ui.activity.create;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.ui.activity.create.service.InitService;

public class CreateHabitViewModel extends ViewModel {

    protected boolean selectSunDate = false;
    protected boolean selectMonDate = false;
    protected boolean selectTueDate = false;
    protected boolean selectWedDate = false;
    protected boolean selectThuDate = false;
    protected boolean selectFriDate = false;
    protected boolean selectSatDate = false;

    protected static boolean selectAnytime = false;
    protected static boolean selectNight = false;
    protected static boolean selectMorning = false;
    protected static boolean selectAfternoon = false;

    InitService initService;

}
