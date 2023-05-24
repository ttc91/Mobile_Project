package com.android.mobile_project.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.databinding.LayoutConfirmLoginDialogBinding;
import com.android.mobile_project.ui.activity.main.fragment.setting.ISettingViewModel;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.InitService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ToastService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class ConfirmLoginDialog extends DialogFragment {

    private final String TAG = ConfirmLoginDialog.class.getSimpleName();

    private final LayoutConfirmLoginDialogBinding binding;

    private final ISettingViewModel vm;

    private final ToastService toastService;

    private final InitService initService;

    private final int LOGIN_REQUEST_CODE = 2000;

    private GoogleSignInOptions gso;

    private GoogleSignInClient gsc;

    public ConfirmLoginDialog(Context context, ISettingViewModel vm, ToastService toastService, InitService initService){
        this.vm = vm;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.layout_confirm_login_dialog, null, false);
        this.toastService = toastService;
        this.initService = initService;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);

        binding.btnStillLogin.setOnClickListener(v -> {
            Intent it = gsc.getSignInIntent();
            startActivityForResult(it, LOGIN_REQUEST_CODE);
        });

        builder.setView(binding.getRoot());
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                if (acct != null) {
                    Log.d(TAG, "onActivityResult: ");
                    String personEmail = acct.getEmail();
                    DataLocalManager.getInstance().setUserName(personEmail);
                    vm.getUserIdByName(personEmail);
                    //viewModel.requestSignInToServer();
                    initService.loadUI();
                    toastService.makeLoginSuccess();
                    DataLocalManager.getInstance().setLoginState("true");
                    Log.d(TAG, "onActivityResult: after");
                    dismiss();
                }
            } catch (Exception e) {
                Log.e("loginError", Arrays.toString(e.getStackTrace()));
                toastService.makeErrorToast();
            }

        }
    }
}
