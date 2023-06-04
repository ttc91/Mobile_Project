package com.android.mobile_project.ui.activity.main.fragment.step;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.ui.activity.main.fragment.step.service.DbService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.step.StepCounterUtils;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.FragmentScope
public class SetCounterStepViewModel extends ViewModel {

    private static final String TAG = SetCounterStepViewModel.class.getSimpleName();

    @Inject
    public SetCounterStepViewModel() { }

    private String value = "10";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    protected String onBtnMinusClick(){
        Log.i(TAG, "onBtnMinusClick");
        int number = Integer.parseInt(value);
        if(number > 10){
            number -= 1;
            value = String.valueOf(number);
            Log.i(TAG, value);
            return value;
        }
        return "10";
    }

    protected String onBtnPlusClick(){
        Log.i(TAG, "onBtnPlusClick");
        int number = Integer.parseInt(value);
        number += 1;
        value = String.valueOf(number);
        Log.i(TAG, value);
        return value;
    }

    protected void onConfirmClick(DbService.SetStepCounterValueResult callback){
        try {
            Log.i(TAG, "onConfirmClick");
            Log.i(TAG, value);
            DataLocalManager.getInstance().setCounterStepValue(StepCounterUtils.setInitKcalToStep(Integer.parseInt(value)));
            DataLocalManager.getInstance().resetCounterStepPerDayValue();
            callback.onSetCounterStepSuccess();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            callback.onSetCounterStepFailure();
        }
    }

}
