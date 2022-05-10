package com.android.mobile_project.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.print.PageRange;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.model.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.model.db.RemainderEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.ActivityHabitSettingBinding;
import com.android.mobile_project.databinding.LayoutRemainderDialogBinding;
import com.android.mobile_project.databinding.LayoutTimePickerDialogBinding;
import com.android.mobile_project.time.DayOfWeek;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.setting.adapter.RemainderAdapter;
import com.android.mobile_project.ui.activity.setting.service.InitService;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class HabitSettingActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private ActivityHabitSettingBinding binding;
    private LayoutTimePickerDialogBinding timerBinding;
    private LayoutRemainderDialogBinding remainderBinding;
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

                viewModel.remainderEntityList = HabitTrackerDatabase.getInstance(getApplicationContext()).remainderDao().getRemainderListByHabitId(viewModel.habitEntity.habitId);

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

                if(viewModel.selectAnytime){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                }else if(viewModel.selectMorning){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                }else if(viewModel.selectAfternoon){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                }else if(viewModel.selectNight){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                }

                binding.setAnytime(viewModel.selectAnytime);
                binding.setAfternoon(viewModel.selectAfternoon);
                binding.setMorning(viewModel.selectMorning);
                binding.setNight(viewModel.selectNight);

                for(HabitInWeekEntity en : viewModel.habitInWeekEntity){
                    if(en.timerHour == null && en.timerMinute == null && en.timerSecond == null ){
                        binding.tHour.setText("00");
                        binding.tMinutes.setText("00");
                        binding.tSecond.setText("00");
                    }else {
                        binding.tHour.setText(String.valueOf(en.timerHour));
                        binding.tMinutes.setText(String.valueOf(en.timerMinute));
                        binding.tSecond.setText(String.valueOf(en.timerSecond));
                    }
                    break;
                }

                final FragmentManager manager = getSupportFragmentManager();

                RemainderAdapter adapter = new RemainderAdapter(getApplicationContext(), viewModel.remainderEntityList,manager, viewModel.habitEntity);
                adapter.notifyDataSetChanged();

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setJustifyContent(JustifyContent.CENTER);
                layoutManager.setAlignItems(AlignItems.CENTER);

                binding.rcvReminder.setLayoutManager(layoutManager);
                binding.rcvReminder.setAdapter(adapter);

            }

            @Override
            public void initTimerDialog(int gravity) {

                timerBinding = LayoutTimePickerDialogBinding.inflate(getLayoutInflater());

                final Dialog dialog = new Dialog(HabitSettingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(timerBinding.getRoot());
                dialog.setCancelable(true);

                Window window = dialog.getWindow();
                if(window == null){
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = gravity;
                window.setAttributes(windowAttributes);

                timerBinding.hNumPicker.setMinValue(0);
                timerBinding.hNumPicker.setMaxValue(59);

                timerBinding.mNumPicker.setMinValue(0);
                timerBinding.mNumPicker.setMaxValue(59);

                timerBinding.sNumPicker.setMinValue(0);
                timerBinding.sNumPicker.setMaxValue(59);

                timerBinding.hNumPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        binding.tHour.setText(String.valueOf(i1));
                    }
                });

                timerBinding.mNumPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        binding.tMinutes.setText(String.valueOf(i1));
                    }
                });

                timerBinding.sNumPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        binding.tSecond.setText(String.valueOf(i1));
                    }
                });

                dialog.show();

            }

            @Override
            public void initRemainderDialog(int gravity) {

                remainderBinding = LayoutRemainderDialogBinding.inflate(getLayoutInflater());

                final Dialog dialog = new Dialog(HabitSettingActivity.this);
                dialog.setContentView(remainderBinding.getRoot());
                dialog.setCancelable(true);

                Window window = dialog.getWindow();
                if(window == null){
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = gravity;
                window.setAttributes(windowAttributes);

                remainderBinding.hNumPicker.setMaxValue(59);
                remainderBinding.hNumPicker.setMinValue(0);
                remainderBinding.mNumPicker.setMaxValue(59);
                remainderBinding.mNumPicker.setMinValue(0);

                remainderBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                remainderBinding.btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemainderEntity entity = new RemainderEntity();
                        entity.habitId = viewModel.habitEntity.habitId;
                        entity.hourTime = Long.valueOf(remainderBinding.hNumPicker.getValue());
                        entity.minutesTime = Long.valueOf(remainderBinding.mNumPicker.getValue());

                        RemainderEntity remainderEntity = HabitTrackerDatabase.getInstance(getApplicationContext()).remainderDao().checkExistRemainder(entity.hourTime, entity.minutesTime, entity.habitId);
                        if(remainderEntity != null){
                            Toast.makeText(getApplicationContext(), "Your remainder is exist !", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        HabitTrackerDatabase.getInstance(getApplicationContext()).remainderDao().insertRemainder(entity);

                        viewModel.remainderEntityList = HabitTrackerDatabase.getInstance(getApplicationContext()).remainderDao().getRemainderListByHabitId(viewModel.habitEntity.habitId);

                        final FragmentManager manager = getSupportFragmentManager();

                        RemainderAdapter adapter = new RemainderAdapter(getApplicationContext(), viewModel.remainderEntityList, manager, viewModel.habitEntity);
                        adapter.notifyDataSetChanged();

                        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
                        layoutManager.setFlexDirection(FlexDirection.ROW);
                        layoutManager.setJustifyContent(JustifyContent.CENTER);
                        layoutManager.setAlignItems(AlignItems.CENTER);

                        binding.rcvReminder.setLayoutManager(layoutManager);
                        binding.rcvReminder.setAdapter(adapter);

                        dialog.dismiss();

                    }
                });

                dialog.show();

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
        }else if(id == R.id.btn_update) {
            onClickUpdate();
        }else if(id == R.id.btn_delete){
            onClickDelete();
        }else if(id == R.id.btn_timer){
            onCLickTimePicker();
        }else if(id == R.id.btn_add_reminder){
            onClickRemainder();
        }

    }

    private void onClickDelete(){
        HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().deleteHabit(viewModel.habitEntity);
        onClickBackBtn();
    }

    private void onClickRemainder(){
        viewModel.initService.initRemainderDialog(Gravity.BOTTOM);
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
                entity.timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                entity.timerMinute = Long.valueOf(binding.tMinutes.getText().toString().trim());
                entity.timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectMonDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(2);
                entity.timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                entity.timerMinute = Long.valueOf(binding.tMinutes.getText().toString().trim());
                entity.timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectTueDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(3);
                entity.timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                entity.timerMinute = Long.valueOf(binding.tMinutes.getText().toString().trim());
                entity.timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectWedDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(4);
                entity.timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                entity.timerMinute = Long.valueOf(binding.tMinutes.getText().toString().trim());
                entity.timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectThuDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(5);
                entity.timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                entity.timerMinute = Long.valueOf(binding.tMinutes.getText().toString().trim());
                entity.timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectFriDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(6);
                entity.timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                entity.timerMinute = Long.valueOf(binding.tMinutes.getText().toString().trim());
                entity.timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

            if(viewModel.selectSatDate){
                HabitInWeekEntity entity = new HabitInWeekEntity();
                entity.habitId = viewModel.habitEntity.habitId;
                entity.userId = DataLocalManager.getUserId();
                entity.dayOfWeekId = Long.valueOf(7);
                entity.timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                entity.timerMinute = Long.valueOf(binding.tMinutes.getText().toString().trim());
                entity.timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());
                HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().insertHabitInWeek(entity);
            }

        }

        if(viewModel.selectAnytime){
            DayOfTimeEntity entity = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Anytime");
            HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().updateDateOfTimeInHabit (entity.dayOfTimeId, viewModel.habitEntity.habitId);
        }else if(viewModel.selectMorning){
            DayOfTimeEntity entity = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Morning");
            HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().updateDateOfTimeInHabit (entity.dayOfTimeId, viewModel.habitEntity.habitId);
        }else if(viewModel.selectAfternoon){
            DayOfTimeEntity entity = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Afternoon");
            HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().updateDateOfTimeInHabit (entity.dayOfTimeId, viewModel.habitEntity.habitId);
        }else if(viewModel.selectNight){
            DayOfTimeEntity entity = HabitTrackerDatabase.getInstance(getApplicationContext()).dayOfTimeDao().searchDayOfTimeByName("Night");
            HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().updateDateOfTimeInHabit (entity.dayOfTimeId, viewModel.habitEntity.habitId);
        }

        String habitUpdateName = binding.hname.getText().toString().trim();
        HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().updateNameOfHabit (habitUpdateName, viewModel.habitEntity.habitId);

        onClickBackBtn();

    }

    private void onCLickTimePicker(){

        viewModel.initService.initTimerDialog(Gravity.BOTTOM);

    }

}
