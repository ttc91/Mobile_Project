package com.android.mobile_project.ui.activity.main.fragment.step.service;

public interface DbService {

    interface SetStepCounterValueResult{
        void onSetCounterStepSuccess();
        void onSetCounterStepFailure();
    }

}
