package com.android.mobile_project.ui.activity.input;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.remote.model.UserModel;
import com.android.mobile_project.databinding.ActivityInputBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.input.service.DbService;
import com.android.mobile_project.ui.activity.input.service.ToastService;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.provider.InputComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;

import javax.inject.Inject;

public class InputActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    @Inject
    InputViewModel viewModel;

    private ActivityInputBinding binding;

    public InputComponent component;

    private Observer<Long> mUserIdObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i("InputActivity", "onCreate()");

        setContentView(initContentView());

        component = ((InputComponentProvider) getApplicationContext()).provideInputComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);

        DataLocalManager.init(getApplicationContext());

        initViewModel();

        mUserIdObserver = aLong -> DataLocalManager.getInstance().setUserId(aLong) ;
        viewModel.getUserIdMutableLiveData().observe(this, mUserIdObserver);

        checkUI();

    }

    @Override
    public View initContentView() {

        binding = ActivityInputBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        viewModel.service = new DbService() {
            @Override
            public void setUser(String userName) {

                UserModel user = new UserModel();
                user.setUserName(userName);

                viewModel.insertUser(user, new InsertUserResult() {
                    @Override
                    public void onInsertUserSuccess() {
                        Log.i("InputActivity", "insert user to RoomDB complete");
                        viewModel.getUserIdByName(userName, new GetUserIdFromLocalResult() {
                            @Override
                            public void onGetIdSuccess() {
                                redirectToNextActivity();
                            }

                            @Override
                            public void onGetIdFailure() {
                                Log.e("InputActivity", "cannot redirect To MainActivity");
                                viewModel.toastService.makeErrorToast();
                            }
                        });
                    }

                    @Override
                    public void onInsertUserFailure() {
                        Log.e("InputActivity", "cannot insert user to RoomDB");
                        viewModel.toastService.makeErrorToast();
                    }
                });

                DataLocalManager.getInstance().setUserName(userName);

            }

            @Override
            public boolean checkExistUser(GetUsernameFromLocalResult callback) {
                String name = DataLocalManager.getInstance().getUserName();
                return !name.equals(null) && !name.equals("");
            }
        };

        viewModel.toastService = new ToastService() {
            @Override
            public void makeUsernameEmptyToast() {
                Toast.makeText(getApplicationContext(), ToastService.InputToastConstant.CONTENT_USERNAME_IS_EMPTY, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void makeErrorToast() {
                Toast.makeText(getApplicationContext(), ToastService.InputToastConstant.CONTENT_ERROR, Toast.LENGTH_SHORT).show();
            }
        };

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_next){

            if(binding.edtInputUser.getText().toString().trim().length() > 0){
                viewModel.service.setUser(binding.edtInputUser.getText().toString().trim());
            }
            else {
                viewModel.toastService.makeUsernameEmptyToast();
            }
        }

    }

    @Override
    protected void onStop() {
        Log.i("InputActivity", "onStop()");
        super.onStop();
        viewModel.setDispose();
    }

    @Override
    protected void onDestroy() {
        Log.i("InputActivity", "onDestroy()");
        super.onDestroy();
        viewModel.setDispose();
        mUserIdObserver = null;
        viewModel = null;
    }

    private void checkUI(){

        boolean check = viewModel.service.checkExistUser(new DbService.GetUsernameFromLocalResult() {
            @Override
            public boolean onGetIdSuccess() {
                Log.i("InputActivity", "onGetIdSuccess");
                return true;
            }

            @Override
            public boolean onGetIdFailure() {
                Log.e("InputActivity", "onGetIdFailure");
                return false;
            }
        });

        if(check){
           redirectToNextActivity();
        }else{
            viewModel.toastService.makeUsernameEmptyToast();
        }
    }

    private void redirectToNextActivity(){
        Log.i("InputActivity", "can redirect to MainActivity");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
