package com.android.mobile_project.ui.activity.login;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.remote.model.UserModel;
import com.android.mobile_project.databinding.ActivityLoginBinding;
import com.android.mobile_project.receiver.system.DayChangedReceiver;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.login.service.DbService;
import com.android.mobile_project.ui.activity.login.service.ToastService;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.provider.InputComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.Calendar;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private final int LOGIN_REQUEST_CODE = 2000;

    @Inject
    LoginViewModel viewModel;

    private ActivityLoginBinding binding;

    public InputComponent component;

    private Observer<Long> mUserIdObserver;

    private AlarmManager mAlarmManager;

    private PendingIntent mPendingIntent;

    private GoogleSignInOptions gso;

    private GoogleSignInClient gsc;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        viewModel.initService.initCheckingUI();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        startInsertHistoryAlarm();

    }

    @Override
    public View initContentView() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        viewModel.dbService = new DbService() {
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

        viewModel.initService = () -> {
            boolean check = viewModel.dbService.checkExistUser(new DbService.GetUsernameFromLocalResult() {
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
        };

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_doing_now){
            viewModel.dbService.setUser("null");
        }else if(view.getId() == R.id.btn_gg){
            Intent it = gsc.getSignInIntent();
            startActivityForResult(it, LOGIN_REQUEST_CODE);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void redirectToNextActivity(){
        Log.i("InputActivity", "can redirect to MainActivity");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void startInsertHistoryAlarm(){

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), DayChangedReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, mPendingIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if(acct != null){
                    String personEmail = acct.getEmail();
                    viewModel.dbService.setUser(personEmail);
                    viewModel.requestSignInToServer();
                }
                redirectToNextActivity();
            }catch (Exception e){
                Log.e("loginError", Arrays.toString(e.getStackTrace()));
                viewModel.toastService.makeErrorToast();
            }

        }
    }
}
