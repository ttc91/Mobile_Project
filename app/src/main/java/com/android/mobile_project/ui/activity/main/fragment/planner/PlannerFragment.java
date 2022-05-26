package com.android.mobile_project.ui.activity.main.fragment.planner;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.FragmentPlannerBinding;
import com.android.mobile_project.time.adapter.MonthlyCalendarAdapter;
import com.android.mobile_project.time.utils.TimeUtils;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.fragment.planner.service.InitService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlannerFragment extends Fragment implements InitLayout, View.OnClickListener {

    private PlannerViewModel viewModel;
    private FragmentPlannerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = initContentView();
        initViewModel();

        viewModel.initService.setCalendarOfMonthView();
        viewModel.initService.setLongestSteak();
        viewModel.initService.setBestHabit();
        viewModel.initService.setWeeklyCalendar();

        return v;

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();


    }

    @Override
    public View initContentView() {

        binding = FragmentPlannerBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

        viewModel = new PlannerViewModel();
        binding.setVm(viewModel);

        viewModel.initService = new InitService() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void setCalendarOfMonthView() {

                TimeUtils utils = new TimeUtils();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
                LocalDate date = LocalDate.now();
                String today = date.format(formatter);

                binding.tvMonth.setText(utils.getMonthYearFromDate());
                ArrayList<String> daysInMonth = utils.daysInMonthArray();

                MonthlyCalendarAdapter calendarAdapter = new MonthlyCalendarAdapter(getContext(), today, daysInMonth);
                RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 7);

                binding.verCar.rcvCalendarVer.setLayoutManager(manager);
                binding.verCar.rcvCalendarVer.setAdapter(calendarAdapter);

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void setLongestSteak() {

                TimeUtils utils = new TimeUtils();
                LocalDate yesterday = utils.getSelectedDate().minus(1, ChronoUnit.DAYS);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String y_String = yesterday.format(formatter);
                List<HistoryEntity> list = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), y_String);

                Boolean check = false;

                for(HistoryEntity entity : list){
                    if (entity.historyHabitsState.equals("true") || entity.historyHabitsState == "true"){
                        viewModel.steak += 1;
                        check = true;
                        break;
                    }

                    if(check == true){
                        break;
                    }

                }

                binding.record.longNum.setText(String.valueOf(viewModel.steak));

            }

            @Override
            public void setBestHabit() {

                List<HabitEntity> list = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitListDescByLongestSteak();
                HabitEntity entity = list.get(0);
                binding.record.bestNum.setText(String.valueOf(entity.numOfLongestSteak));
                binding.record.hName.setText(entity.habitName);

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void setWeeklyCalendar() {

                LocalDate date = LocalDate.now();
                DayOfWeek day = date.getDayOfWeek();
                String dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault());

                LocalDate dateWeek[]= new LocalDate[7];

                int i;

                if(dayName.equals("Monday") || dayName == "Monday"){
                    dateWeek[0] = date;
                    i = 0;
                }else if(dayName.equals("Tuesday") || dayName == "Tuesday"){
                    dateWeek[1] = date;
                    i = 1;
                }else if(dayName.equals("Wednesday") || dayName == "Wednesday"){
                    dateWeek[2] = date;
                    i = 2;
                }else if(dayName.equals("Thursday") || dayName == "Thursday"){
                    dateWeek[3] = date;
                    i = 3;
                }else if(dayName.equals("Friday") || dayName == "Friday"){
                    dateWeek[4] = date;
                    i = 4;
                }else if(dayName.equals("Saturday") || dayName == "Saturday"){
                    dateWeek[5] = date;
                    i = 5;
                }else {
                    dateWeek[6] = date;
                    i = 6;
                }

                if(dayName != "Sunday" && dayName != "Monday") {

                    int currentIndex = i;

                    while (currentIndex != 6) {
                        currentIndex += 1;
                        date = date.plusDays(1);
                        dateWeek[currentIndex] = date;
                    }

                    currentIndex = i;
                    date = LocalDate.now();

                    while (currentIndex != 0) {
                        currentIndex -= 1;
                        date = date.minusDays(1);
                        dateWeek[currentIndex] = date;
                    }

                }else if(dayName.equals("Monday") || dayName == "Monday"){

                    int currentIndex = i;

                    while(currentIndex != 6){
                        currentIndex += 1;
                        date = date.plusDays(1);
                        dateWeek[currentIndex] = date;
                    }

                }else {
                    int currentIndex = i;

                    while(currentIndex != 0){
                        currentIndex += 1;
                        date = date.minusDays(1);
                        dateWeek[currentIndex] = date;
                    }
                }

                //Monday:

                List<HistoryEntity> entities = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), dateWeek[0].toString());
                if(entities.size() == 0){
                    binding.wCar.proMon.gl.setGuidelinePercent(1 - 0);
                }else {
                    int count = 0;
                    for(HistoryEntity historyEntity : entities){
                        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
                            count = count + 1;
                        }
                    }
                    binding.wCar.proMon.gl.setGuidelinePercent(1- (float)(count/entities.size()));
                }

                //Tuesday:
                entities = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), dateWeek[1].toString());
                if(entities.size() == 0){
                    binding.wCar.proTue.gl.setGuidelinePercent(1 - 0);
                }else {
                    int count = 0;
                    for(HistoryEntity historyEntity : entities){
                        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
                            count = count + 1;
                        }
                    }
                    binding.wCar.proTue.gl.setGuidelinePercent(1- (float)(count/entities.size()));
                }

                //Wednesday:
                entities = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), dateWeek[2].toString());
                if(entities.size() == 0){
                    binding.wCar.proWeb.gl.setGuidelinePercent(1 - 0);
                }else {
                    int count = 0;
                    for(HistoryEntity historyEntity : entities){
                        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
                            count = count + 1;
                        }
                    }
                    binding.wCar.proWeb.gl.setGuidelinePercent(1- (float)(count/entities.size()));
                }

                //Thursday:
                entities = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), dateWeek[3].toString());
                if(entities.size() == 0){
                    binding.wCar.proThu.gl.setGuidelinePercent(1 - 0);
                }else {
                    int count = 0;
                    for(HistoryEntity historyEntity : entities){
                        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
                            count = count + 1;
                        }
                    }
                    binding.wCar.proThu.gl.setGuidelinePercent(1- (float)(count/entities.size()));
                }

                //Friday:
                entities = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), dateWeek[4].toString());
                if(entities.size() == 0){
                    binding.wCar.proFri.gl.setGuidelinePercent(1 - 0);
                }else {
                    int count = 0;
                    for(HistoryEntity historyEntity : entities){
                        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
                            count = count + 1;
                        }
                    }
                    binding.wCar.proFri.gl.setGuidelinePercent(1- (float)(count/entities.size()));
                }

                //Saturday:
                entities = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), dateWeek[5].toString());
                if(entities.size() == 0){
                    binding.wCar.proSat.gl.setGuidelinePercent(1 - 0);
                }else {
                    int count = 0;
                    for(HistoryEntity historyEntity : entities){
                        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
                            count = count + 1;
                        }
                    }
                    binding.wCar.proSat.gl.setGuidelinePercent(1- (float)(count/entities.size()));
                }

                //Sunday:
                entities = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(DataLocalManager.getUserId(), dateWeek[6].toString());
                if(entities.size() == 0){
                    binding.wCar.proSun.gl.setGuidelinePercent(1 - 0);
                }else {
                    int count = 0;
                    for(HistoryEntity historyEntity : entities){
                        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
                            count = count + 1;
                        }
                    }
                    binding.wCar.proSun.gl.setGuidelinePercent(1- (float)(count/entities.size()));
                }

            }
        };

    }


}
