package com.android.mobile_project.ui.activity.main.fragment.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.FragmentPlannerBinding;
import com.android.mobile_project.ui.InitLayout;

public class PlannerFragment extends Fragment implements InitLayout, View.OnClickListener {

    private PlannerViewModel viewModel;
    private FragmentPlannerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initContentView();

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

    }


}
