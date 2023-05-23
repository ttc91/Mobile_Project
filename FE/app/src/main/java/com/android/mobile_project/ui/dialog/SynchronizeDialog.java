package com.android.mobile_project.ui.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.databinding.LayoutSynchronizeDialogBinding;
import com.android.mobile_project.ui.activity.main.fragment.setting.ISettingViewModel;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ApiService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ToastService;

public class SynchronizeDialog extends DialogFragment {

    private final LayoutSynchronizeDialogBinding binding;

    private final ISettingViewModel vm;

    private final ToastService toastService;

    public SynchronizeDialog(Context context, ISettingViewModel vm, ToastService toastService){
        this.vm = vm;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.layout_synchronize_dialog, null, false);
        this.toastService = toastService;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        binding.btnNo.setOnClickListener(v -> dismiss());

        binding.btnYes.setOnClickListener(v -> vm.synchronizeToServer(new ApiService.SynchronizeToServerResult() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSynchronizeToServerSuccess() {
                toastService.makeSynchronizedSuccessToast();
                DataLocalManager.getInstance().setUserStateChangeData("false");
                Log.i("Change state data change", DataLocalManager.getInstance().getUserStateChangeData());
                dismiss();
            }

            @Override
            public void onSynchronizeToServerFailure() {
                toastService.makeErrorToast();
            }
        }));

        builder.setView(binding.getRoot());
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }
}
