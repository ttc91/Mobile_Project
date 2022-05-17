package com.android.mobile_project.ui.activity.main.fragment.home;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.time.utils.TimeUtils;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.AfterAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
import com.android.mobile_project.ui.activity.main.fragment.home.service.UpdateService;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeViewModel extends ViewModel {

    protected InitUIService initUIService;
    protected UpdateService updateService;
    protected HabitAdapter.RecyclerViewClickListener recyclerViewClickListener;

    protected List<HabitEntity> habitEntityList = new ArrayList<>();
    protected HabitAdapter adapter;

    protected List<HabitEntity> habitEntityDoneList = new ArrayList();
    protected DoneHabitAdapter doneHabitAdapter;

    protected List<HabitEntity> habitEntityFailedList = new ArrayList();
    protected FailedHabitAdapter failedHabitAdapter;

    protected List<HistoryEntity> historyEntityList = new ArrayList<>();

    protected boolean hideToDo = false;
    protected boolean hideDone = false;
    protected boolean hideFailed = false;

    private TimeUtils timeUtils = new TimeUtils();
    private java.time.DayOfWeek day = timeUtils.getSelectedDate().getDayOfWeek();
    protected String dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault());
    protected Long dayOfWeekId;

    protected DailyCalendarAdapter.OnClickItem onClickItem;

    protected DailyCalendarAdapter dailyCalendarAdapter;
    protected List<LocalDate> days = new ArrayList<>();

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
