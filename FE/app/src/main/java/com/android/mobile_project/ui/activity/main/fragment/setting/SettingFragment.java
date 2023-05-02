package com.android.mobile_project.ui.activity.main.fragment.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.databinding.FragmentSettingBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.about.AboutUsActivity;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ApiService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.InitService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ToastService;
import com.android.mobile_project.ui.dialog.ConfirmDialog;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.SettingComponent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import javax.inject.Inject;

public class SettingFragment extends Fragment implements InitLayout, View.OnClickListener{

    private final int LOGIN_REQUEST_CODE = 2000;

    public SettingComponent component;

    @Inject
    SettingViewModel viewModel;

    private FragmentSettingBinding binding;

    private GoogleSignInOptions gso;

    private GoogleSignInClient gsc;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttach(@NonNull Context context) {
        Log.i("SettingFragment", "onAttach");
        component = ((MainActivity)getActivity()).component.mSettingComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("SettingFragment", "onCreateView");
        View v = initContentView();
        initViewModel();
        viewModel.initService.initUI();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_synchronize){
            viewModel.synchronizeToServer(new ApiService.SynchronizeToServerResult() {
                @Override
                public void onSynchronizeToServerSuccess() {
                    viewModel.toastService.makeSynchronizedSuccessToast();
                }

                @Override
                public void onSynchronizeToServerFailure() {
                    viewModel.toastService.makeErrorToast();
                }
            });
        }else if(v.getId() == R.id.btn_login_logout){
            if(viewModel.isLogin){
                DataLocalManager.getInstance().setUserName("null");
                executeLogOut();
            }else {
                executeLogIn();
            }
        }else if(v.getId() == R.id.btn_about_us){
            redirectToAboutUsActivity();
        }
    }

    @Override
    public View initContentView() {

        binding = FragmentSettingBinding.inflate(getLayoutInflater());
        binding.setF(this);
        initViewModel();

        return binding.getRoot();
    }

    @Override
    public void initViewModel() {
        binding.setVm(viewModel);

        viewModel.toastService = new ToastService() {
            @Override
            public void makeSynchronizedSuccessToast() {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), SettingToastConstant.CONTENT_SYNCHRONIZE_SUCCESS, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeLoginSuccess() {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), SettingToastConstant.CONTENT_LOGIN_SUCCESS, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeErrorToast() {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), SettingToastConstant.CONTENT_ERROR, Toast.LENGTH_SHORT).show());
            }
        };

        viewModel.initService = new InitService() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void initUI() {
                String username = DataLocalManager.getInstance().getUserName();
                viewModel.isLogin = !username.equals("null");
                if(viewModel.isLogin){
                    binding.btnSynchronize.setVisibility(View.VISIBLE);
                    binding.btnLoginLogout.setBackground(getResources().getDrawable(R.drawable.bck_item_failed));
                    binding.tvLoginStatus.setText(getResources().getString(R.string.text_logout));
                }else {
                    binding.btnSynchronize.setVisibility(View.GONE);
                    binding.btnLoginLogout.setBackground(getResources().getDrawable(R.drawable.bck_item_done));
                    binding.tvLoginStatus.setText(getResources().getString(R.string.text_login));
                }
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void loadUI() {
                String username = DataLocalManager.getInstance().getUserName();
                viewModel.isLogin = !username.equals("null");
                if(viewModel.isLogin){
                    binding.btnSynchronize.setVisibility(View.VISIBLE);
                    binding.btnLoginLogout.setBackground(getResources().getDrawable(R.drawable.bck_item_failed));
                    binding.tvLoginStatus.setText(getResources().getString(R.string.text_logout));
                }else {
                    binding.btnSynchronize.setVisibility(View.GONE);
                    binding.btnLoginLogout.setBackground(getResources().getDrawable(R.drawable.bck_item_done));
                    binding.tvLoginStatus.setText(getResources().getString(R.string.text_login));
                }
            }
        };

    }

    private void executeLogOut(){
        ConfirmDialog dialog = new ConfirmDialog(getContext(), false, null, gsc, viewModel.initService);
        dialog.show(getChildFragmentManager(), "dialog");
    }

    private void executeLogIn(){
        Intent it = gsc.getSignInIntent();
        startActivityForResult(it, LOGIN_REQUEST_CODE);
    }

    private void redirectToAboutUsActivity(){
        startActivity(new Intent(getContext(), AboutUsActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                if(acct != null){
                    String personEmail = acct.getEmail();
                    DataLocalManager.getInstance().setUserName(personEmail);
                    viewModel.requestSignInToServer();
                    viewModel.initService.loadUI();
                    viewModel.toastService.makeLoginSuccess();
                }
            }catch (Exception e){
                Log.e("loginError", Arrays.toString(e.getStackTrace()));
                viewModel.toastService.makeErrorToast();
            }

        }
    }

}
