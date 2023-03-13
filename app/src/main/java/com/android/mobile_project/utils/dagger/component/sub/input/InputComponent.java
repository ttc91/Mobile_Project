package com.android.mobile_project.utils.dagger.component.sub.input;

import com.android.mobile_project.ui.activity.input.InputActivity;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.input.InputModule;

import dagger.Subcomponent;

@Subcomponent(modules = InputModule.class)
@MyCustomAnnotation.MyScope.ActivityScope
public interface InputComponent {

    @Subcomponent.Factory
    interface Factory{
        InputComponent create();
    }

    void inject(InputActivity activity);

}
