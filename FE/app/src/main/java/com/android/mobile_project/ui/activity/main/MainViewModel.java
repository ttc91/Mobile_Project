package com.android.mobile_project.ui.activity.main;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class MainViewModel extends ViewModel {

    @Inject
    public MainViewModel() { }

}
