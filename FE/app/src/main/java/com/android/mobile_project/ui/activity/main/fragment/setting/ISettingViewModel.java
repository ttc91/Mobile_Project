package com.android.mobile_project.ui.activity.main.fragment.setting;

import com.android.mobile_project.ui.activity.main.fragment.setting.service.ApiService;

public interface ISettingViewModel {

    void synchronizeToServer(ApiService.SynchronizeToServerResult callback);
    void deleteAllDB();
    void loadAllDataFromServer();
    void insertAllDataIntoDB();
    void loadReminderFromServer();
    void loadHistoryFromServer();
    void loadHabitInWeekFromServer();
    void loadHabitFromServer();
    void getUserIdByName(String username);
    void requestSignInToServer();

}
