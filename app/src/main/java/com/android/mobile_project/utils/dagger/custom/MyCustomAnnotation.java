package com.android.mobile_project.utils.dagger.custom;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Scope;

public final class MyCustomAnnotation {

    public interface MyScope{

        @Scope
        @Retention(RetentionPolicy.RUNTIME)
        @interface ActivityScope{}

        @Scope
        @Retention(RetentionPolicy.RUNTIME)
        @interface FragmentScope{}

    }

    public interface MyQualifier{

        @Qualifier
        @Retention(RetentionPolicy.RUNTIME)
        @interface ApplicationContext{}

    }

}
