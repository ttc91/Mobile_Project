package com.android.mobile_project.ui.activity.setting;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.ui.activity.setting.service.InitService;

import java.util.ArrayList;
import java.util.List;

public class HabitSettingViewModel extends ViewModel {

    protected InitService initService;

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

    protected HabitEntity habitEntity = new HabitEntity();
    protected List<HabitInWeekEntity> habitInWeekEntity = new ArrayList<>();

}
