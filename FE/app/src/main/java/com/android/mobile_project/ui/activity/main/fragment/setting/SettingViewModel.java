package com.android.mobile_project.ui.activity.main.fragment.setting;

import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.FragmentScope
public class SettingViewModel {

    @Inject
    public SettingViewModel() {
    }

}
