package com.android.mobile_project.ui.activity.create;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.ActivityCreateHabitBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.create.service.InitService;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.HomeFragment;

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
                    binding.sunDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.sunDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectSunDate = false;
                }else {
                    binding.sunDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.sunDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectSunDate = true;
                }
                break;
            case R.id.mon_date :
                if (viewModel.selectMonDate){
                    binding.monDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.monDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectMonDate = false;
                }else {
                    binding.monDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.monDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectMonDate = true;
                }
                break;
            case R.id.tue_date :
                if (viewModel.selectTueDate){
                    binding.tueDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.tueDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectTueDate = false;
                }else {
                    binding.tueDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.tueDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectTueDate = true;
                }
                break;
            case R.id.wed_date :
                if (viewModel.selectWedDate){
                    binding.wedDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.wedDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectWedDate = false;
                }else {
                    binding.wedDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.wedDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectWedDate = true;
                }
                break;
            case R.id.thu_date :
                if (viewModel.selectThuDate){
                    binding.thuDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.thuDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectThuDate = false;
                }else {
                    binding.thuDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.thuDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectThuDate = true;
                }
                break;
            case R.id.fri_date :
                if (viewModel.selectFriDate){
                    binding.friDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.friDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectFriDate = false;
                }else {
                    binding.friDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.friDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewModel.selectFriDate = true;
                }
                break;
            case R.id.sat_date :
                if (viewModel.selectSatDate){
                    binding.satDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.satDate.setTextColor(Color.parseColor("#000000"));
                    viewModel.selectSatDate = false;
                }else {
                    binding.satDate.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
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

        switch (id) {

            case R.id.time_afternoon :
                if (viewModel.selectAfternoon){
                    binding.timeAfternoon.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
                    binding.tAfternoon.setTextColor(Color.parseColor("#000000"));
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                    viewModel.selectAfternoon = false;
                }else {
                    binding.timeAfternoon.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                    binding.tAfternoon.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                    viewModel.selectAfternoon = true;
                }
                break;
            case R.id.time_any :
                if (viewModel.selectAnytime){
                    binding.timeAny.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
                    binding.tAnytime.setTextColor(Color.parseColor("#000000"));
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                    viewModel.selectAnytime = false;
                }else {
                    binding.timeAny.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                    binding.tAnytime.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                    viewModel.selectAnytime = true;
                }
                break;
            case R.id.time_morning :
                if (viewModel.selectMorning){
                    binding.timeMorning.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
                    binding.tMorning.setTextColor(Color.parseColor("#000000"));
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                    viewModel.selectMorning = false;
                }else {
                    binding.timeMorning.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                    binding.tMorning.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                    viewModel.selectMorning = true;
                }
                break;
            case R.id.time_night :
                if (viewModel.selectNight){
                    binding.timeNight.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
                    binding.tNight.setTextColor(Color.parseColor("#000000"));
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                    viewModel.selectNight = false;
                }else {
                    binding.timeNight.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                    binding.tNight.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                    viewModel.selectNight = true;
                }
                break;
            default:
                break;

        }

    }

}
