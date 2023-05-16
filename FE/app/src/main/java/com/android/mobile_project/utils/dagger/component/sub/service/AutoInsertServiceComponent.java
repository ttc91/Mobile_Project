package com.android.mobile_project.utils.dagger.component.sub.service;

import com.android.mobile_project.service.AutoInsertService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@Subcomponent
@MyCustomAnnotation.MyScope.ServiceScope
public interface AutoInsertServiceComponent {


    @Subcomponent.Factory
    interface Factory{
        AutoInsertServiceComponent create();
    }

    void inject(AutoInsertService service);

}
