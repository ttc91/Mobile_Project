package com.android.mobile_project.utils.dagger;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;
import com.android.mobile_project.utils.dagger.module.ApplicationModule;
import com.android.mobile_project.utils.dagger.module.DatabaseModule;
import com.android.mobile_project.utils.dagger.module.SubComponentModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        DatabaseModule.class,
        SubComponentModule.class
})
public interface ApplicationGraph {

    void inject(MyApplication application);

    /**
     * Used to declare Component when start app.
     */
    MainComponent.Factory mMainComponent();
    HabitSettingComponent.Factory mHabitSettingComponent();
    InputComponent.Factory mInputComponent();
    CountDownComponent.Factory mCountDownComponent();
    CreateHabitComponent.Factory mCreateHabitComponent();

}
