package com.android.mobile_project.ui.activity.main.fragment.step;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.databinding.FragmentSetCounterStepBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.CounterStepActivity;
import com.android.mobile_project.ui.activity.main.fragment.step.service.DbService;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.SetCounterStepComponent;

import javax.inject.Inject;

public class SetCounterStepFragment extends Fragment implements View.OnClickListener, InitLayout{

    private static final String TAG = SetCounterStepFragment.class.getSimpleName();

    private static final int PHYSICAL_ACTIVITY = 2999;

    private FragmentSetCounterStepBinding binding;

    private SetCounterStepComponent component;

    private SensorManager sensorManager;

    @Inject
    SetCounterStepViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "onAttach");
        component = ((CounterStepActivity) getActivity()).component.mSetCounterStepComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = initContentView();
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == binding.btnMinus.getId()){
            binding.tvCounterStepVal.setText(viewModel.onBtnMinusClick());
        }else if(id == binding.btnPlus.getId()){
            binding.tvCounterStepVal.setText(viewModel.onBtnPlusClick());
        }else if(id == binding.btnConfirmStepCounter.getId()){

            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){

                if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
                    //ask for permission
                    requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PHYSICAL_ACTIVITY);
                }

            }else {
                Toast.makeText(getContext(), "Device not supported step counter sensor", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Device not supported step counter sensor");
            }
        }
    }

    @Override
    public View initContentView() {
        binding = FragmentSetCounterStepBinding.inflate(getLayoutInflater());
        binding.setF(this);
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PHYSICAL_ACTIVITY:
                if (grantResults.length > 0&&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.onConfirmClick(new DbService.SetStepCounterValueResult() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void onSetCounterStepSuccess() {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction().replace(R.id.counter_step_fragment, new ScheduleCounterStepFragment()).commit();
                        }

                        @Override
                        public void onSetCounterStepFailure() {
                            Toast.makeText(getContext(), "System error please contact to us", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Please give app permission to execute step business", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
