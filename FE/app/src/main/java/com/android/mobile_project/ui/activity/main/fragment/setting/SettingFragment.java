package com.android.mobile_project.ui.activity.main.fragment.setting;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.mobile_project.R;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.SettingComponent;

import javax.inject.Inject;

public class SettingFragment extends Fragment {

    public SettingComponent component;

    @Inject
    SettingViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttach(@NonNull Context context) {
        component = ((MainActivity)getActivity()).component.mSettingComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, container, false);

    }
}
