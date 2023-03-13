package com.android.mobile_project.utils.dagger.component.sub.main.fragment;

import com.android.mobile_project.ui.activity.main.fragment.setting.SettingFragment;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.main.SettingModule;

import dagger.Subcomponent;

@MyCustomAnnotation.MyScope.FragmentScope
@Subcomponent(modules = {SettingModule.class})
public interface SettingComponent {

    @Subcomponent.Factory
    interface Factory{
        SettingComponent create();
    }

    void inject(SettingFragment fragment);
}
