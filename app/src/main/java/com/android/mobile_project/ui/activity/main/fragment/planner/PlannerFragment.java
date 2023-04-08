package com.android.mobile_project.ui.activity.main.fragment.planner;

import android.content.Context;
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

import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.FragmentPlannerBinding;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.PlannerComponent;
import com.android.mobile_project.utils.time.adapter.MonthlyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;
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

import javax.inject.Inject;

public class PlannerFragment extends Fragment implements InitLayout, View.OnClickListener {

    public PlannerComponent component;

    @Inject
    PlannerViewModel viewModel;

    private FragmentPlannerBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttach(@NonNull Context context) {
        component = ((MainActivity)getActivity()).component.mPlannerComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

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
    public void onClick(View view) { }

    @Override
    public View initContentView() {
        binding = FragmentPlannerBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

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

                List<HistoryModel> list = viewModel.getHistoryByDate(yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                boolean check = false;

                for(HistoryModel model : list){
                    if (model.getHistoryHabitsState().equals("true")){
                        PlannerViewModel.steak += 1;
                        check = true;
                        break;
                    }

                    if(check){
                        break;
                    }

                }

                binding.record.longNum.setText(String.valueOf(PlannerViewModel.steak));

            }

            @Override
            public void setBestHabit() {

                List<HabitModel> list = viewModel.getHabitListDescByLongestSteak();
                HabitModel model = list.get(0);
                binding.record.bestNum.setText(String.valueOf(model.getNumOfLongestSteak()));
                binding.record.hName.setText(model.getHabitName());

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void setWeeklyCalendar() {

                LocalDate date = LocalDate.now();
                DayOfWeek day = date.getDayOfWeek();
                String dayName = day.getDisplayName(TextStyle.FULL, Locale.US);

                LocalDate[] dateWeek = new LocalDate[7];

                int i;

                if(dayName.equals(R.string.str_monday)){
                    dateWeek[0] = date;
                    i = 0;
                }else if(dayName.equals(R.string.str_tuesday)){
                    dateWeek[1] = date;
                    i = 1;
                }else if(dayName.equals(R.string.str_wednesday)){
                    dateWeek[2] = date;
                    i = 2;
                }else if(dayName.equals(R.string.str_thursday)){
                    dateWeek[3] = date;
                    i = 3;
                }else if(dayName.equals(R.string.str_friday)){
                    dateWeek[4] = date;
                    i = 4;
                }else if(dayName.equals(R.string.str_saturday)){
                    dateWeek[5] = date;
                    i = 5;
                }else {
                    dateWeek[6] = date;
                    i = 6;
                }

                if(!dayName.equals("Sunday") && !dayName.equals("Monday")) {

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

                }else if(dayName.equals("Monday")){

                    int currentIndex = i;

                    while(currentIndex != 6){
                        currentIndex += 1;
                        date = date.plusDays(1);
                        dateWeek[currentIndex] = date;
                    }

                }else {
                    int currentIndex = i;

                    while (currentIndex != 0) {
                        currentIndex += 1;
                        date = date.minusDays(1);
                        dateWeek[currentIndex] = date;
                    }
                }

                List<HistoryModel> models = viewModel.getHistoryByDate(dateWeek[0].toString());
                if(models.size() == 0){
                    binding.wCar.proMon.gl.setGuidelinePercent(1);
                }else {
                    int count = 0;
                    for(HistoryModel model : models){
                        if(model.getHistoryHabitsState().equals("true")){
                            count = count + 1;
                        }
                    }
                    float percent = (float)count/(float)models.size();
                    binding.wCar.proMon.gl.setGuidelinePercent(percent);
                }

                //Tuesday:
                models = viewModel.getHistoryByDate(dateWeek[1].toString());
                if(models.size() == 0){
                    binding.wCar.proTue.gl.setGuidelinePercent(1);
                }else {
                    int count = 0;
                    for(HistoryModel model : models){
                        if(model.getHistoryHabitsState().equals("true")){
                            count = count + 1;
                        }
                    }
                    float percent = (float)count/(float)models.size();
                    binding.wCar.proTue.gl.setGuidelinePercent(percent);
                }

                //Wednesday:
                models = viewModel.getHistoryByDate(dateWeek[2].toString());
                if(models.size() == 0){
                    binding.wCar.proWeb.gl.setGuidelinePercent(1);
                }else {
                    int count = 0;
                    for(HistoryModel model : models){
                        if(model.getHistoryHabitsState().equals("true")){
                            count = count + 1;
                        }
                    }
                    float percent = (float)count/(float)models.size();
                    binding.wCar.proWeb.gl.setGuidelinePercent(percent);
                }

                //Thursday:
                models = viewModel.getHistoryByDate(dateWeek[3].toString());
                if(models.size() == 0){
                    binding.wCar.proThu.gl.setGuidelinePercent(1);
                }else {
                    int count = 0;
                    for(HistoryModel model : models){
                        if(model.getHistoryHabitsState().equals("true")){
                            count = count + 1;
                        }
                    }
                    float percent = (float)count/(float)models.size();
                    binding.wCar.proThu.gl.setGuidelinePercent(percent);
                }

                //Friday:
                models = viewModel.getHistoryByDate(dateWeek[4].toString());
                if(models.size() == 0){
                    binding.wCar.proFri.gl.setGuidelinePercent(1);
                }else {
                    int count = 0;
                    for(HistoryModel model : models){
                        if(model.getHistoryHabitsState().equals("true")){
                            count = count + 1;
                        }
                    }
                    float percent = (float)count/(float)models.size();
                    binding.wCar.proFri.gl.setGuidelinePercent(percent);
                }

                //Saturday:
                models = viewModel.getHistoryByDate(dateWeek[5].toString());
                if(models.size() == 0){
                    binding.wCar.proSat.gl.setGuidelinePercent(1);
                }else {
                    int count = 0;
                    for(HistoryModel model : models){
                        if(model.getHistoryHabitsState().equals("true")){
                            count = count + 1;
                        }
                    }
                    float percent = (float)count/(float)models.size();
                    binding.wCar.proSat.gl.setGuidelinePercent(percent);
                }

                //Sunday:
                models = viewModel.getHistoryByDate(dateWeek[6].toString());
                if(models.size() == 0){
                    binding.wCar.proSun.gl.setGuidelinePercent(1);
                }else {
                    int count = 0;
                    for(HistoryModel model : models){
                        if(model.getHistoryHabitsState().equals("true")){
                            count = count + 1;
                        }
                    }
                    float percent = (float)count/(float)models.size();
                    binding.wCar.proSun.gl.setGuidelinePercent(percent);
                }

            }
        };

    }


}
