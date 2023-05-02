package com.android.mobile_project.ui.binding;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class BindingAdapter {

    private BindingAdapter(){

    }

    @androidx.databinding.BindingAdapter(value = {"my_progress"})
    public static void setConstraintPercent(CircularProgressBar view, float percent){
        view.setProgress(percent);
    }

}
