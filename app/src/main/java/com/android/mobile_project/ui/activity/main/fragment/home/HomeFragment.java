package com.android.mobile_project.ui.activity.main.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.FragmentHomeBinding;
import com.android.mobile_project.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.time.utils.TimeUtils;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.create.CreateHabitActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements InitLayout, View.OnClickListener {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = initContentView();
        initViewModel();

        viewModel.setDate(binding.tvDate);
        viewModel.setMonth(binding.titleMonth);

        viewModel.initUIService.initDailyCalendar();
        viewModel.initUIService.initHabitListUI();


        return v;
    }

    @Override
    public View initContentView() {

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();

    }

    @Override
    public void initViewModel() {

        viewModel = new HomeViewModel();
        binding.setVm(viewModel);

        viewModel.initUIService = new InitUIService() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void initDailyCalendar() {

                com.android.mobile_project.time.utils.TimeUtils utils = new TimeUtils();
                ArrayList<LocalDate> days = utils.getSixtyDaysArray();

                DailyCalendarAdapter adapter = new DailyCalendarAdapter(getContext(), days);
                adapter.notifyDataSetChanged();
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

                binding.rvHorCalendar.setLayoutManager(layoutManager);
                binding.rvHorCalendar.setAdapter(adapter);
                binding.rvHorCalendar.smoothScrollToPosition(days.size() / 2 + 1);

                SnapHelper helper = new LinearSnapHelper();
                helper.attachToRecyclerView(binding.rvHorCalendar);

            }

            @Override
            public void initHabitListUI() {

                List<HabitEntity> entities = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitListByUserId(DataLocalManager.getUserId());

                HabitAdapter adapter = new HabitAdapter(getContext(), entities);
                adapter.notifyDataSetChanged();
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                binding.rcvHabitList.setAdapter(adapter);
                binding.rcvHabitList.setLayoutManager(manager);

            }
        };

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == R.id.btn_ch){
            clickCreateHabit();
        }

    }

    public void clickCreateHabit(){

        Intent intent = new Intent(getContext(), CreateHabitActivity.class);
        startActivity(intent);

    }
}
