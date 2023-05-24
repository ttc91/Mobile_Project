package com.android.mobile_project.ui.activity.guide;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.ActivityGuideBinding;
import com.android.mobile_project.ui.InitLayout;

public class GuideActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private ActivityGuideBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_guid_back){
            finish();
        }
    }

    @Override
    public View initContentView() {
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

    }
}
