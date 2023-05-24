package com.android.mobile_project.ui.activity.main.fragment.planner;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.FragmentPlannerBinding;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.main.fragment.planner.service.DbService;
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
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlannerFragment extends Fragment implements InitLayout, View.OnClickListener {

    private static final String TAG = PlannerFragment.class.getSimpleName();

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public PlannerComponent component;

    @Inject
    PlannerViewModel viewModel;

    private FragmentPlannerBinding binding;

    private LocalDate[] dateWeek = new LocalDate[7];

    private Observer<HabitModel> longestSteakHabitModelObserver;

    private Observer<Float> percentHistoryMondayObserver;

    private Observer<Float> percentHistoryTuesdayObserver;

    private Observer<Float> percentHistoryWednesdayObserver;

    private Observer<Float> percentHistoryThursdayObserver;

    private Observer<Float> percentHistoryFridayObserver;

    private Observer<Float> percentHistorySaturdayObserver;

    private Observer<Float> percentHistorySundayObserver;

    private Observer<Integer> longestSteakPlanerFragmentObserver;

    private final DbService.CountTrueStateByHistoryDateResult mCountTrueStateByHistoryDateResultCallback = new DbService.CountTrueStateByHistoryDateResult() {
        @SuppressLint("LongLogTag")
        @Override
        public void onCountTrueStateByHistoryDate(CompositeDisposable disposable) {
            Log.i("PlannerFragment-mCountTrueStateByHistoryDateResultCallback", "onCountTrueStateByHistoryDate");
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onCountTrueStateByHistoryDateFailure(CompositeDisposable disposable) {
            Log.e("PlannerFragment-mCountTrueStateByHistoryDateResultCallback", "onCountTrueStateByHistoryDateFailure");
            disposable.clear();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttach(@NonNull Context context) {
        component = ((MainActivity) getActivity()).component.mPlannerComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        longestSteakHabitModelObserver = habitModel -> {
            binding.record.bestNum.setText(String.valueOf(habitModel.getNumOfLongestSteak()));
            binding.record.hName.setText(habitModel.getHabitName());
        };

        percentHistoryMondayObserver = aFloat -> binding.wCar.proMon.gl.setGuidelinePercent(aFloat);
        percentHistoryTuesdayObserver = aFloat -> binding.wCar.proTue.gl.setGuidelinePercent(aFloat);
        percentHistoryWednesdayObserver = aFloat -> binding.wCar.proWeb.gl.setGuidelinePercent(aFloat);
        percentHistoryThursdayObserver = aFloat -> binding.wCar.proThu.gl.setGuidelinePercent(aFloat);
        percentHistoryFridayObserver = aFloat -> binding.wCar.proFri.gl.setGuidelinePercent(aFloat);
        percentHistorySaturdayObserver = aFloat -> binding.wCar.proSat.gl.setGuidelinePercent(aFloat);
        percentHistorySundayObserver = aFloat -> binding.wCar.proSun.gl.setGuidelinePercent(aFloat);

        longestSteakPlanerFragmentObserver = integer -> {
            binding.record.longNum.setText(String.valueOf(integer));
            binding.tvLongestSteak.setText(String.valueOf(integer));
        };

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = initContentView();
        initViewModel();

        viewModel.initService.setCalendarOfMonthView();
        viewModel.initService.setBestHabit();
        viewModel.initService.setLongestSteak();
        viewModel.initService.setWeeklyCalendar();

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getLongestSteakHabitModelMutable().observe(getViewLifecycleOwner(), longestSteakHabitModelObserver);
        viewModel.getPercentHistoryMondayMutableLiveData().observe(getViewLifecycleOwner(), percentHistoryMondayObserver);
        viewModel.getPercentHistoryTuesdayMutableLiveData().observe(getViewLifecycleOwner(), percentHistoryTuesdayObserver);
        viewModel.getPercentHistoryWednesdayMutableLiveData().observe(getViewLifecycleOwner(), percentHistoryWednesdayObserver);
        viewModel.getPercentHistoryThursdayMutableLiveData().observe(getViewLifecycleOwner(), percentHistoryThursdayObserver);
        viewModel.getPercentHistoryFridayMutableLiveData().observe(getViewLifecycleOwner(), percentHistoryFridayObserver);
        viewModel.getPercentHistorySaturdayMutableLiveData().observe(getViewLifecycleOwner(), percentHistorySaturdayObserver);
        viewModel.getPercentHistorySundayMutableLiveData().observe(getViewLifecycleOwner(), percentHistorySundayObserver);
        viewModel.getLongestSteakForPlannerFragmentMutableLiveData().observe(getViewLifecycleOwner(), longestSteakPlanerFragmentObserver);
    }

    @Override
    public void onClick(View view) {
    }

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
                viewModel.setLongestSteakForPlannerFragment();
            }

            @Override
            public void setBestHabit() {

                viewModel.getHabitListDescByLongestSteak(new DbService.GetHabitByMostLongestSteak() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onGetHabitByMostLongestSteakSuccess(CompositeDisposable disposable) {
                        Log.i("PlannerFragment-getHabitListDescByLongestSteak", "onGetHabitByMostLongestSteakSuccess");
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onGetHabitByMostLongestSteakFailure(CompositeDisposable disposable) {
                        Log.i("PlannerFragment-getHabitListDescByLongestSteak", "onGetHabitByMostLongestSteakFailure");
                        disposable.clear();
                    }
                });

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void setWeeklyCalendar() {

                LocalDate date = LocalDate.now();
                DayOfWeek day = date.getDayOfWeek();
                String dayName = day.getDisplayName(TextStyle.FULL, Locale.US);

                int i;

                if(dayName.equals(com.android.mobile_project.utils.time.DayOfWeek.MON.getDayName())) {
                    dateWeek[0] = date;
                    i = 0;
                } else if (dayName.equals(com.android.mobile_project.utils.time.DayOfWeek.TUE.getDayName())) {
                    dateWeek[1] = date;
                    i = 1;
                } else if (dayName.equals(com.android.mobile_project.utils.time.DayOfWeek.WED.getDayName())) {
                    dateWeek[2] = date;
                    i = 2;
                } else if (dayName.equals(com.android.mobile_project.utils.time.DayOfWeek.THU.getDayName())) {
                    dateWeek[3] = date;
                    i = 3;
                } else if (dayName.equals(com.android.mobile_project.utils.time.DayOfWeek.FRI.getDayName())) {
                    dateWeek[4] = date;
                    i = 4;
                } else if (dayName.equals(com.android.mobile_project.utils.time.DayOfWeek.SAT.getDayName())) {
                    dateWeek[5] = date;
                    i = 5;
                } else {
                    dateWeek[6] = date;
                    i = 6;
                }

                if (!dayName.equals("Sunday") && !dayName.equals("Monday")) {

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

                } else if (dayName.equals("Monday")) {

                    int currentIndex = i;

                    while (currentIndex != 6) {
                        currentIndex += 1;
                        date = date.plusDays(1);
                        dateWeek[currentIndex] = date;
                    }

                } else {
                    int currentIndex = i;

                    while (currentIndex != 0) {
                        currentIndex -= 1;
                        date = date.minusDays(1);
                        dateWeek[currentIndex] = date;
                    }
                }

                //Monday
                viewModel.getHistoryByMonday(dateWeek[0].toString(), mCountTrueStateByHistoryDateResultCallback);

                //Tuesday:
                viewModel.getHistoryByTuesday(dateWeek[1].toString(), mCountTrueStateByHistoryDateResultCallback);

                //Wednesday:
                viewModel.getHistoryByWednesday(dateWeek[2].toString(), mCountTrueStateByHistoryDateResultCallback);

                //Thursday:
                viewModel.getHistoryByThursday(dateWeek[3].toString(), mCountTrueStateByHistoryDateResultCallback);

                //Friday:
                viewModel.getHistoryByFriday(dateWeek[4].toString(), mCountTrueStateByHistoryDateResultCallback);

                //Saturday:
                viewModel.getHistoryBySaturday(dateWeek[5].toString(), mCountTrueStateByHistoryDateResultCallback);

                //Sunday:
                viewModel.getHistoryBySunday(dateWeek[6].toString(), mCountTrueStateByHistoryDateResultCallback);

            }

        };

    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        viewModel.initService.setWeeklyCalendar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.dispose();
    }
}
