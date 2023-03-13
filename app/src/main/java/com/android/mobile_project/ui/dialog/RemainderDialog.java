package com.android.mobile_project.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.databinding.LayoutRemainderDialogBinding;
import com.android.mobile_project.ui.activity.setting.IHabitSettingViewModel;

public class RemainderDialog extends DialogFragment {

    private final LayoutRemainderDialogBinding binding;
    private final RemainderModel model;
    private final IHabitSettingViewModel vm;

    private Long hour;
    private Long minute;

    public Long getMinute() {
        return minute;
    }

    public Long getHour() {
        return hour;
    }

    public RemainderDialog(RemainderModel model, Context context, IHabitSettingViewModel vm){
        this.model = model;
        this.hour = model.getHourTime();
        this.minute = model.getMinutesTime();
        binding = DataBindingUtil.
                inflate(LayoutInflater.from(context), R.layout.layout_remainder_dialog,null,false);
        this.vm = vm;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding.setDialog(this);

        binding.hNumPicker.setMaxValue(59);
        binding.hNumPicker.setMinValue(0);
        binding.mNumPicker.setMaxValue(59);
        binding.mNumPicker.setMinValue(0);

        binding.hNumPicker.setValue(Math.toIntExact(model.getHourTime()));
        binding.mNumPicker.setValue(Math.toIntExact(model.getMinutesTime()));

        binding.btnCancel.setOnClickListener(view -> dismiss());

        binding.btnSelect.setOnClickListener(view -> {

            model.setHourTime((long) binding.hNumPicker.getValue());
            model.setMinutesTime((long) binding.mNumPicker.getValue());

            hour = model.getHourTime();
            minute = model.getMinutesTime();

            vm.updateRemainder(model);

            dismiss();
        });

        builder.setView(binding.getRoot());
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

}
