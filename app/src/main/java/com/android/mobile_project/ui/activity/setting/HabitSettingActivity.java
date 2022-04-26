package com.android.mobile_project.ui.activity.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.model.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.ActivityHabitSettingBinding;
import com.android.mobile_project.time.DayOfWeek;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.setting.service.InitService;

import java.util.ArrayList;
import java.util.List;

public class HabitSettingActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private ActivityHabitSettingBinding binding;
    private HabitSettingViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initContentView());

        initViewModel();

        viewModel.initService.getHabit();
        viewModel.initService.getHabitInWeek();

        viewModel.initService.initUI();
    }

    @Override
    public View initContentView() {

        binding = ActivityHabitSettingBinding.inflate(getLayoutInflater());
        binding.setA(this);
        View v = binding.getRoot();
        return v;

    }

    @Override
    public void initViewModel() {

        viewModel = new HabitSettingViewModel();
        binding.setVm(viewModel);

        binding.executePendingBindings();

        viewModel.initService = new InitService() {
            @Override
            public void getHabit() {

                Bundle extras = getIntent().getExtras();
                Long habitId = extras.getLong("habitId");

                HabitEntity habitEntity = HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().getHabitByUserIdAndHabitId(DataLocalManager.getUserId(), habitId);
                binding.setHabit(habitEntity);
                viewModel.habitEntity = habitEntity;

            }

            @Override
            public void getHabitInWeek() {

                List<HabitInWeekEntity> entity = HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getUserId(),
                        viewModel.habitEntity.habitId);
                viewModel.habitInWeekEntity = entity;

            }

            @Override
            public void initUI() {

                List<DayOfWeekEntity> dayOfWeekEntityList = new ArrayList<>();

                for(HabitInWeekEntity entity : viewModel.habitInWeekEntity){
                    DayOfWeekEntity dayOfWeekEntity = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfWeekDao().getDayOfWeekById(entity.dayOfWeekId);
                    dayOfWeekEntityList.add(dayOfWeekEntity);

                    Log.e("Date", dayOfWeekEntity.dayOfWeekName);
                }

                for(DayOfWeekEntity entity : dayOfWeekEntityList){

                    switch (entity.dayOfWeekName){
                        case "Sunday":
                            viewModel.selectSunDate = true;
                            break;
                        case "Monday":
                            viewModel.selectMonDate = true;
                            break;
                        case "Tuesday":
                            viewModel.selectTueDate = true;
                            break;
                        case "Wednesday":
                            viewModel.selectWedDate = true;
                            break;
                        case "Thursday":
                            viewModel.selectThuDate = true;
                            break;
                        case "Friday":
                            viewModel.selectFriDate = true;
                            break;
                        case "Saturday":
                            viewModel.selectSatDate = true;
                            break;
                        default:
                            break;
                    }

                }

                binding.setSun(viewModel.selectSunDate);
                binding.setMon(viewModel.selectMonDate);
                binding.setTue(viewModel.selectTueDate);
                binding.setWed(viewModel.selectWedDate);
                binding.setThu(viewModel.selectThuDate);
                binding.setFri(viewModel.selectFriDate);
                binding.setSat(viewModel.selectSatDate);

                DayOfTimeEntity entity = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().getDayOfTimeById(viewModel.habitEntity.dayOfTimeId);

                switch (entity.dayOfTimeName){
                    case "Anytime" :
                        viewModel.selectAnytime = true;
                        break;
                    case "Morning" :
                        viewModel.selectMorning = true;
                        break;
                    case "Afternoon" :
                        viewModel.selectAfternoon = true;
                        break;
                    case "Night" :
                        viewModel.selectNight = true;
                        break;
                    default:
                        break;
                }

                binding.setAnytime(viewModel.selectAnytime);
                binding.setAfternoon(viewModel.selectAfternoon);
                binding.setMorning(viewModel.selectMorning);
                binding.setNight(viewModel.selectNight);

            }
        };

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.sun_date || id == R.id.mon_date || id == R.id.tue_date || id == R.id.thu_date
            || id == R.id.fri_date || id == R.id.sat_date || id == R.id.wed_date){
            onClickDayOfWeek(id);
        }else if(id == R.id.time_afternoon || id == R.id.time_morning || id == R.id.time_any || id == R.id.time_night){
            clickBtnDateOfTime(id);
        }else if(id == R.id.btn_back){
            onClickBackBtn();
        }else if(id == R.id.btn_update){
            onClickUpdate();
        }

    }

    private void onClickDayOfWeek(int id){

        switch (id){

            case R.id.sun_date :
                if (viewModel.selectSunDate){
                    binding.sunDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.sunDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectSunDate = false;
                }else {
                    binding.sunDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.sunDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectSunDate = true;
                }
                break;
            case R.id.mon_date :
                if (viewModel.selectMonDate){
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.monDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectMonDate = false;
                }else {
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.monDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectMonDate = true;
                }
                break;
            case R.id.tue_date :
                if (viewModel.selectTueDate){
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.tueDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectTueDate = false;
                }else {
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.tueDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectTueDate = true;
                }
                break;
            case R.id.wed_date :
                if (viewModel.selectWedDate){
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.wedDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectWedDate = false;
                }else {
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.wedDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectWedDate = true;
                }
                break;
            case R.id.thu_date :
                if (viewModel.selectThuDate){
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.thuDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectThuDate = false;
                }else {
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.thuDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectThuDate = true;
                }
                break;
            case R.id.fri_date :
                if (viewModel.selectFriDate){
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.friDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectFriDate = false;
                }else {
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.friDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectFriDate = true;
                }
                break;
            case R.id.sat_date :
                if (viewModel.selectSatDate){
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.satDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectSatDate = false;
                }else {
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.satDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectSatDate = true;
                }
                break;
            default:
                break;

        }

    }

    public void clickBtnDateOfTime(int id){

        binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAfternoon.setTextColor(Color.parseColor("#000000"));
        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
        viewModel.selectAfternoon = false;

        binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAny.setTextColor(Color.parseColor("#000000"));
        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
        viewModel.selectAnytime = false;

        binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tMorning.setTextColor(Color.parseColor("#000000"));
        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
        viewModel.selectMorning = false;

        binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tNight.setTextColor(Color.parseColor("#000000"));
        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
        viewModel.selectNight = false;

        switch (id) {

            case R.id.time_afternoon :

                binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAfternoon.setTextColor(Color.parseColor("#FFFFFF"));
                binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                viewModel.selectAfternoon = true;
                break;

            case R.id.time_any :

                binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAny.setTextColor(Color.parseColor("#FFFFFF"));
                binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                viewModel.selectAnytime = true;
                break;

            case R.id.time_morning :

                binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tMorning.setTextColor(Color.parseColor("#FFFFFF"));
                binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                viewModel.selectMorning = true;
                break;

            case R.id.time_night :

                binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tNight.setTextColor(Color.parseColor("#FFFFFF"));
                binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                viewModel.selectNight = true;
                break;

            default:
                break;

        }

    }

    private void onClickBackBtn(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void onClickUpdate(){

        for (HabitInWeekEntity entity : viewModel.habitInWeekEntity){
            HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().deleteHabitInWeek(entity);
        }

        viewModel.habitInWeekEntity = new ArrayList<>();

        if(!viewModel.selectSunDate && !viewModel.selectMonDate && !viewModel.selectTueDate && !viewModel.selectWedDate && !viewModel.selectThuDate
            && !viewModel.selectFriDate && !viewModel.selectSatDate){
            Toast.makeText(getApplicationContext(), "Please choose date in week of your habit !", Toast.LENGTH_SHORT).show();
            return;
        }else {

            if(viewModel.selectSunDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(1);
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectMonDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(2);
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectTueDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(3);
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectWedDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(4);
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectThuDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(5);
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectFriDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(6);
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectSatDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(7);
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

        }

        onClickBackBtn();

    }

}
