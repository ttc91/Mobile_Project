package com.android.mobile_project.utils.step;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.DataLocalManager;

public class StepCounterUtils {

    private static final String TAG = StepCounterUtils.class.getSimpleName();

    private static final Double KCAL_VAL = 0.04;

    public static int setInitKcalToStep(int kcal){
        Log.i(TAG, "setInitKcalToStep");
        return (int) (kcal / KCAL_VAL);
    }

    public static int currentStepToKcal(){
        Log.i(TAG, "currentStepToKcal");
        return (int) (DataLocalManager.getInstance().getCounterStepPerDayValue() * KCAL_VAL);
    }

    public static int currentStepToKm(){
        //8 steps -> 0.01 km
        Log.i(TAG, "currentStepToKm");
        return (int) (DataLocalManager.getInstance().getCounterStepPerDayValue() * (0.01 / 8));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int currentStep(){
        Log.i(TAG, "currentStep");
        return Math.toIntExact(DataLocalManager.getInstance().getCounterStepPerDayValue());
    }

    public static int particularStepToKcal(Long step) {
        Log.i(TAG, "particularStepToKcal");
        return (int) (step * KCAL_VAL);
    }

    public static int particularStepToKm(Long step) {
        Log.i(TAG, "particularStepToKm");
        return (int) (step * (0.01 / 8));
    }

}
