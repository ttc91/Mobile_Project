package com.android.mobile_project.utils.dagger.component.sub.setting;

import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.setting.HabitSettingModule;

import dagger.Subcomponent;

@Subcomponent(modules = HabitSettingModule.class)
@MyCustomAnnotation.MyScope.ActivityScope
public interface HabitSettingComponent {

    @Subcomponent.Factory
    interface Factory{
        HabitSettingComponent create();
    }

    void inject(HabitSettingActivity activity);

}
