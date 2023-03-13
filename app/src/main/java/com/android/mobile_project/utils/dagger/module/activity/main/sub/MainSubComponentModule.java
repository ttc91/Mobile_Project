package com.android.mobile_project.utils.dagger.module.activity.main.sub;

import com.android.mobile_project.utils.dagger.component.sub.main.fragment.HomeComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.PlannerComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.SettingComponent;

import dagger.Module;

@Module(
        subcomponents = {
                HomeComponent.class,
                PlannerComponent.class,
                SettingComponent.class
        }
)
public class MainSubComponentModule {
}
