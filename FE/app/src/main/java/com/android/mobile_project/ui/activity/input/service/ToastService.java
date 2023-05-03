package com.android.mobile_project.ui.activity.input.service;

public interface ToastService {

    void makeUsernameEmptyToast();
    void makeErrorToast();

    interface InputToastConstant{
        String CONTENT_ERROR = "System has some error now, please contract to us";
        String CONTENT_USERNAME_IS_EMPTY = "Please input your name !";
    }

}
