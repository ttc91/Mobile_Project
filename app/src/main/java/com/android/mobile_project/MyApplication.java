package com.android.mobile_project;

import android.app.Application;

import com.android.mobile_project.utils.dagger.ApplicationGraph;
import com.android.mobile_project.utils.dagger.DaggerApplicationGraph;
import com.android.mobile_project.utils.dagger.component.provider.CountDownComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.CreateHabitComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.HabitSettingComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.InputComponentProvider;
import com.android.mobile_project.utils.dagger.component.provider.MainComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;
import com.android.mobile_project.utils.dagger.module.ApplicationModule;
import com.android.mobile_project.utils.dagger.module.DatabaseModule;

public class MyApplication extends Application
        implements MainComponentProvider, HabitSettingComponentProvider,
        CreateHabitComponentProvider, InputComponentProvider, CountDownComponentProvider {

    private ApplicationGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        graph = DaggerApplicationGraph.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule(this))
                .build();
        graph.inject(this);
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
    public CreateHabitComponent provideCreateHabitComponent() {
        return graph.mCreateHabitComponent().create();
    }

    @Override
    public InputComponent provideInputComponent() {
        return graph.mInputComponent().create();
    }

    @Override
    public CountDownComponent provideCountDownComponent() {
        return graph.mCountDownComponent().create();
    }
}
