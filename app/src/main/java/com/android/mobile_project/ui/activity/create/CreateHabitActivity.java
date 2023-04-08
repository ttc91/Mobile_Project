package com.android.mobile_project.ui.activity.create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.ActivityCreateHabitBinding;
import com.android.mobile_project.receiver.local.CreateHistoryReceiver;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.create.service.DbService;
import com.android.mobile_project.ui.activity.create.service.InitService;
import com.android.mobile_project.ui.activity.create.service.ToastService;
import com.android.mobile_project.utils.dagger.component.provider.CreateHabitComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;

import javax.inject.Inject;

public class CreateHabitActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private ActivityCreateHabitBinding binding;

    private final CreateHistoryReceiver receiver = new CreateHistoryReceiver();

    private static final String MY_ACTION = "com.android.project.CREATE_HISTORY";

    public CreateHabitComponent component;

    private Observer<Boolean> habitModelInsertedObserver = aBoolean -> {
        sendBroadcast(new Intent(MY_ACTION));
    };

    @Inject
    CreateHabitViewModel viewModel;

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(MY_ACTION));
        viewModel.getHabitModelInsertedMutableLiveData().observe(this, habitModelInsertedObserver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i("CreateHabitActivity", "onCreate");

        setContentView(initContentView());

        component = ((CreateHabitComponentProvider) getApplicationContext()).provideCreateHabitComponent();
        component.inject(this);
        super.onCreate(savedInstanceState);


        initViewModel();
        viewModel.initService.intiDayOfWeekLogo();

    }

    @Override
    public View initContentView() {
        binding = ActivityCreateHabitBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        viewModel.toastService = new ToastService() {
            @Override
            public void makeInsertHabitSuccessToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), CreateHabitToastConstant.CONTENT_CREATE_HABIT_SUCCESS, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeHabitNameIsExistedToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), CreateHabitToastConstant.CONTENT_HABIT_NAME_IS_EXISTED, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeHabitNameInputtedIsEmptyToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), CreateHabitToastConstant.CONTENT_HABIT_NAME_IS_EMPTY, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeDaysOfWeekInputtedIsEmptyToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), CreateHabitToastConstant.CONTENT_DAY_OF_WEEK_IS_EMPTY, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeDayOfTimeInputtedIsEmptyToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), CreateHabitToastConstant.CONTENT_DAY_OF_TIME_IS_EMPTY, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeErrorToast() {
                runOnUiThread(()->Toast.makeText(getApplicationContext(), CreateHabitToastConstant.CONTENT_ERROR, Toast.LENGTH_SHORT).show());
            }
        };

        viewModel.initService = new InitService() {

            @Override
            public void intiDayOfWeekLogo() {
                binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
            }

            @Override
            public void initCreateHabitEvent() {

                if(binding.edtHname.getText().toString().trim().equals("") || binding.edtHname.getText().toString().trim().equals(null)){
                    viewModel.toastService.makeHabitNameInputtedIsEmptyToast();
                }else {
                    viewModel.checkExistHabitByName(binding.edtHname.getText().toString().trim(),
                            new DbService.GetHabitByName() {
                                @Override
                                public void onGetHabitByNameSuccess(Long id) {
                                    Log.i("checkExistHabitByName", "already existed with ID - " + id);
                                    viewModel.toastService.makeHabitNameIsExistedToast();
                                }

                                @Override
                                public void onGetHabitByNameFailure() {
                                    Log.i("checkExistHabitByName", "has not existed");
                                        viewModel.insertHabit(binding.edtHname.getText().toString().trim(), new DbService.InsertHabit() {
                                            @Override
                                            public void onInsertHabitSuccess() {
                                                Log.i("insertHabit", "isSuccess");
                                                viewModel.toastService.makeInsertHabitSuccessToast();
                                                finish();
                                            }

                                            @Override
                                            public void onInsertHabitFailure() {
                                                Log.e("insertHabit", "isFailure");
                                                viewModel.toastService.makeErrorToast();
                                            }
                                        }
                                    );
                                }
                            }
                    );
                }
            }
        };

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == R.id.btn_back){
            clickBackToHome();
        }else if (id == R.id.sun_date || id == R.id.mon_date || id == R.id.tue_date || id == R.id.wed_date || id == R.id.thu_date || id == R.id.fri_date || id == R.id.sat_date){
            clickBtnDateOfWeek(id);
        }else if(id == R.id.time_afternoon || id == R.id.time_morning || id == R.id.time_any || id == R.id.time_night){
            clickBtnDateOfTime(id);
        }else if(id == R.id.btn_create){
            viewModel.initService.initCreateHabitEvent();
        }

    }

    public void clickBackToHome(){
        viewModel.disposeCompositeDisposable();
        finish();
    }

    /**
     * <b>Event</b> : Click on day of week button
     */

    @SuppressLint("NonConstantResourceId")
    public void clickBtnDateOfWeek(int id){

        switch (id){

            case R.id.sun_date :
                if (viewModel.isSelectSunDate()){
                    binding.sunDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.sunDate.setTextColor(getResources().getColor(R.color.black));
                    viewModel.setSelectSunDate(false);
                }else {
                    binding.sunDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.sunDate.setTextColor(getResources().getColor(R.color.white));
                    viewModel.setSelectSunDate(true);
                }
                break;
            case R.id.mon_date :
                if (viewModel.isSelectMonDate()){
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.monDate.setTextColor(getResources().getColor(R.color.black));
                    viewModel.setSelectMonDate(false);
                }else {
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.monDate.setTextColor(getResources().getColor(R.color.white));
                    viewModel.setSelectMonDate(true);
                }
                break;
            case R.id.tue_date :
                if (viewModel.isSelectTueDate()){
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.tueDate.setTextColor(getResources().getColor(R.color.black));
                    viewModel.setSelectTueDate(false);
                }else {
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.tueDate.setTextColor(getResources().getColor(R.color.white));
                    viewModel.setSelectTueDate(true);
                }
                break;
            case R.id.wed_date :
                if (viewModel.isSelectWedDate()){
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.wedDate.setTextColor(getResources().getColor(R.color.black));
                    viewModel.setSelectWedDate(false);
                }else {
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.wedDate.setTextColor(getResources().getColor(R.color.white));
                    viewModel.setSelectWedDate(true);
                }
                break;
            case R.id.thu_date :
                if (viewModel.isSelectThuDate()){
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.thuDate.setTextColor(getResources().getColor(R.color.black));
                    viewModel.setSelectThuDate(false);
                }else {
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.thuDate.setTextColor(getResources().getColor(R.color.white));
                    viewModel.setSelectThuDate(true);
                }
                break;
            case R.id.fri_date :
                if (viewModel.isSelectFriDate()){
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.friDate.setTextColor(getResources().getColor(R.color.black));
                    viewModel.setSelectFriDate(false);
                }else {
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.friDate.setTextColor(getResources().getColor(R.color.white));
                    viewModel.setSelectFriDate(true);
                }
                break;
            case R.id.sat_date :
                if (viewModel.isSelectSatDate()){
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.satDate.setTextColor(getResources().getColor(R.color.black));
                    viewModel.setSelectSatDate(false);
                }else {
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.satDate.setTextColor(getResources().getColor(R.color.white));
                    viewModel.setSelectSatDate(true);
                }
                break;
            default:
                break;

        }
    }

    /**
     * <b>Event</b> : Click on day of time button
     */

    @SuppressLint("NonConstantResourceId")
    public void clickBtnDateOfTime(int id){

        binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAfternoon.setTextColor(getResources().getColor(R.color.black));
        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
        viewModel.setSelectAfternoon(false);

        binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAnytime.setTextColor(getResources().getColor(R.color.black));
        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
        viewModel.setSelectAnytime(false);

        binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tMorning.setTextColor(getResources().getColor(R.color.black));
        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
        viewModel.setSelectMorning(false);

        binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tNight.setTextColor(getResources().getColor(R.color.black));
        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
        viewModel.setSelectNight(false);

        switch (id) {

            case R.id.time_afternoon :

                binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAfternoon.setTextColor(getResources().getColor(R.color.white));
                binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                viewModel.setSelectAfternoon(true);
                break;

            case R.id.time_any :

                binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAnytime.setTextColor(getResources().getColor(R.color.white));
                binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                viewModel.setSelectAnytime(true);
                break;

            case R.id.time_morning :

                binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tMorning.setTextColor(getResources().getColor(R.color.white));
                binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                viewModel.setSelectMorning(true);
                break;

            case R.id.time_night :

                binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tNight.setTextColor(getResources().getColor(R.color.white));
                binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                viewModel.setSelectNight(true);
                break;

            default:
                break;

        }

    }

    @Override
    public void onDestroy() {
        Log.i("CreateHabitActivity", "onDestroy");
        super.onDestroy();
        viewModel.disposeCompositeDisposable();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        Log.i("CreateHabitActivity", "onBackPressed");
        super.onBackPressed();
        viewModel.disposeCompositeDisposable();
        finish();
    }

}
