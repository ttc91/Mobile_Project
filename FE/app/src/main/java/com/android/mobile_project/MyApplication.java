package com.android.mobile_project;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.utils.dagger.ApplicationGraph;
import com.android.mobile_project.utils.dagger.DaggerApplicationGraph;
import com.android.mobile_project.utils.dagger.component.provider.AutoInsertServiceComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CountDownComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CreateHabitComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CreateHistoryReceiverComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.DayChangedReceiverComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.HabitSettingComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.InputComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.MainComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.CreateHistoryReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.DayChangedReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.service.AutoInsertServiceComponent;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;
import com.android.mobile_project.utils.dagger.module.ApplicationModule;
import com.android.mobile_project.utils.dagger.module.DatabaseModule;
import com.android.mobile_project.utils.dagger.module.RetrofitModule;
import com.android.mobile_project.utils.worker.AutoInsertWorker;

public class MyApplication extends Application
        implements MainComponentProvider, HabitSettingComponentProvider,
        InputComponentProvider, CountDownComponentProvider,
        CreateHabitComponentProvider, CreateHistoryReceiverComponentProvider,
        DayChangedReceiverComponentProvider, AutoInsertServiceComponentProvider {

    private ApplicationGraph graph;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(){
        graph = DaggerApplicationGraph.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule(this))
//                .retrofitModule(new RetrofitModule())
                .build();
        graph.inject(this);

        AutoInsertWorker.enqueueWorker(getApplicationContext());

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
}
