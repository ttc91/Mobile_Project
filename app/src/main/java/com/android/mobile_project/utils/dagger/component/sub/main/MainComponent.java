package com.android.mobile_project.utils.dagger.component.sub.main;

import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.HomeComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.PlannerComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.SettingComponent;
import com.android.mobile_project.utils.dagger.module.activity.main.sub.MainSubComponentModule;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@Subcomponent(modules = {
        MainSubComponentModule.class
})
@MyCustomAnnotation.MyScope.ActivityScope
public interface MainComponent {

    @Subcomponent.Factory
    interface Factory{
        MainComponent create();
    }

    void inject(MainActivity activity);

    HomeComponent.Factory mHomeComponent();
    PlannerComponent.Factory mPlannerComponent();
    SettingComponent.Factory mSettingComponent();

}
