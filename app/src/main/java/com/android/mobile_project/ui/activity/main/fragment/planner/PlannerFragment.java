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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
        };

    }


}
