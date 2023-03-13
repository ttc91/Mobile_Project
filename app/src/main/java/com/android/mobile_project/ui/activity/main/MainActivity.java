package com.android.mobile_project.ui.activity.main;

import android.annotation.SuppressLint;
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
import com.android.mobile_project.utils.dagger.component.provider.MainComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements InitLayout {

    private ActivityMainBinding binding;

    public MainComponent component;

    @Inject
    MainViewModel viewModel;

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

        setContentView(initContentView());

        component = ((MainComponentProvider) getApplicationContext()).provideMainComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);

        initAdapter();
        initViewModel();

    }

    @SuppressLint("NonConstantResourceId")
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
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        binding.executePendingBindings();

    }
}
