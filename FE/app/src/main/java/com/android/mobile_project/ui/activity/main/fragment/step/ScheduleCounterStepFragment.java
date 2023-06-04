package com.android.mobile_project.ui.activity.main.fragment.step;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.databinding.FragmentScheduleCounterStepBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.CounterStepActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.ScheduleCounterStepComponent;
import com.android.mobile_project.utils.step.StepCounterUtils;

import javax.inject.Inject;

public class ScheduleCounterStepFragment extends Fragment implements View.OnClickListener, InitLayout {

    private static final String TAG = ScheduleCounterStepFragment.class.getSimpleName();

    private FragmentScheduleCounterStepBinding binding;

    private ScheduleCounterStepComponent component;

    private Observer<Float> mondayObserver;

    private Observer<Float> tuesdayObserver;

    private Observer<Float> wednesdayObserver;

    private Observer<Float> thursdayObserver;

    private Observer<Float> fridayObserver;

    private Observer<Float> saturdayObserver;

    private Observer<Float> sundayObserver;

    private Observer<String> workTodayObserver;

    private Observer<String> stepTodayObserver;

    private Observer<String> distanceObserver;

    private Observer<String> stepWeekObserver;

    private SensorManager sensorManager;

    private Sensor stepCounterSensor;

    @Inject
    ScheduleCounterStepViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "onAttach");
        component = ((CounterStepActivity) getActivity()).component.mScheduleCounterStepComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //Observer behavior
        stepTodayObserver = s -> binding.tvStepToday.setText(s);
        workTodayObserver = s -> binding.tvWorkToday.setText(s);
        distanceObserver = s -> binding.tvDistanceToday.setText(s);

        mondayObserver = aFloat -> {
            if (aFloat == 0.0F || aFloat == 0 || String.valueOf(aFloat).equals("0")) {
                binding.stepScheduleChart.cirStepMon.setBackgroundColor(R.color.white);
            }
            binding.stepScheduleChart.cirStepMon.setProgress(aFloat);
        };
        tuesdayObserver = aFloat -> {
            if (aFloat == 0.0F || aFloat == 0 || String.valueOf(aFloat).equals("0")) {
                binding.stepScheduleChart.cirStepTue.setBackgroundColor(R.color.white);
            }
            binding.stepScheduleChart.cirStepTue.setProgress(aFloat);
        };
        wednesdayObserver = aFloat -> {
            if (aFloat == 0.0F || aFloat == 0 || String.valueOf(aFloat).equals("0")) {
                binding.stepScheduleChart.cirStepWed.setBackgroundColor(R.color.white);
            }
            binding.stepScheduleChart.cirStepWed.setProgress(aFloat);
        };
        thursdayObserver = aFloat -> {
            if (aFloat == 0.0F || aFloat == 0 || String.valueOf(aFloat).equals("0")) {
                binding.stepScheduleChart.cirStepThu.setBackgroundColor(R.color.white);
            }
            binding.stepScheduleChart.cirStepThu.setProgress(aFloat);
        };
        fridayObserver = aFloat -> {
            if (aFloat == 0.0F || aFloat == 0 || String.valueOf(aFloat).equals("0")) {
                binding.stepScheduleChart.cirStepFri.setBackgroundColor(R.color.white);
            }
            binding.stepScheduleChart.cirStepFri.setProgress(aFloat);
        };
        saturdayObserver = aFloat -> {
            if (aFloat == 0.0F || aFloat == 0 || String.valueOf(aFloat).equals("0")) {
                binding.stepScheduleChart.cirStepSat.setBackgroundColor(R.color.white);
            }
            binding.stepScheduleChart.cirStepSat.setProgress(aFloat);
        };
        sundayObserver = aFloat -> {
            if (aFloat == 0.0F || aFloat == 0 || String.valueOf(aFloat).equals("0")) {
                binding.stepScheduleChart.cirStepSun.setBackgroundColor(R.color.white);
            }
            binding.stepScheduleChart.cirStepSun.setProgress(aFloat);
        };

        stepWeekObserver = s -> {
            binding.tvStepWeek.setText(s);
            binding.tvWorkWeek.setText(viewModel.getTvWorkWeekVal());
            binding.tvDistanceWeek.setText(viewModel.getTvDistanceWeekVal());
            binding.tvScheduleStepWeek.setText(s + " / "
                    + (DataLocalManager.getInstance().getCounterStepValue() * 7));
        };

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        initViewModel();

        //Subscribe observer
        viewModel.getMutableMondayPercentLiveData().observe(getViewLifecycleOwner(), mondayObserver);
        viewModel.getMutableTuesdayPercentLiveData().observe(getViewLifecycleOwner(), tuesdayObserver);
        viewModel.getMutableWednesdayPercentLiveData().observe(getViewLifecycleOwner(), wednesdayObserver);
        viewModel.getMutableThursdayPercentLiveData().observe(getViewLifecycleOwner(), thursdayObserver);
        viewModel.getMutableFridayPercentLiveData().observe(getViewLifecycleOwner(), fridayObserver);
        viewModel.getMutableSaturdayPercentLiveData().observe(getViewLifecycleOwner(), saturdayObserver);
        viewModel.getMutableSundayPercentLiveData().observe(getViewLifecycleOwner(), sundayObserver);

        viewModel.getMutableStepValTodayLiveData().observe(getViewLifecycleOwner(), stepTodayObserver);
        viewModel.getMutableWorkValTodayLiveData().observe(getViewLifecycleOwner(), workTodayObserver);
        viewModel.getMutableDistanceValTodayLiveData().observe(getViewLifecycleOwner(), distanceObserver);

        viewModel.getMutableStepWeekLiveData().observe(getViewLifecycleOwner(), stepWeekObserver);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        Log.i(TAG, "onRegister");
        sensorManager.registerListener(new SensorEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.i(TAG, "onSensorChanged");
                if (event.sensor == stepCounterSensor) {
                    DataLocalManager.getInstance().setCounterStepPerDayValue();
                    binding.tvWorkToday.setText(String.valueOf(StepCounterUtils.currentStepToKcal()));
                    binding.tvStepToday.setText(String.valueOf(StepCounterUtils.currentStep()));
                    binding.tvDistanceToday.setText(String.valueOf(StepCounterUtils.currentStepToKm()));

                    viewModel.setTvStepTotalWeek(viewModel.getTvStepTotalWeek() + 1L);
                    binding.tvStepWeek.setText(String.valueOf(viewModel.getTvStepTotalWeek()));
                    binding.tvWorkWeek.setText(viewModel.getTvWorkWeekVal());
                    binding.tvDistanceWeek.setText(viewModel.getTvDistanceWeekVal());
                    binding.tvScheduleStepWeek.setText(viewModel.getTvStepTotalWeek() + " / "
                            + (DataLocalManager.getInstance().getCounterStepValue() * 7));

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i(TAG, "onAccuracyChanged");
            }
        }, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);

        return initContentView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == binding.btnBack.getId()){
            getActivity().finish();
        }else if(id == binding.btnConfig.getId()){
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.counter_step_fragment, new SetCounterStepFragment()).commit();
        }
    }

    @Override
    public View initContentView() {
        binding = FragmentScheduleCounterStepBinding.inflate(getLayoutInflater());
        binding.setF(this);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initViewModel() {
        viewModel.scheduleToday();
        viewModel.scheduleWeek();
    }
}
