package com.android.mobile_project.utils.dagger.component.sub.count;

import com.android.mobile_project.ui.activity.count.CountDownActivity;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.count.CountDownModule;

import dagger.Subcomponent;

@Subcomponent(modules = {
        CountDownModule.class
})
@MyCustomAnnotation.MyScope.ActivityScope
public interface CountDownComponent {

    @Subcomponent.Factory
    interface Factory{
        CountDownComponent create();
    }

    void inject(CountDownActivity activity);

}
