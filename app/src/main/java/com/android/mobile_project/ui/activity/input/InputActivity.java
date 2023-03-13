package com.android.mobile_project.ui.activity.input;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(initContentView());

        component = ((InputComponentProvider) getApplicationContext()).provideInputComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);

        DataLocalManager.init(getApplicationContext());

        initViewModel();

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

                viewModel.insertUser(user);

                DataLocalManager.setUserName(userName);
                DataLocalManager.setUserId(viewModel.getUserIdByName(userName));

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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
