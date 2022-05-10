package com.android.mobile_project.ui.activity.count;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile_project.databinding.ActivityProcessTimeBinding;
import com.android.mobile_project.ui.InitLayout;

public class CountDownActivity extends AppCompatActivity implements InitLayout {

    ActivityProcessTimeBinding binding;
    CountDownViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        initViewModel();
    }

    @Override
    public View initContentView() {

        binding = ActivityProcessTimeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

        viewModel = new CountDownViewModel();
        binding.setVm(viewModel);

    }
}
