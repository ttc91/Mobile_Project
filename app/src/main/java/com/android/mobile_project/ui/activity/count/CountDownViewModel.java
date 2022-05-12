package com.android.mobile_project.ui.activity.count;

import android.os.CountDownTimer;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.ui.activity.count.service.InitService;
import com.android.mobile_project.ui.activity.count.service.TimerService;

public class CountDownViewModel extends ViewModel {

    public HabitEntity habitEntity;

    public float percent = 0;

    protected Long START_TIME_IN_MILLIS;

    protected InitService initService;
    protected TimerService timerService;

    protected CountDownTimer mCountDownTimer;
    protected boolean mTimerRunning = false;

}
