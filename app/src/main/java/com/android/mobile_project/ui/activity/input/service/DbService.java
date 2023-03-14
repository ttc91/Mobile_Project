package com.android.mobile_project.ui.activity.input.service;

public interface DbService {

    void setUser(String userName);

    boolean checkExistUser();

    interface InsertUserResult{

        void onInsertUserSuccess();

        void onInsertUserFailure();

    }

    interface GetUserIdFromLocalResult{
        void onGetIdSuccess();
        void onFetIdFailure();
    }

}
