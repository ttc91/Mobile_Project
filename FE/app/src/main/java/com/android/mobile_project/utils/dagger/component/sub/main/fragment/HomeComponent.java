package com.android.mobile_project.utils.dagger.component.sub.main.fragment;

import com.android.mobile_project.ui.activity.main.fragment.home.HomeFragment;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.main.HomeModule;

import dagger.Subcomponent;

@Subcomponent(modules = {HomeModule.class})
@MyCustomAnnotation.MyScope.FragmentScope
public interface HomeComponent {

    @Subcomponent.Factory
    interface Factory{
        HomeComponent create();
    }

    void inject(HomeFragment fragment);
}
