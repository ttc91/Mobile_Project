package com.android.mobile_project.ui.activity.main.fragment.setting;

import com.android.mobile_project.ui.activity.main.fragment.setting.service.ApiService;

public interface ISettingViewModel {

    void synchronizeToServer(ApiService.SynchronizeToServerResult callback);

}
