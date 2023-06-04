package com.android.mobile_project;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.receiver.system.TimeTickReceiver;
import com.android.mobile_project.utils.alarm.AlarmUtil;
import com.android.mobile_project.utils.dagger.ApplicationGraph;
import com.android.mobile_project.utils.dagger.DaggerApplicationGraph;
import com.android.mobile_project.utils.dagger.component.provider.AutoInsertServiceComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CountDownComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CounterStepComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CreateHabitComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CreateHistoryReceiverComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.DayChangedReceiverComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.HabitSettingComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.InputComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.MainComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.RebootReceiverComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.TimeTickReceiverComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.CounterStepComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.CreateHistoryReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.DayChangedReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.RebootReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.TimeTickReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.service.AutoInsertServiceComponent;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;
import com.android.mobile_project.utils.dagger.module.ApplicationModule;
import com.android.mobile_project.utils.dagger.module.DatabaseModule;
import com.android.mobile_project.utils.dagger.module.RetrofitModule;

public class MyApplication extends Application
        implements MainComponentProvider, HabitSettingComponentProvider,
        InputComponentProvider, CountDownComponentProvider,
        CreateHabitComponentProvider, CreateHistoryReceiverComponentProvider,
        DayChangedReceiverComponentProvider, AutoInsertServiceComponentProvider,
        RebootReceiverComponentProvider, TimeTickReceiverComponentProvider,
        CounterStepComponentProvider {

    private static final String TAG = MyApplication.class.getSimpleName();

    private ApplicationGraph graph;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(){
        graph = DaggerApplicationGraph.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule(this))
                .retrofitModule(new RetrofitModule())
                .build();
        graph.inject(this);

        AlarmUtil.setAutoInsertHistoryAlarm(getApplicationContext());
        registerReceiver(new TimeTickReceiver(), new IntentFilter(Intent.ACTION_TIME_TICK));

        DataLocalManager.init(getApplicationContext());
        DataLocalManager.getInstance().setCountToSynchronizeServer(0L);
        DataLocalManager.getInstance().setUserStateChangeData("false");
        if(DataLocalManager.getInstance().getCounterStepValue() == null){
            DataLocalManager.getInstance().setCounterStepValue(0);
        }
    }

    @Override
    public MainComponent provideMainComponent() {
        return graph.mMainComponent().create();
    }

    @Override
    public HabitSettingComponent provideHabitSettingComponent() {
        return graph.mHabitSettingComponent().create();
    }

    @Override
    public InputComponent provideInputComponent() {
        return graph.mInputComponent().create();
    }

    @Override
    public CountDownComponent provideCountDownComponent() {
        return graph.mCountDownComponent().create();
    }

    @Override
    public CreateHabitComponent provideCreateHabitComponent() {
        return graph.mCreateHabitComponent().create();
    }

    @Override
    public CreateHistoryReceiverComponent provideCreateHistoryReceiverComponent() {
        return graph.mCreateHistoryReceiverComponent().create();
    }

    @Override
    public DayChangedReceiverComponent provideDayChangedReceiverComponent() {
        return graph.mDayChangedReceiverComponent().create();
    }

    @Override
    public AutoInsertServiceComponent provideAutoInsertServiceComponent() {
        return graph.mAutoInsertServiceComponent().create();
    }

    @Override
    public RebootReceiverComponent provideRebootReceiverComponent() {
        return graph.mRebootReceiverComponent().create();
    }

    @Override
    public TimeTickReceiverComponent provideTimeTickReceiverComponent() {
        return graph.mTimeTickReceiverComponent().create();
    }

    @Override
    public CounterStepComponent provideCounterStepComponent() {
        return graph.mCounterStepComponent().create();
    }
}
