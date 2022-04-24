package com.android.mobile_project.ui.activity.create;

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
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.ActivityCreateHabitBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.create.service.InitService;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.HomeFragment;

import java.util.List;

public class CreateHabitActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private ActivityCreateHabitBinding binding;
    private CreateHabitViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initContentView());
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

        viewModel = new CreateHabitViewModel();
        binding.setVm(viewModel);

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

                HabitEntity habit = new HabitEntity();

                if(binding.edtHname.getText().toString().trim().equals("") || binding.edtHname.getText().toString().trim().equals(null)){
                    Toast.makeText(getApplicationContext(), "Please input your habit name !", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    habit.habitName = binding.edtHname.getText().toString().trim();
                }

                habit.habitLogo = null;
                habit.userId = DataLocalManager.getUserId();
                habit.numOfLongestSteak = Long.valueOf(0);

                if(viewModel.selectAnytime){
                    DayOfTimeEntity time = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Anytime");
                    Long dateOfTimeId = time.dayOfTimeId;
                    habit.dayOfTimeId = dateOfTimeId;
                }else if(viewModel.selectMorning){
                    DayOfTimeEntity time = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Morning");
                    Long dateOfTimeId = time.dayOfTimeId;
                    habit.dayOfTimeId = dateOfTimeId;
                }else if(viewModel.selectNight){
                    DayOfTimeEntity time = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Night");
                    Long dateOfTimeId = time.dayOfTimeId;
                    habit.dayOfTimeId = dateOfTimeId;
                }else if(viewModel.selectAfternoon) {
                    DayOfTimeEntity time = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Afternoon");
                    Long dateOfTimeId = time.dayOfTimeId;
                    habit.dayOfTimeId = dateOfTimeId;
                }else {
                    Toast.makeText(getApplicationContext(), "Please choose your day time !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!viewModel.selectSunDate && !viewModel.selectMonDate && !viewModel.selectTueDate && !viewModel.selectWedDate
                    && !viewModel.selectThuDate && !viewModel.selectFriDate && !viewModel.selectSatDate){
                    Toast.makeText(getApplicationContext(), "Please choose your days of week !", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    HabitEntity habitCheck = HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().getHabitByName(habit.habitName);

                    if(habitCheck != null){
                        Toast.makeText(getApplicationContext(), "Your habit name is exist please input other name !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().insertHabit(habit);
                }

                habit = HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().getHabitByName(habit.habitName);
                Long habitId = habit.habitId;

                if(viewModel.selectWedDate){

                    HabitInWeekEntity habitInWeekEntity = new HabitInWeekEntity();
                    habitInWeekEntity.habitId = habitId;
                    habitInWeekEntity.dayOfWeekId = Long.valueOf(4);
                    habitInWeekEntity.userId = Long.parseLong(String.valueOf(DataLocalManager.getUserId()));

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(habitInWeekEntity);

                }

                if(viewModel.selectSunDate){

                    HabitInWeekEntity habitInWeekEntity = new HabitInWeekEntity();
                    habitInWeekEntity.habitId = habitId;
                    habitInWeekEntity.dayOfWeekId = Long.valueOf(1);
                    habitInWeekEntity.userId = Long.parseLong(String.valueOf(DataLocalManager.getUserId()));

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(habitInWeekEntity);

                }

                if(viewModel.selectMonDate){

                    HabitInWeekEntity habitInWeekEntity = new HabitInWeekEntity();
                    habitInWeekEntity.habitId = habitId;
                    habitInWeekEntity.dayOfWeekId = Long.valueOf(2);
                    habitInWeekEntity.userId = Long.parseLong(String.valueOf(DataLocalManager.getUserId()));

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(habitInWeekEntity);

                }

                if(viewModel.selectTueDate){

                    HabitInWeekEntity habitInWeekEntity = new HabitInWeekEntity();
                    habitInWeekEntity.habitId = habitId;
                    habitInWeekEntity.dayOfWeekId = Long.valueOf(3);
                    habitInWeekEntity.userId = Long.parseLong(String.valueOf(DataLocalManager.getUserId()));

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(habitInWeekEntity);

                }

                if(viewModel.selectThuDate){

                    HabitInWeekEntity habitInWeekEntity = new HabitInWeekEntity();
                    habitInWeekEntity.habitId = habitId;
                    habitInWeekEntity.dayOfWeekId = Long.valueOf(5);
                    habitInWeekEntity.userId = Long.parseLong(String.valueOf(DataLocalManager.getUserId()));

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(habitInWeekEntity);

                }

                if(viewModel.selectFriDate){

                    HabitInWeekEntity habitInWeekEntity = new HabitInWeekEntity();
                    habitInWeekEntity.habitId = habitId;
                    habitInWeekEntity.dayOfWeekId = Long.valueOf(6);
                    habitInWeekEntity.userId = Long.parseLong(String.valueOf(DataLocalManager.getUserId()));

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(habitInWeekEntity);

                }

                if(viewModel.selectSatDate){

                    HabitInWeekEntity habitInWeekEntity = new HabitInWeekEntity();
                    habitInWeekEntity.habitId = habitId;
                    habitInWeekEntity.dayOfWeekId = Long.valueOf(7);
                    habitInWeekEntity.userId = Long.parseLong(String.valueOf(DataLocalManager.getUserId()));

                    HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(habitInWeekEntity);

                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };

    }

    @Override
    public void onClick(View view) {

        Log.e("Check", "OK");

        int id = view.getId();

        if(id == R.id.btn_back){
            clickBackToHome();
        }else if (id == R.id.sun_date || id == R.id.mon_date || id == R.id.tue_date || id == R.id.wed_date || id == R.id.thu_date || id == R.id.fri_date || id == R.id.sat_date){
            clickBtnDateOfWeek(id);
        }else if(id == R.id.time_afternoon || id == R.id.time_morning || id == R.id.time_any || id == R.id.time_night){
            clickBtnDateOfTime(id);
        }else if(id == R.id.btn_create){
            viewModel.initService.initCreateHabitEvent();
            Log.e("CHECK", "CREATE OK !");
        }

    }

    public void clickBackToHome(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * <b>Event</b> : Click on day of week button
     */

    public void clickBtnDateOfWeek(int id){

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

    /**
     * <b>Event</b> : Click on day of time button
     */

    public void clickBtnDateOfTime(int id){

        binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAfternoon.setTextColor(Color.parseColor("#000000"));
        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
        viewModel.selectAfternoon = false;

        binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAnytime.setTextColor(Color.parseColor("#000000"));
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
                binding.tAnytime.setTextColor(Color.parseColor("#FFFFFF"));
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

}
