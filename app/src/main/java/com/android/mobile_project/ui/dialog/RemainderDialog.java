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
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.model.db.RemainderEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.LayoutRemainderDialogBinding;
import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;

public class RemainderDialog extends DialogFragment {

    private LayoutRemainderDialogBinding binding;
    private RemainderEntity entity;
    private Long hour;
    private Long minute;

    public Long getMinute() {
        return minute;
    }

    public Long getHour() {
        return hour;
    }

    private Context context;

    public RemainderDialog(RemainderEntity entity, Context context){
        this.entity = entity;
        this.hour = entity.hourTime;
        this.minute = entity.minutesTime;
        this.context = context;
        binding = DataBindingUtil.
                inflate(LayoutInflater.from(this.context), R.layout.layout_remainder_dialog,null,false);
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

        binding.hNumPicker.setValue(Math.toIntExact(entity.hourTime));
        binding.mNumPicker.setValue(Math.toIntExact(entity.minutesTime));

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entity.hourTime = Long.valueOf(binding.hNumPicker.getValue());
                entity.minutesTime = Long.valueOf(binding.mNumPicker.getValue());

                hour = entity.hourTime;
                minute = entity.minutesTime;

                HabitTrackerDatabase.getInstance(context).remainderDao().updateRemainder(entity);

                dismiss();
            }
        });



        builder.setView(binding.getRoot());
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

}
