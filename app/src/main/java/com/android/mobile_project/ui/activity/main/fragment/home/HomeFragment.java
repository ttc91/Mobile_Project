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

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.FragmentHomeBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.create.CreateHabitActivity;

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
