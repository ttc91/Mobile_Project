package com.android.mobile_project.ui.activity.main.fragment.home;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.time.utils.TimeUtils;

public class HomeViewModel extends ViewModel {

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setDate(TextView text){

        TimeUtils utils = new TimeUtils();
        text.setText(utils.getDateFromLocalDate());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setMonth(TextView text){
        TimeUtils utils = new TimeUtils();
        text.setText(utils.getMonthYearFromDate());
    }

}
