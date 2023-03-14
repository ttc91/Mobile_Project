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

        setContentView(initContentView());

        component = ((InputComponentProvider) getApplicationContext()).provideInputComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);

        DataLocalManager.init(getApplicationContext());

        initViewModel();

        mUserIdObserver = DataLocalManager::setUserId;
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
                        Log.e("InputActivity", "insert user to RoomDB complete");
                        viewModel.getUserIdByName(userName, new GetUserIdFromLocalResult() {
                            @Override
                            public void onGetIdSuccess() {
                                Log.i("InputActivity", "can redirect to MainActivity");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFetIdFailure() {
                                Log.e("InputActivity", "cannot redirect To MainActivity");
                            }
                        });
                    }

                    @Override
                    public void onInsertUserFailure() {
                        Log.e("InputActivity", "cannot insert user to RoomDB");
                    }
                });

                DataLocalManager.setUserName(userName);

            }

            @Override
            public boolean checkExistUser() {
                String name = DataLocalManager.getUserName();
                return !name.equals(null) && !name.equals("");
            }
        };

        viewModel.toastService = () -> Toast.makeText(getApplicationContext(),"Please input your name !", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == R.id.btn_next){

            if(binding.edtInputUser.getText().toString().trim().length() > 0){
                viewModel.service.setUser(binding.edtInputUser.getText().toString().trim());
            }
            else {
                viewModel.toastService.makeToast();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.setDispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.setDispose();
    }

    public void checkUI(){

        boolean check = viewModel.service.checkExistUser();

        if(check){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

}
