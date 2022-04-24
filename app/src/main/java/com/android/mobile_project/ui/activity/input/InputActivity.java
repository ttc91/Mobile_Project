package com.android.mobile_project.ui.activity.input;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.UserEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.ActivityInputBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.input.service.DbService;
import com.android.mobile_project.ui.activity.input.service.ToastService;
import com.android.mobile_project.ui.activity.main.MainActivity;

public class InputActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private InputViewModel viewModel;
    private ActivityInputBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataLocalManager.init(getApplicationContext());

        setContentView(initContentView());
        initViewModel();

        checkUI();

    }

    @Override
    public View initContentView() {

        binding = ActivityInputBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        return v;
    }

    @Override
    public void initViewModel() {

        viewModel = new InputViewModel();
        binding.setVm(viewModel);

        viewModel.service = new DbService() {
            @Override
            public void setUser(String userName) {

                UserEntity user = new UserEntity();
                user.userName = userName;

                HabitTrackerDatabase.getInstance(getApplicationContext()).userDao().insertUser(user);

                DataLocalManager.setUserName(userName);
                Long userId = HabitTrackerDatabase.getInstance(getApplicationContext()).userDao().getUserIdByName(userName);
                DataLocalManager.setUserId(userId);

            }

            @Override
            public boolean checkExistUser() {

                String name = DataLocalManager.getUserName();

                if(name.equals(null) || name.equals("")){
                    return false;
                }

                return true;
            }
        };

        viewModel.toastService = new ToastService() {
            @Override
            public void makeToast() {
                Toast.makeText(getApplicationContext(),"Please input your name !", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == R.id.btn_next){

            String name = binding.edtInputUser.getText().toString().trim();

            if(name.length() > 0){
                viewModel.service.setUser(name);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            else {
                viewModel.toastService.makeToast();
            }
        }

    }

    public void checkUI(){

        boolean check = viewModel.service.checkExistUser();

        if(check){

            Log.e("User name :", DataLocalManager.getUserName());
            Log.e("User id :", String.valueOf(DataLocalManager.getUserId()));

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
