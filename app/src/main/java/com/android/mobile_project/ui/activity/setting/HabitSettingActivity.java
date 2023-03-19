package com.android.mobile_project.ui.activity.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.remote.model.DayOfTimeModel;
import com.android.mobile_project.data.remote.model.DayOfWeekModel;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.databinding.ActivityHabitSettingBinding;
import com.android.mobile_project.databinding.LayoutRemainderDialogBinding;
import com.android.mobile_project.databinding.LayoutTimePickerDialogBinding;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.time.DayOfTime;
import com.android.mobile_project.utils.time.DayOfWeek;
import com.android.mobile_project.utils.time.utils.TimeUtils;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.setting.adapter.MonthlyCalendarHabitAdapter;
import com.android.mobile_project.ui.activity.setting.adapter.RemainderAdapter;
import com.android.mobile_project.ui.activity.setting.service.InitService;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HabitSettingActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private static final String ZERO_VALUE = "00";

    private ActivityHabitSettingBinding binding;
    private LayoutTimePickerDialogBinding timerBinding;
    private LayoutRemainderDialogBinding remainderBinding;

    private final static int MAX_MINUTES_MAX = 59;
    private final static int MIN_MINUTES_MAX = 0;

    @Inject
    HabitSettingViewModel viewModel;

    public HabitSettingComponent component;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        component = ((MyApplication) getApplicationContext()).provideHabitSettingComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(initContentView());

        initViewModel();

        viewModel.initService.getHabit();
        viewModel.initService.getHabitInWeek();

        viewModel.initService.initUI();
        viewModel.initService.setCalendarOfMonthView();

    }

    @Override
    public View initContentView() {
        binding = ActivityHabitSettingBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        binding.executePendingBindings();

        viewModel.initService = new InitService() {
            @Override
            public void getHabit() {

                Bundle extras = getIntent().getExtras();
                Long habitId = extras.getLong("habitId");

                HabitModel habitModel = viewModel.getHabitByUserIdAndHabitId(habitId);
                binding.setHabit(habitModel);
                viewModel.setHabitModel(habitModel);

                viewModel.setRemainderModelList(viewModel.getRemainderListByHabitId());

            }

            @Override
            public void getHabitInWeek() {
                List<HabitInWeekModel> models = viewModel.getDayOfWeekHabitListByUserAndHabitId();
                viewModel.setHabitInWeekModelList(models);
            }

            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void initUI() {

                List<DayOfWeekModel> dayOfWeekModelList = new ArrayList<>();

                for(HabitInWeekModel model : viewModel.getHabitInWeekModelList()){
                    DayOfWeekModel dayOfWeekModel = viewModel.getDayOfWeekById(model.getDayOfWeekId());
                    dayOfWeekModelList.add(dayOfWeekModel);
                }

                for(DayOfWeekModel model : dayOfWeekModelList){

                    if (DayOfWeek.SUN.getDayName().equals(model.getDayOfWeekName())) {
                        viewModel.setSelectSunDate(true);
                    } else if (DayOfWeek.MON.getDayName().equals(model.getDayOfWeekName())) {
                        viewModel.setSelectMonDate(true);
                    } else if (DayOfWeek.TUE.getDayName().equals(model.getDayOfWeekName())) {
                        viewModel.setSelectTueDate(true);
                    } else if (DayOfWeek.WED.getDayName().equals(model.getDayOfWeekName())) {
                        viewModel.setSelectWedDate(true);
                    } else if (DayOfWeek.THU.getDayName().equals(model.getDayOfWeekName())) {
                        viewModel.setSelectThuDate(true);
                    } else if (DayOfWeek.FRI.getDayName().equals(model.getDayOfWeekName())) {
                        viewModel.setSelectFriDate(true);
                    } else if (DayOfWeek.SAT.getDayName().equals(model.getDayOfWeekName())) {
                        viewModel.setSelectSatDate(true);
                    }

                }

                binding.setSun(viewModel.isSelectSunDate());
                binding.setMon(viewModel.isSelectMonDate());
                binding.setTue(viewModel.isSelectTueDate());
                binding.setWed(viewModel.isSelectWedDate());
                binding.setThu(viewModel.isSelectThuDate());
                binding.setFri(viewModel.isSelectFriDate());
                binding.setSat(viewModel.isSelectSatDate());

                DayOfTimeModel model = viewModel.getDayOfTimeById();

                if (DayOfTime.ANYTIME.getTimeName().equals(model.getDayOfTimeName())) {
                    viewModel.setSelectAnytime(true);
                } else if (DayOfTime.MORNING.getTimeName().equals(model.getDayOfTimeName())) {
                    viewModel.setSelectMorning(true);
                } else if (DayOfTime.AFTERNOON.getTimeName().equals(model.getDayOfTimeName())) {
                    viewModel.setSelectAfternoon(true);
                } else if (DayOfTime.NIGHT.getTimeName().equals(model.getDayOfTimeName())) {
                    viewModel.setSelectNight(true);
                }

                if(viewModel.isSelectAnytime()){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                }else if(viewModel.isSelectMorning()){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                }else if(viewModel.isSelectAfternoon()){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                }else if(viewModel.isSelectNight()){
                    binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                    binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                    binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                    binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                }

                binding.setAnytime(viewModel.isSelectAnytime());
                binding.setAfternoon(viewModel.isSelectAfternoon());
                binding.setMorning(viewModel.isSelectMorning());
                binding.setNight(viewModel.isSelectNight());

                for(HabitInWeekModel m : viewModel.getHabitInWeekModelList()){
                    if(m.getTimerHour() == null && m.getTimerMinute() == null && m.getTimerSecond() == null ){
                        binding.tHour.setText(ZERO_VALUE);
                        binding.tMinutes.setText(ZERO_VALUE);
                        binding.tSecond.setText(ZERO_VALUE);
                    }else {
                        binding.tHour.setText(String.valueOf(m.getTimerHour()));
                        binding.tMinutes.setText(String.valueOf(m.getTimerMinute()));
                        binding.tSecond.setText(String.valueOf(m.getTimerSecond()));
                    }
                    break;
                }

                final FragmentManager manager = getSupportFragmentManager();

                RemainderAdapter adapter = new RemainderAdapter(getApplicationContext(), viewModel.getRemainderModelList(),manager, viewModel);
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

                timerBinding.hNumPicker.setOnValueChangedListener((numberPicker, i, i1) -> binding.tHour.setText(String.valueOf(i1)));

                timerBinding.mNumPicker.setOnValueChangedListener((numberPicker, i, i1) -> binding.tMinutes.setText(String.valueOf(i1)));

                timerBinding.sNumPicker.setOnValueChangedListener((numberPicker, i, i1) -> binding.tSecond.setText(String.valueOf(i1)));

                dialog.show();

            }

            @SuppressLint("NotifyDataSetChanged")
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

                remainderBinding.hNumPicker.setMaxValue(MAX_MINUTES_MAX);
                remainderBinding.hNumPicker.setMinValue(MIN_MINUTES_MAX);
                remainderBinding.mNumPicker.setMaxValue(MAX_MINUTES_MAX);
                remainderBinding.mNumPicker.setMinValue(MIN_MINUTES_MAX);

                remainderBinding.btnCancel.setOnClickListener(view -> dialog.dismiss());

                remainderBinding.btnSelect.setOnClickListener(view -> {

                    RemainderModel model = viewModel.checkExistRemainder((long) remainderBinding.hNumPicker.getValue(),
                            (long) remainderBinding.mNumPicker.getValue());

                    if(model != null){
                        Toast.makeText(getApplicationContext(), "Your remainder is exist !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viewModel.insertRemainder(model);
                    viewModel.setRemainderModelList(viewModel.getRemainderListByHabitId());

                    final FragmentManager manager = getSupportFragmentManager();

                    RemainderAdapter adapter = new RemainderAdapter(getApplicationContext(), viewModel.getRemainderModelList(), manager, viewModel);
                    adapter.notifyDataSetChanged();

                    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
                    layoutManager.setFlexDirection(FlexDirection.ROW);
                    layoutManager.setJustifyContent(JustifyContent.CENTER);
                    layoutManager.setAlignItems(AlignItems.CENTER);

                    binding.rcvReminder.setLayoutManager(layoutManager);
                    binding.rcvReminder.setAdapter(adapter);

                    dialog.dismiss();

                });

                dialog.show();

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void setCalendarOfMonthView() {

                String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String[] dateStringSplit = dateString.split("-");
                String getPresentMonthYear = dateStringSplit[0] + "-" + dateStringSplit[1];

                TimeUtils utils = new TimeUtils();

                String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"));

                ArrayList<String> daysInMonth = utils.daysInMonthArray();

                MonthlyCalendarHabitAdapter calendarAdapter = new MonthlyCalendarHabitAdapter(getApplicationContext(), today, daysInMonth, getPresentMonthYear,
                        viewModel.getHabitModel().getHabitId(), viewModel);
                RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

                binding.verCar.rcvCalendarVer.setLayoutManager(manager);
                binding.verCar.rcvCalendarVer.setAdapter(calendarAdapter);

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
        viewModel.deleteHabit();
        onClickBackBtn();
    }

    private void onClickRemainder(){
        viewModel.initService.initRemainderDialog(Gravity.BOTTOM);
    }

    @SuppressLint("NonConstantResourceId")
    private void onClickDayOfWeek(int id){

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

    @SuppressLint("NonConstantResourceId")
    public void clickBtnDateOfTime(int id){

        binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAfternoon.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
        viewModel.setSelectAfternoon(false);

        binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAny.setTextColor(Color.parseColor(String.valueOf(R.color.black)));
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
                binding.tAny.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
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

    private void onClickBackBtn(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void onClickUpdate(){

        for (HabitInWeekModel model : viewModel.getHabitInWeekModelList()){
            viewModel.deleteHabitInWeek(model);
        }

        viewModel.setHabitInWeekModelList(new ArrayList<>());

        if(!viewModel.isSelectSunDate() && !viewModel.isSelectMonDate() && !viewModel.isSelectTueDate() && !viewModel.isSelectWedDate() && !viewModel.isSelectThuDate()
            && !viewModel.isSelectFriDate() && !viewModel.isSelectSatDate()){
            Toast.makeText(getApplicationContext(), "Please choose date in week of your habit !", Toast.LENGTH_SHORT).show();
            return;
        }else {

            if(viewModel.isSelectSunDate()){
                HabitInWeekModel model = new HabitInWeekModel();
                model.setHabitId(viewModel.getHabitModel().getHabitId());
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setDayOfWeekId(1L);
                model.setTimerHour(Long.valueOf(binding.tHour.getText().toString().trim()));
                model.setTimerMinute(Long.valueOf(binding.tMinutes.getText().toString().trim()));
                model.setTimerSecond(Long.valueOf(binding.tSecond.getText().toString().trim()));
                viewModel.insertHabitInWeek(model);
            }

            if(viewModel.isSelectMonDate()){
                HabitInWeekModel model = new HabitInWeekModel();
                model.setHabitId(viewModel.getHabitModel().getHabitId());
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setDayOfWeekId(2L);
                model.setTimerHour(Long.valueOf(binding.tHour.getText().toString().trim()));
                model.setTimerMinute(Long.valueOf(binding.tMinutes.getText().toString().trim()));
                model.setTimerSecond(Long.valueOf(binding.tSecond.getText().toString().trim()));
                viewModel.insertHabitInWeek(model);
            }

            if(viewModel.isSelectTueDate()){
                HabitInWeekModel model = new HabitInWeekModel();
                model.setHabitId(viewModel.getHabitModel().getHabitId());
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setDayOfWeekId(3L);
                model.setTimerHour(Long.valueOf(binding.tHour.getText().toString().trim()));
                model.setTimerMinute(Long.valueOf(binding.tMinutes.getText().toString().trim()));
                model.setTimerSecond(Long.valueOf(binding.tSecond.getText().toString().trim()));
                viewModel.insertHabitInWeek(model);
            }

            if(viewModel.isSelectWedDate()){
                HabitInWeekModel model = new HabitInWeekModel();
                model.setHabitId(viewModel.getHabitModel().getHabitId());
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setDayOfWeekId(4L);
                model.setTimerHour(Long.valueOf(binding.tHour.getText().toString().trim()));
                model.setTimerMinute(Long.valueOf(binding.tMinutes.getText().toString().trim()));
                model.setTimerSecond(Long.valueOf(binding.tSecond.getText().toString().trim()));
                viewModel.insertHabitInWeek(model);
            }

            if(viewModel.isSelectThuDate()){
                HabitInWeekModel model = new HabitInWeekModel();
                model.setHabitId(viewModel.getHabitModel().getHabitId());
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setDayOfWeekId(5L);
                model.setTimerHour(Long.valueOf(binding.tHour.getText().toString().trim()));
                model.setTimerMinute(Long.valueOf(binding.tMinutes.getText().toString().trim()));
                model.setTimerSecond(Long.valueOf(binding.tSecond.getText().toString().trim()));
                viewModel.insertHabitInWeek(model);
            }

            if(viewModel.isSelectFriDate()){
                HabitInWeekModel model = new HabitInWeekModel();
                model.setHabitId(viewModel.getHabitModel().getHabitId());
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setDayOfWeekId(6L);
                model.setTimerHour(Long.valueOf(binding.tHour.getText().toString().trim()));
                model.setTimerMinute(Long.valueOf(binding.tMinutes.getText().toString().trim()));
                model.setTimerSecond(Long.valueOf(binding.tSecond.getText().toString().trim()));
                viewModel.insertHabitInWeek(model);
            }

            if(viewModel.isSelectSatDate()){
                HabitInWeekModel model = new HabitInWeekModel();
                model.setHabitId(viewModel.getHabitModel().getHabitId());
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setDayOfWeekId(7L);
                model.setTimerHour(Long.valueOf(binding.tHour.getText().toString().trim()));
                model.setTimerMinute(Long.valueOf(binding.tMinutes.getText().toString().trim()));
                model.setTimerSecond(Long.valueOf(binding.tSecond.getText().toString().trim()));
                viewModel.insertHabitInWeek(model);
            }

        }

        if(viewModel.isSelectAnytime()){
            DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.ANYTIME.getTimeName());
            viewModel.updateDateOfTimeInHabit (model.getDayOfTimeId());
        }else if(viewModel.isSelectMorning()){
            DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.MORNING.getTimeName());
            viewModel.updateDateOfTimeInHabit (model.getDayOfTimeId());
        }
        else if(viewModel.isSelectAfternoon()){
            DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.AFTERNOON.getTimeName());
            viewModel.updateDateOfTimeInHabit (model.getDayOfTimeId());
        }else if(viewModel.isSelectNight()){
            DayOfTimeModel model = viewModel.searchDayOfTimeByName(DayOfTime.NIGHT.getTimeName());
            viewModel.updateDateOfTimeInHabit (model.getDayOfTimeId());
        }

        viewModel.updateNameOfHabit(binding.hname.getText().toString().trim());

        onClickBackBtn();

    }

    private void onCLickTimePicker(){
        viewModel.initService.initTimerDialog(Gravity.BOTTOM);
    }

}
