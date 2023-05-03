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
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.LayoutConfirmDialogBinding;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.InitService;
import com.android.mobile_project.ui.activity.setting.IHabitSettingViewModel;
import com.android.mobile_project.ui.activity.setting.service.DbService;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ConfirmDialog extends DialogFragment {

    private boolean isDeleteHabit;

    private final LayoutConfirmDialogBinding binding;

    private final Context context;

    private final IHabitSettingViewModel vm;

    private final GoogleSignInClient gsc;

    private final InitService initService;

    public ConfirmDialog(Context context, boolean isDeleteHabit, IHabitSettingViewModel vm, GoogleSignInClient gsc, InitService initService) {
        this.context = context;
        this.isDeleteHabit = isDeleteHabit;
        binding = DataBindingUtil.
                inflate(LayoutInflater.from(context), R.layout.layout_confirm_dialog, null, false);
        this.vm = vm;
        this.gsc = gsc;
        this.initService = initService;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        binding.btnNo.setOnClickListener(v -> dismiss());

        if(isDeleteHabit){
            binding.btnYes.setOnClickListener(v -> {
                vm.deleteHabit(new DbService.DeleteHabitResult() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onDeleteHabitSuccess(CompositeDisposable disposable) {
                        Log.i("ConfirmDialog-deleteHabit", "onDeleteHabitSuccess");
                        disposable.clear();
                        dismiss();
                        getActivity().finish();
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onDeleteHabitFailure(CompositeDisposable disposable) {
                        Log.e("ConfirmDialog-deleteHabit", "onDeleteHabitFailure");
                        disposable.clear();
                    }
                });
            });
            binding.tvConfirm.setText(getResources().getString(R.string.str_content_delete_habit));
            binding.ivConfirm.setImageResource(R.drawable.ic_wastebasket);
        }else {
            binding.btnYes.setOnClickListener(v -> {
                gsc.signOut().addOnCompleteListener(task -> initService.loadUI());
                dismiss();
            });
            binding.tvConfirm.setText(getResources().getString(R.string.str_content_logout));
            binding.ivConfirm.setImageResource(R.drawable.ic_logout);
        }

        builder.setView(binding.getRoot());
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }
}
