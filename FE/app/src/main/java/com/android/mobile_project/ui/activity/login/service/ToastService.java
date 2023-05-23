package com.android.mobile_project.ui.activity.login.service;

public interface ToastService {

    void makeUsernameEmptyToast();
    void makeErrorToast();
    void makeNetworkErrorConnectToast();

    interface InputToastConstant{
        String CONTENT_ERROR = "System has some error now, please contract to us";
        String CONTENT_USERNAME_IS_EMPTY = "Please input your name !";
        String CONTENT_CONNECT_NETWORK_ERROR = "Please connect network !";
    }

}
