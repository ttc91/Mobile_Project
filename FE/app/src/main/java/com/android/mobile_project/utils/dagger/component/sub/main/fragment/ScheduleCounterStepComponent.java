package com.android.mobile_project.utils.dagger.component.sub.main.fragment;

import com.android.mobile_project.ui.activity.main.fragment.step.ScheduleCounterStepFragment;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@MyCustomAnnotation.MyScope.FragmentScope
@Subcomponent
public interface ScheduleCounterStepComponent {

    @Subcomponent.Factory
    interface Factory{
        ScheduleCounterStepComponent create();
    }

    void inject(ScheduleCounterStepFragment fragment);

}
