package com.android.mobile_project.ui.activity.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.ActivityMainBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.fragment.home.HomeFragment;
import com.android.mobile_project.ui.activity.main.fragment.planner.PlannerFragment;
import com.android.mobile_project.ui.activity.main.fragment.setting.SettingFragment;


public class MainActivity extends AppCompatActivity implements InitLayout {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    private FragmentManager fm;

    HomeFragment homeFragment;
    PlannerFragment plannerFragment;
    SettingFragment settingFragment;

    private static final String HOME = "HOME";
    private static final String PLANNER = "PLANNER";
    private static final String SETTING = "SETTING";

    Fragment active;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initContentView());

        initAdapter();
        initViewModel();

    }

    public void initAdapter(){

        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        active = homeFragment;

        fm.beginTransaction().add(R.id.frag_contain, homeFragment, HOME).commit();

        binding.btmNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bot_home:
                    fm.beginTransaction().hide(active).show(homeFragment).setTransition(FragmentTransaction.TRANSIT_NONE).commit();
                    active = homeFragment;
                    return true;
                case R.id.bot_planner:
                    if(plannerFragment == null){
                        plannerFragment = new PlannerFragment();
                        fm.beginTransaction().add(R.id.frag_contain, plannerFragment, PLANNER).hide(active).setTransition(FragmentTransaction.TRANSIT_NONE).commit();
                    }else {
                        fm.beginTransaction().hide(active).show(plannerFragment).setTransition(FragmentTransaction.TRANSIT_NONE).commit();
                    }
                    active = plannerFragment;
                    return true;
                case R.id.bot_setting:
                    if(settingFragment == null){
                        settingFragment = new SettingFragment();
                        fm.beginTransaction().add(R.id.frag_contain, settingFragment, SETTING).hide(active).setTransition(FragmentTransaction.TRANSIT_NONE).commit();
                    }else {
                        fm.beginTransaction().hide(active).show(settingFragment).setTransition(FragmentTransaction.TRANSIT_NONE).commit();
                    }
                    active = settingFragment;
                    return true;
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    public View initContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        return v;
    }

    @Override
    public void initViewModel() {

        viewModel = new MainViewModel();
        binding.setVm(viewModel);

        binding.executePendingBindings();

    }
}
