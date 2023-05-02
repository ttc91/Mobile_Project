package com.android.mobile_project.ui.activity.main.fragment.setting.service;

public interface ApiService {

    interface SynchronizeToServerResult{
        void onSynchronizeToServerSuccess();
        void onSynchronizeToServerFailure();
    }

}
