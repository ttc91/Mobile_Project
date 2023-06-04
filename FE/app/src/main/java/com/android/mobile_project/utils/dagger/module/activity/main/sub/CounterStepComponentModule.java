package com.android.mobile_project.utils.dagger.module.activity.main.sub;

import com.android.mobile_project.utils.dagger.component.sub.main.fragment.ScheduleCounterStepComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.SetCounterStepComponent;

import dagger.Module;

@Module(
        subcomponents = {
                SetCounterStepComponent.class,
                ScheduleCounterStepComponent.class
        }
)
public class CounterStepComponentModule {
}
