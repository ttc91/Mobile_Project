package com.android.mobile_project.ui.activity.setting;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.model.db.RemainderEntity;
import com.android.mobile_project.ui.activity.setting.adapter.RemainderAdapter;
import com.android.mobile_project.ui.activity.setting.service.InitService;
import com.android.mobile_project.ui.dialog.RemainderDialog;

import java.util.ArrayList;
import java.util.List;

public class HabitSettingViewModel extends ViewModel {

    protected InitService initService;

    protected boolean selectSunDate = false;
    protected boolean selectMonDate = false;
    protected boolean selectTueDate = false;
    protected boolean selectWedDate = false;
    protected boolean selectThuDate = false;
    protected boolean selectFriDate = false;
    protected boolean selectSatDate = false;

    protected boolean selectAnytime = false;
    protected boolean selectNight = false;
    protected boolean selectMorning = false;
    protected boolean selectAfternoon = false;

    protected HabitEntity habitEntity = new HabitEntity();
    protected List<HabitInWeekEntity> habitInWeekEntity = new ArrayList<>();
    protected List<RemainderEntity> remainderEntityList = new ArrayList<>();

}
