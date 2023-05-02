package com.android.mobile_project.ui.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.databinding.LayoutRemainderDialogBinding;
import com.android.mobile_project.ui.activity.setting.IHabitSettingViewModel;
import com.android.mobile_project.ui.activity.setting.adapter.RemainderAdapter;
import com.android.mobile_project.ui.activity.setting.service.DbService;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RemainderDialog extends DialogFragment {

    private final LayoutRemainderDialogBinding binding;
    private final IHabitSettingViewModel vm;
    private final Context context;
    private final int position;

    private Long hourOld;
    private Long minuteOld;

    private Long hourNew;
    private Long minutesNew;

    public RemainderDialog(int position, Long hourOld, Long minuteOld, Context context, IHabitSettingViewModel vm){
        this.position = position;
        this.context = context;
        this.hourOld = hourOld;
        this.minuteOld = minuteOld;
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

        binding.hNumPicker.setValue(Math.toIntExact(hourOld));
        binding.mNumPicker.setValue(Math.toIntExact(minuteOld));

        binding.btnCancel.setOnClickListener(view -> dismiss());

        binding.btnSelect.setOnClickListener(view -> {

            hourNew = (long) binding.hNumPicker.getValue();
            minutesNew = (long) binding.mNumPicker.getValue();

            vm.updateRemainder(position, hourOld, minuteOld, hourNew, minutesNew, new DbService.UpdateRemainderResult() {
                @SuppressLint("LongLogTag")
                @Override
                public void onUpdateRemainderSuccess(CompositeDisposable disposable) {
                    Log.i("RemainderDialog-updateRemainder", "onUpdateRemainderSuccess");
                    disposable.clear();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onUpdateRemainderFailurẹ(CompositeDisposable disposable) {
                    Log.i("RemainderDialog-updateRemainder", "onUpdateRemainderFailurẹ");
                    Toast.makeText(context, "This remainder maybe has existed !", Toast.LENGTH_SHORT).show();
                    disposable.clear();
                }
            });

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
