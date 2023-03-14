package com.android.mobile_project.ui.activity.input.service;

public interface DbService {

    void setUser(String userName);

    boolean checkExistUser(GetUsernameFromLocalResult callback);

    interface InsertUserResult{
        void onInsertUserSuccess();
        void onInsertUserFailure();
    }

    interface GetUserIdFromLocalResult{
        void onGetIdSuccess();
        void onGetIdFailure();
    }

    interface GetUsernameFromLocalResult{
        boolean onGetIdSuccess();
        boolean onGetIdFailure();
    }

}
