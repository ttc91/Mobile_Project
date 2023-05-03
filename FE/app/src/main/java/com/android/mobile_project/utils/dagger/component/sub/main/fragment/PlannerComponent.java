package com.android.mobile_project.utils.dagger.component.sub.main.fragment;

import com.android.mobile_project.ui.activity.main.fragment.planner.PlannerFragment;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.main.PlannerModule;

import dagger.Subcomponent;

@Subcomponent(modules = {PlannerModule.class})
@MyCustomAnnotation.MyScope.FragmentScope
public interface PlannerComponent {

    @Subcomponent.Factory
    interface Factory{
        PlannerComponent create();
    }

    void inject(PlannerFragment fragment);
}
