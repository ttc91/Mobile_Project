package com.android.mobile_project.ui.activity.input;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.ui.activity.input.service.DbService;
import com.android.mobile_project.ui.activity.input.service.ToastService;

public class InputViewModel extends ViewModel {

    protected DbService service;
    protected ToastService toastService;

}
