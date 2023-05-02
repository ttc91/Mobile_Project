package com.android.mobile_project.ui.activity.main.fragment.setting.service;

public interface ToastService {

    void makeSynchronizedSuccessToast();
    void makeLoginSuccess();
    void makeErrorToast();

    interface SettingToastConstant{
        String CONTENT_SYNCHRONIZE_SUCCESS = "Synchronize data completed !";
        String CONTENT_LOGIN_SUCCESS = "Login success !";
        String CONTENT_ERROR = "System has some error now, please contract to us";
    }

}
