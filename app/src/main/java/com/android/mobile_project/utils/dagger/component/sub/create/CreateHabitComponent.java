package com.android.mobile_project.utils.dagger.component.sub.create;

import com.android.mobile_project.ui.activity.create.CreateHabitActivity;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.dagger.module.activity.create.CreateHabitModule;

import dagger.Subcomponent;

@Subcomponent(modules = {
        CreateHabitModule.class
})
@MyCustomAnnotation.MyScope.ActivityScope
public interface CreateHabitComponent {

    @Subcomponent.Factory
    interface Factory{
        CreateHabitComponent create();
    }

    void inject(CreateHabitActivity fragment);

}
