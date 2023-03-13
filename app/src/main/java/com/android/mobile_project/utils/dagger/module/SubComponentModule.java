package com.android.mobile_project.utils.dagger.module;

import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;

import dagger.Module;

@Module(subcomponents = {
        MainComponent.class,
        HabitSettingComponent.class,
        CreateHabitComponent.class,
        CountDownComponent.class,
        InputComponent.class
})
public class SubComponentModule { }
