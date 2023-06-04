package com.android.mobile_project.utils.dagger.component.sub.main.fragment;

import com.android.mobile_project.ui.activity.main.fragment.step.SetCounterStepFragment;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@MyCustomAnnotation.MyScope.FragmentScope
@Subcomponent
public interface SetCounterStepComponent {

    @Subcomponent.Factory
    interface Factory{
        SetCounterStepComponent create();
    }

    void inject(SetCounterStepFragment fragment);

}
