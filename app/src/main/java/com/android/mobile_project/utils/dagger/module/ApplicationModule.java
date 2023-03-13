package com.android.mobile_project.utils.dagger.module;

import android.app.Application;
import android.content.Context;

import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Provides
    @MyCustomAnnotation.MyQualifier.ApplicationContext
    public Context provideApplicationContext(){
        return application;
    }

}
