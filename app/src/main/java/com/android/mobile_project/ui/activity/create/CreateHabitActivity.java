package com.android.mobile_project.ui.activity.create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.remote.model.DayOfTimeModel;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.ActivityCreateHabitBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.create.service.InitService;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.provider.CreateHabitComponentProvider;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.time.DayOfTime;

import javax.inject.Inject;

public class CreateHabitActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private ActivityCreateHabitBinding binding;

    public CreateHabitComponent component;

    @Inject
    CreateHabitViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        component = ((CreateHabitComponentProvider) getApplicationContext()).provideCreateHabitComponent();
        component.inject(this);

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

                HabitModel habit = new HabitModel();

                if(binding.edtHname.getText().toString().trim().equals("") || binding.edtHname.getText().toString().trim().equals(null)){
                    Toast.makeText(getApplicationContext(), "Please input your habit name !", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    habit.setHabitName(binding.edtHname.getText().toString().trim());
                }

                habit.setHabitLogo(null);
                habit.setUserId(DataLocalManager.getUserId());
                habit.setNumOfLongestSteak(0L);

                if(viewModel.isSelectAnytime()){
                    DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.ANYTIME.getTimeName());
                    habit.setDayOfTimeId(model.getDayOfTimeId());
                }else if(viewModel.isSelectMorning()){
                    DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.MORNING.getTimeName());
                    habit.setDayOfTimeId(model.getDayOfTimeId());
                }else if(viewModel.isSelectNight()){
                    DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.NIGHT.getTimeName());
                    habit.setDayOfTimeId(model.getDayOfTimeId());
                }else if(viewModel.isSelectAfternoon()) {
                    DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.AFTERNOON.getTimeName());
                    habit.setDayOfTimeId(model.getDayOfTimeId());
                }else {
                    Toast.makeText(getApplicationContext(), "Please choose your day time !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!viewModel.isSelectSunDate() && !viewModel.isSelectMonDate() && !viewModel.isSelectTueDate() && !viewModel.isSelectWedDate()
                    && !viewModel.isSelectThuDate() && !viewModel.isSelectFriDate() && !viewModel.isSelectSatDate()){
                    Toast.makeText(getApplicationContext(), "Please choose your days of week !", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    if(viewModel.getHabitByName(habit.getHabitName()) != null){
                        Toast.makeText(getApplicationContext(), "Your habit name is exist please input other name !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viewModel.insertHabit(habit);
                }

                habit = viewModel.getHabitByName(habit.getHabitName());
                Long habitId = habit.getHabitId();

                if(viewModel.isSelectWedDate()){

                    HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
                    habitInWeekModel.setHabitId(habitId);
                    habitInWeekModel.setDayOfWeekId(4L);
                    habitInWeekModel.setUserId(Long.parseLong(String.valueOf(DataLocalManager.getUserId())));

                    viewModel.insertHabitInWeek(habitInWeekModel);

                }

                if(viewModel.isSelectSunDate()){

                    HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
                    habitInWeekModel.setHabitId(habitId);
                    habitInWeekModel.setDayOfWeekId(1L);
                    habitInWeekModel.setUserId(Long.parseLong(String.valueOf(DataLocalManager.getUserId())));

                    viewModel.insertHabitInWeek(habitInWeekModel);
                }

                if(viewModel.isSelectMonDate()){

                    HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
                    habitInWeekModel.setHabitId(habitId);
                    habitInWeekModel.setDayOfWeekId(2L);
                    habitInWeekModel.setUserId(Long.parseLong(String.valueOf(DataLocalManager.getUserId())));

                    viewModel.insertHabitInWeek(habitInWeekModel);
                }

                if(viewModel.isSelectTueDate()){

                    HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
                    habitInWeekModel.setHabitId(habitId);
                    habitInWeekModel.setDayOfWeekId(3L);
                    habitInWeekModel.setUserId(Long.parseLong(String.valueOf(DataLocalManager.getUserId())));

                    viewModel.insertHabitInWeek(habitInWeekModel);
                }

                if(viewModel.isSelectThuDate()){

                    HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
                    habitInWeekModel.setHabitId(habitId);
                    habitInWeekModel.setDayOfWeekId(5L);
                    habitInWeekModel.setUserId(Long.parseLong(String.valueOf(DataLocalManager.getUserId())));

                    viewModel.insertHabitInWeek(habitInWeekModel);
                }

                if(viewModel.isSelectFriDate()){

                    HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
                    habitInWeekModel.setHabitId(habitId);
                    habitInWeekModel.setDayOfWeekId(6L);
                    habitInWeekModel.setUserId(Long.parseLong(String.valueOf(DataLocalManager.getUserId())));

                    viewModel.insertHabitInWeek(habitInWeekModel);
                }

                if(viewModel.isSelectSatDate()){

                    HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
                    habitInWeekModel.setHabitId(habitId);
                    habitInWeekModel.setDayOfWeekId(7L);
                    habitInWeekModel.setUserId(Long.parseLong(String.valueOf(DataLocalManager.getUserId())));

                    viewModel.insertHabitInWeek(habitInWeekModel);
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
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
                    binding.sunDate.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
                    viewModel.setSelectSunDate(false);
                }else {
                    binding.sunDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.sunDate.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                    viewModel.setSelectSunDate(true);
                }
                break;
            case R.id.mon_date :
                if (viewModel.isSelectMonDate()){
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.monDate.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
                    viewModel.setSelectMonDate(false);
                }else {
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.monDate.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                    viewModel.setSelectMonDate(true);
                }
                break;
            case R.id.tue_date :
                if (viewModel.isSelectTueDate()){
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.tueDate.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
                    viewModel.setSelectTueDate(false);
                }else {
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.tueDate.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                    viewModel.setSelectTueDate(true);
                }
                break;
            case R.id.wed_date :
                if (viewModel.isSelectWedDate()){
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.wedDate.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
                    viewModel.setSelectWedDate(false);
                }else {
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.wedDate.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                    viewModel.setSelectWedDate(true);
                }
                break;
            case R.id.thu_date :
                if (viewModel.isSelectThuDate()){
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.thuDate.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
                    viewModel.setSelectThuDate(false);
                }else {
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.thuDate.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                    viewModel.setSelectThuDate(true);
                }
                break;
            case R.id.fri_date :
                if (viewModel.isSelectFriDate()){
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.friDate.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
                    viewModel.setSelectFriDate(false);
                }else {
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.friDate.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                    viewModel.setSelectFriDate(true);
                }
                break;
            case R.id.sat_date :
                if (viewModel.isSelectSatDate()){
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.satDate.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
                    viewModel.setSelectSatDate(false);
                }else {
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.satDate.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
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
        binding.tAfternoon.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
        viewModel.setSelectAfternoon(false);

        binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAnytime.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
        viewModel.setSelectAnytime(false);

        binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tMorning.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
        viewModel.setSelectMorning(false);

        binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tNight.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
        viewModel.setSelectNight(false);

        switch (id) {

            case R.id.time_afternoon :

                binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAfternoon.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                viewModel.setSelectAfternoon(true);
                break;

            case R.id.time_any :

                binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAnytime.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                viewModel.setSelectAnytime(true);
                break;

            case R.id.time_morning :

                binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tMorning.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                viewModel.setSelectMorning(true);
                break;

            case R.id.time_night :

                binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tNight.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                viewModel.setSelectNight(true);
                break;

            default:
                break;

        }

    }

}
