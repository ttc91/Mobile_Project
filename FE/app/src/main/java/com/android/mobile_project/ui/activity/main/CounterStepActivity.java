package com.android.mobile_project.ui.activity.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.databinding.ActivityCounterStepBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.fragment.step.ScheduleCounterStepFragment;
import com.android.mobile_project.ui.activity.main.fragment.step.SetCounterStepFragment;
import com.android.mobile_project.utils.dagger.component.provider.CounterStepComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.main.CounterStepComponent;

public class CounterStepActivity extends AppCompatActivity implements InitLayout {

    private final String TAG = CounterStepActivity.class.getSimpleName();

    private ActivityCounterStepBinding binding;

    public CounterStepComponent component;

    private FragmentManager fm;

    private static final SetCounterStepFragment SET_COUNTER_STEP_FRAGMENT = new SetCounterStepFragment();

    private static final ScheduleCounterStepFragment SCHEDULE_COUNTER_STEP_FRAGMENT = new ScheduleCounterStepFragment();

    private static final String SET_COUNTER_STEP_FRAGMENT_FLAG = "SET_COUNTER_STEP_FRAGMENT";

    private static final String SET_SCHEDULE_COUNTER_STEP_FRAGMENT = "SET_SCHEDULE_COUNTER_STEP";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        component = ((CounterStepComponentProvider) getApplicationContext()).provideCounterStepComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);

        fm = getSupportFragmentManager();


        fm.beginTransaction().add(R.id.counter_step_fragment,
                SET_COUNTER_STEP_FRAGMENT, SET_COUNTER_STEP_FRAGMENT_FLAG).commit();
        fm.beginTransaction().add(R.id.counter_step_fragment,
                SCHEDULE_COUNTER_STEP_FRAGMENT, SET_SCHEDULE_COUNTER_STEP_FRAGMENT).commit();

        setContentView(initContentView());

        switch (DataLocalManager.getInstance().getCounterStepValue()){
            case 0:
                fm.beginTransaction().show(SET_COUNTER_STEP_FRAGMENT)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .hide(SCHEDULE_COUNTER_STEP_FRAGMENT)
                        .commit();
                break;
            default:
                fm.beginTransaction()
                        .show(SCHEDULE_COUNTER_STEP_FRAGMENT).setTransition(FragmentTransaction.TRANSIT_NONE)
                        .hide(SET_COUNTER_STEP_FRAGMENT)
                        .commit();
                break;
        }

    }

    @Override
    public View initContentView() {
        binding = ActivityCounterStepBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {
        binding.executePendingBindings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
