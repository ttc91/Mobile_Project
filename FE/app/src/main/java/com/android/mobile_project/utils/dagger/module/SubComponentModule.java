package com.android.mobile_project.utils.dagger.module;

import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.CreateHistoryReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.DayChangedReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.service.AutoInsertServiceComponent;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;

import dagger.Module;

@Module(subcomponents = {
        MainComponent.class,
        HabitSettingComponent.class,
        CountDownComponent.class,
        InputComponent.class,
        CreateHabitComponent.class,
        CreateHistoryReceiverComponent.class,
        DayChangedReceiverComponent.class,
        AutoInsertServiceComponent.class
})
public class SubComponentModule { }
