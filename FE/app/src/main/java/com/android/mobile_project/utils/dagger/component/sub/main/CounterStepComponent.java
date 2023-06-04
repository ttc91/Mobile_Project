package com.android.mobile_project.utils.dagger.component.sub.main;

import com.android.mobile_project.ui.activity.main.CounterStepActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.ScheduleCounterStepComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.SetCounterStepComponent;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.main.sub.CounterStepComponentModule;

import dagger.Subcomponent;

@Subcomponent(modules = CounterStepComponentModule.class)
@MyCustomAnnotation.MyScope.ActivityScope
public interface CounterStepComponent {

    @Subcomponent.Factory
    interface Factory{
        CounterStepComponent create();
    }

    void inject(CounterStepActivity activity);

    SetCounterStepComponent.Factory mSetCounterStepComponent();
    ScheduleCounterStepComponent.Factory mScheduleCounterStepComponent();

}
