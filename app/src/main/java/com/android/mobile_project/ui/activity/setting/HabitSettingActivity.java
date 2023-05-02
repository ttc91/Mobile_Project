package com.android.mobile_project.ui.activity.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.ActivityHabitSettingBinding;
import com.android.mobile_project.databinding.LayoutRemainderDialogBinding;
import com.android.mobile_project.databinding.LayoutTimePickerDialogBinding;
import com.android.mobile_project.ui.activity.setting.service.DbService;
import com.android.mobile_project.ui.activity.setting.service.ToastService;
import com.android.mobile_project.utils.constant.TimeConstant;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.notification.NotificationWorker;
import com.android.mobile_project.utils.time.DayOfWeek;
import com.android.mobile_project.utils.time.utils.TimeUtils;
import com.android.mobile_project.ui.InitLayout;
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

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HabitSettingActivity extends AppCompatActivity implements InitLayout, View.OnClickListener {

    private static final String ZERO_VALUE = "00";
    private static final String TAG = HabitSettingActivity.class.getSimpleName();

    private ActivityHabitSettingBinding binding;
    private LayoutTimePickerDialogBinding timerBinding;
    private LayoutRemainderDialogBinding remainderBinding;

    private final static int MAX_MINUTES_MAX = 59;
    private final static int MIN_MINUTES_MAX = 0;

    private final static int MAX_HOURS_MAX = 23;
    private final static int MIN_HOURS_MAX = 0;
    private final static String STRING_HABIT_ID = "habitId";

    private final static String format = "%02d";

    private boolean checkHabitValidName = false;

    private final DbService.InsertHabitInWeekResult insertHabitInWeekCallback = new DbService.InsertHabitInWeekResult() {
        @SuppressLint("LongLogTag")
        @Override
        public void onInsertHabitInWeekSuccess(CompositeDisposable disposable) {
            Log.i("HabitSettingActivity-insertHabitInWeekCallback", "onInsertHabitInWeekSuccess");
            disposable.clear();
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onInsertHabitInWeekFailure(CompositeDisposable disposable) {
            Log.i("HabitSettingActivity-insertHabitInWeekCallback", "onInsertHabitInWeekFailure");
            disposable.clear();
        }
    };

    private Observer<HabitModel> habitModelObserver;

    private Observer<List<HabitInWeekModel>> habitInWeekModelListObserver;

    private Observer<Long> dayOfTimeModelIdObserver;

    private Observer<Boolean> isUpdateHabitNameObserver;

    private Observer<Boolean> isDeleteHabitInWeekObserver;

    private Observer<Boolean> isUpdateDayOfTimeObserver;

    private Observer<Boolean> isInsertOrRemoveRemainderModelListObserver;

    private Observer<Integer> isUpdateRemainderModelObserver;

    @Inject
    HabitSettingViewModel viewModel;

    public HabitSettingComponent component;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i("HabitSettingActivity", "onCreate()");

        component = ((MyApplication) getApplicationContext()).provideHabitSettingComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(initContentView());

        initViewModel();

        viewModel.initService.getHabit();
        viewModel.initService.getHabitInWeek();
        viewModel.initService.getDayOfTime();
        viewModel.initService.getRemainderList();

        viewModel.initService.initUI();
        viewModel.initService.initUpdateBehavior();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
        viewModel.getMHabitModelMutableLiveData().observe(this, habitModelObserver);
        viewModel.getMHabitInWeekListMutableLiveData().observe(this, habitInWeekModelListObserver);
        viewModel.getMDayOfTimeModelIdMutableLiveData().observe(this, dayOfTimeModelIdObserver);
        viewModel.getUpdateHabitNameResultMutableLiveData().observe(this, isUpdateHabitNameObserver);
        viewModel.getDeleteHabitInWeekResultMutableLiveData().observe(this, isDeleteHabitInWeekObserver);
        viewModel.getUpdateDayOfTimeMutableLiveData().observe(this, isUpdateDayOfTimeObserver);
        viewModel.getInsertOrRemoveRemainderModelListMutableLiveData().observe(this, isInsertOrRemoveRemainderModelListObserver);
        viewModel.getUpdateRemainderMutableLiveData().observe(this, isUpdateRemainderModelObserver);
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

        viewModel.setContext(getApplicationContext());

        binding.executePendingBindings();

        viewModel.toastService = new ToastService() {
            @Override
            public void makeUpdateHabitCompleteToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), HabitSettingConstant.CONTENT_HABIT_UPDATE_COMPLETE, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeErrorApplicationToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), HabitSettingConstant.CONTENT_ERROR, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeDaysOfWeekInputtedIsEmptyToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), HabitSettingConstant.CONTENT_DAY_OF_WEEK_IS_EMPTY, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeDayOfTimeInputtedIsEmptyToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), HabitSettingConstant.CONTENT_HABIT_NAME_IS_EMPTY, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeHabitNameIsExistedToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), HabitSettingConstant.CONTENT_HABIT_NAME_IS_EXISTED, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeHabitNameInputtedIsEmptyToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), HabitSettingConstant.CONTENT_HABIT_NAME_IS_EMPTY, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void makeRemainderWasExistedToast() {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), HabitSettingConstant.CONTENT_REMAINDER_WAS_EXISTED, Toast.LENGTH_SHORT).show());
            }
        };

        viewModel.initService = new InitService() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void getHabit() {

                Bundle extras = getIntent().getExtras();
                Long habitId = extras.getLong(STRING_HABIT_ID);

                viewModel.getHabitByUserIdAndHabitId(habitId, new DbService.GetHabitByUserIdAndHabitIdResult() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onGetHabitByUserIdAndHabitIdSuccess(HabitModel model, CompositeDisposable disposable) {
                        Log.i("HabitSettingActivity-getHabitByUserIdAndHabitId", "onGetHabitByUserIdAndHabitIdSuccess");
                        viewModel.setHabitModel(model);
                        binding.setHabit(model);
                        disposable.clear();
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onGetHabitByUserIdAndHabitIdFailure(CompositeDisposable disposable) {
                        Log.e("HabitSettingActivity-getHabitByUserIdAndHabitId", "onGetHabitByUserIdAndHabitIdFailure");
                        disposable.clear();
                    }
                });

                habitModelObserver = habitModel -> {

                    viewModel.initService.setCalendarOfMonthView(habitModel.getHabitId());

                    //Get day of week per Habit after accessed

                    viewModel.getDayOfWeekHabitListByUserAndHabitId(habitModel.getHabitId(), new DbService.GetHabitInWeekHabitListByUserAndHabitId() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onGetHabitInWeekHabitListByUserAndHabitIdSuccess(CompositeDisposable disposable) {
                            Log.i("HabitSettingActivity-getDayOfWeekHabitListByUserAndHabitId", "onGetHabitInWeekHabitListByUserAndHabitIdSuccess");

                            viewModel.getRemainderListByHabitId(new DbService.GetRemainderListByHabitIdResult() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onGetRemainderListByHabitIdSuccess(CompositeDisposable disposable) {
                                    Log.i("HabitSettingActivity-getRemainderListByHabitId", "onGetRemainderListByHabitIdSuccess");
                                    runOnUiThread(() -> viewModel.initService.initRemainderAdapter());
                                    disposable.clear();
                                }

                                @SuppressLint("LongLogTag")
                                @Override
                                public void onGetRemainderListByHabitIdFailurẹ(CompositeDisposable disposable) {
                                    Log.i("HabitSettingActivity-getRemainderListByHabitId", "onGetRemainderListByHabitIdFailurẹ");
                                    runOnUiThread(() -> viewModel.initService.initRemainderAdapter());
                                    disposable.clear();
                                }
                            });

                        }

                        @SuppressLint("LongLogTag")
                        @Override
                        public void onGetHabitInWeekHabitListByUserAndHabitIdFailure(CompositeDisposable disposable) {
                            Log.e("HabitSettingActivity-getDayOfWeekHabitListByUserAndHabitId", "onGetHabitInWeekHabitListByUserAndHabitIdFailure");
                            disposable.clear();
                        }
                    });

                };

            }

            @Override
            public void getHabitInWeek() {
                habitInWeekModelListObserver = habitInWeekModels -> {
                    binding.setSun(viewModel.isSelectSunDate());
                    binding.setMon(viewModel.isSelectMonDate());
                    binding.setTue(viewModel.isSelectTueDate());
                    binding.setWed(viewModel.isSelectWedDate());
                    binding.setThu(viewModel.isSelectThuDate());
                    binding.setFri(viewModel.isSelectFriDate());
                    binding.setSat(viewModel.isSelectSatDate());
                };
            }

            @Override
            public void getDayOfTime() {
                dayOfTimeModelIdObserver = aLong -> {
                    if (viewModel.isSelectAnytime()) {
                        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                    } else if (viewModel.isSelectMorning()) {
                        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                    } else if (viewModel.isSelectAfternoon()) {
                        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
                    } else if (viewModel.isSelectNight()) {
                        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
                        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
                        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
                        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                    }
                    binding.setAnytime(viewModel.isSelectAnytime());
                    binding.setAfternoon(viewModel.isSelectAfternoon());
                    binding.setMorning(viewModel.isSelectMorning());
                    binding.setNight(viewModel.isSelectNight());
                };
            }

            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void getRemainderList() {
                isInsertOrRemoveRemainderModelListObserver = aBoolean -> {
                    Log.i("HabitSettingActivity-isInsertOrRemoveRemainderModelListObserver", "onChange");
                    if (aBoolean) {
                        viewModel.getMRemainderAdapter().notifyItemInserted(viewModel.getRemainderModelList().size() - 1);
                    } else {
                        viewModel.getMRemainderAdapter().notifyDataSetChanged();
                    }
                };

                isUpdateRemainderModelObserver = integer -> runOnUiThread(() -> viewModel.getMRemainderAdapter().notifyItemChanged(integer));
            }

            @SuppressLint({"SetTextI18n"})
            @Override
            public void initUI() {
                viewModel.getMHabitInWeekListMutableLiveData().observe(HabitSettingActivity.this, habitInWeekModels -> {
                    for (HabitInWeekModel m : habitInWeekModels) {
                        if (m.getTimerHour() == null && m.getTimerMinute() == null && m.getTimerSecond() == null) {
                            binding.tHour.setText(ZERO_VALUE);
                            binding.tMinutes.setText(ZERO_VALUE);
                            binding.tSecond.setText(ZERO_VALUE);
                        } else {
                            binding.tHour.setText(String.format(format, m.getTimerHour()));
                            binding.tMinutes.setText(String.format(format, m.getTimerMinute()));
                            binding.tSecond.setText(String.format(format, m.getTimerSecond()));
                        }
                        break;
                    }
                });
            }

            @SuppressLint({"NotifyDataSetChanged", "LongLogTag"})
            @Override
            public void initRemainderAdapter() {

                Log.i("HabitSettingActivity-initRemainderAdapter", "initRemainderAdapter");

                final FragmentManager manager = getSupportFragmentManager();
                viewModel.setMRemainderAdapter(new RemainderAdapter(getApplicationContext(), viewModel.getRemainderModelList(), manager, viewModel));

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setJustifyContent(JustifyContent.CENTER);
                layoutManager.setAlignItems(AlignItems.CENTER);

                binding.rcvReminder.setLayoutManager(layoutManager);
                binding.rcvReminder.setAdapter(viewModel.getMRemainderAdapter());

            }

            @Override
            public void initUpdateBehavior() {

                isUpdateHabitNameObserver = aBoolean -> viewModel.deleteHabitInWeekByHabitId(new DbService.DeleteHabitInWeekResult() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onDeleteHabitInWeekSuccess(CompositeDisposable disposable) {
                        Log.i("HabitSettingActivity-deleteHabitInWeekByHabitId", "onDeleteHabitInWeekSuccess");
                        disposable.clear();
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onDeleteHabitInWeekFailure(CompositeDisposable disposable) {
                        Log.e("HabitSettingActivity- ", "onDeleteHabitInWeekFailure");
                        viewModel.toastService.makeErrorApplicationToast();
                        disposable.clear();
                    }
                });

                isDeleteHabitInWeekObserver = aBoolean -> {
                    Long timerHour = Long.valueOf(binding.tHour.getText().toString().trim());
                    Long timerMinutes = Long.valueOf(binding.tMinutes.getText().toString().trim());
                    Long timerSecond = Long.valueOf(binding.tSecond.getText().toString().trim());

                    if (viewModel.isSelectSunDate()) {
                        viewModel.insertHabitInWeek(DayOfWeek.SUN.getId(),
                                timerHour, timerMinutes, timerSecond,
                                insertHabitInWeekCallback
                        );
                    }

                    if (viewModel.isSelectMonDate()) {
                        viewModel.insertHabitInWeek(DayOfWeek.MON.getId(),
                                timerHour, timerMinutes, timerSecond,
                                insertHabitInWeekCallback
                        );
                    }

                    if (viewModel.isSelectTueDate()) {
                        viewModel.insertHabitInWeek(DayOfWeek.TUE.getId(),
                                timerHour, timerMinutes, timerSecond,
                                insertHabitInWeekCallback
                        );
                    }

                    if (viewModel.isSelectWedDate()) {
                        viewModel.insertHabitInWeek(DayOfWeek.WED.getId(),
                                timerHour, timerMinutes, timerSecond,
                                insertHabitInWeekCallback
                        );
                    }

                    if (viewModel.isSelectThuDate()) {
                        viewModel.insertHabitInWeek(DayOfWeek.THU.getId(),
                                timerHour, timerMinutes, timerSecond,
                                insertHabitInWeekCallback
                        );
                    }

                    if (viewModel.isSelectFriDate()) {
                        viewModel.insertHabitInWeek(DayOfWeek.FRI.getId(),
                                timerHour, timerMinutes, timerSecond,
                                insertHabitInWeekCallback
                        );
                    }

                    if (viewModel.isSelectSatDate()) {
                        viewModel.insertHabitInWeek(DayOfWeek.SAT.getId(),
                                timerHour, timerMinutes, timerSecond,
                                insertHabitInWeekCallback
                        );
                    }

                    viewModel.updateDateOfTimeInHabit(new DbService.UpdateDateOfTimeInHabitResult() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onUpdateDateOfTimeInHabitSuccess(CompositeDisposable disposable) {
                            Log.i("HabitSettingActivity-updateDateOfTimeInHabit", "onUpdateDateOfTimeInHabitSuccess");
                        }

                        @SuppressLint("LongLogTag")
                        @Override
                        public void onUpdateDateOfTimeInHabitFailure(CompositeDisposable disposable) {
                            Log.e("HabitSettingActivity-updateDateOfTimeInHabit", "onUpdateDateOfTimeInHabitFailure");
                            viewModel.toastService.makeErrorApplicationToast();
                        }
                    });
                };

                isUpdateDayOfTimeObserver = aBoolean -> onClickBackBtn();

            }

            @Override
            public void initTimerDialog(int gravity) {

                timerBinding = LayoutTimePickerDialogBinding.inflate(getLayoutInflater());

                final Dialog dialog = new Dialog(HabitSettingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(timerBinding.getRoot());
                dialog.setCancelable(true);

                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = gravity;
                window.setAttributes(windowAttributes);

                String[] timeValue = TimeConstant.getTimeDisplayValue();

                timerBinding.hNumPicker.setMinValue(0);
                timerBinding.hNumPicker.setMaxValue(59);
                timerBinding.hNumPicker.setDisplayedValues(timeValue);

                timerBinding.mNumPicker.setMinValue(0);
                timerBinding.mNumPicker.setMaxValue(59);
                timerBinding.mNumPicker.setDisplayedValues(timeValue);

                timerBinding.sNumPicker.setMinValue(0);
                timerBinding.sNumPicker.setMaxValue(59);
                timerBinding.sNumPicker.setDisplayedValues(timeValue);

                timerBinding.hNumPicker.setOnValueChangedListener((numberPicker, i, i1) -> binding.tHour.setText(String.format(format, i1)));

                timerBinding.mNumPicker.setOnValueChangedListener((numberPicker, i, i1) -> binding.tMinutes.setText(String.format(format, i1)));

                timerBinding.sNumPicker.setOnValueChangedListener((numberPicker, i, i1) -> binding.tSecond.setText(String.format(format, i1)));

                dialog.show();

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void initRemainderDialog(int gravity) {

                remainderBinding = LayoutRemainderDialogBinding.inflate(getLayoutInflater());

                final Dialog dialog = new Dialog(HabitSettingActivity.this);
                dialog.setContentView(remainderBinding.getRoot());
                dialog.setCancelable(true);

                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = gravity;
                window.setAttributes(windowAttributes);

                remainderBinding.hNumPicker.setMaxValue(MAX_HOURS_MAX);
                remainderBinding.hNumPicker.setMinValue(MIN_HOURS_MAX);
                remainderBinding.hNumPicker.setDisplayedValues(TimeConstant.getTimeDisplayValue());
                remainderBinding.mNumPicker.setMaxValue(MAX_MINUTES_MAX);
                remainderBinding.mNumPicker.setMinValue(MIN_MINUTES_MAX);
                remainderBinding.mNumPicker.setDisplayedValues(TimeConstant.getTimeDisplayValue());

                remainderBinding.btnCancel.setOnClickListener(view -> dialog.dismiss());

                remainderBinding.btnSelect.setOnClickListener(view -> {
                    viewModel.checkExistAndInsertRemainder((long) remainderBinding.hNumPicker.getValue(),
                            (long) remainderBinding.mNumPicker.getValue(), new DbService.CheckExistRemainderResult() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onCheckExistRemainderSuccess(Long remainderId, CompositeDisposable disposable) {
                                    Log.i("HabitSettingActivity-checkExistAndInsertRemainder", "onCheckExistRemainderSuccess");
                                    viewModel.toastService.makeRemainderWasExistedToast();
                                    disposable.clear();
                                }

                                @SuppressLint("LongLogTag")
                                @Override
                                public void onCheckExistRemainderFailure(CompositeDisposable disposable) {
                                    Log.i("HabitSettingActivity-checkExistAndInsertRemainder", "onCheckExistRemainderFailure");
                                    disposable.clear();
                                }
                            }
                    );
                    dialog.dismiss();
                });
                dialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void setCalendarOfMonthView(Long habitId) {

                String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String[] dateStringSplit = dateString.split("-");
                String getPresentMonthYear = dateStringSplit[0] + "-" + dateStringSplit[1];

                TimeUtils utils = new TimeUtils();

                String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"));

                ArrayList<String> daysInMonth = utils.daysInMonthArray();

                MonthlyCalendarHabitAdapter calendarAdapter = new MonthlyCalendarHabitAdapter(getApplicationContext(), today, daysInMonth, getPresentMonthYear,
                        habitId, viewModel);
                RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

                binding.verCar.rcvCalendarVer.setLayoutManager(manager);
                binding.verCar.rcvCalendarVer.setAdapter(calendarAdapter);

            }
        };

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.sun_date || id == R.id.mon_date || id == R.id.tue_date || id == R.id.thu_date
                || id == R.id.fri_date || id == R.id.sat_date || id == R.id.wed_date) {
            onClickDayOfWeek(id);
        } else if (id == R.id.time_afternoon || id == R.id.time_morning || id == R.id.time_any || id == R.id.time_night) {
            clickBtnDateOfTime(id);
        } else if (id == R.id.btn_back) {
            onClickBackBtn();
        } else if (id == R.id.btn_update) {
            onClickUpdate();
        } else if (id == R.id.btn_delete) {
            onClickDelete();
        } else if (id == R.id.btn_timer) {
            onCLickTimePicker();
        } else if (id == R.id.btn_add_reminder) {
            onClickRemainder();
        }

    }

    private void onClickDelete() {
        viewModel.deleteHabit(new DbService.DeleteHabitResult() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDeleteHabitSuccess(CompositeDisposable disposable) {
                Log.i("HabitSettingActivity-deleteHabit", "onDeleteHabitSuccess");
                disposable.clear();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onDeleteHabitFailure(CompositeDisposable disposable) {
                Log.e("HabitSettingActivity-deleteHabit", "onDeleteHabitFailure");
                disposable.clear();
            }
        });
        onClickBackBtn();
    }

    private void onClickRemainder() {
        viewModel.initService.initRemainderDialog(Gravity.BOTTOM);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    private void onClickDayOfWeek(int id) {

        switch (id) {

            case R.id.sun_date:
                if (viewModel.isSelectSunDate()) {
                    binding.sunDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.sunDate.setTextColor(getColor(R.color.black));
                    viewModel.setSelectSunDate(false);
                } else {
                    binding.sunDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.sunDate.setTextColor(getColor(R.color.white));
                    viewModel.setSelectSunDate(true);
                }
                break;
            case R.id.mon_date:
                if (viewModel.isSelectMonDate()) {
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.monDate.setTextColor(getColor(R.color.black));
                    viewModel.setSelectMonDate(false);
                } else {
                    binding.monDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.monDate.setTextColor(getColor(R.color.white));
                    viewModel.setSelectMonDate(true);
                }
                break;
            case R.id.tue_date:
                if (viewModel.isSelectTueDate()) {
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.tueDate.setTextColor(getColor(R.color.black));
                    viewModel.setSelectTueDate(false);
                } else {
                    binding.tueDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.tueDate.setTextColor(getColor(R.color.white));
                    viewModel.setSelectTueDate(true);
                }
                break;
            case R.id.wed_date:
                if (viewModel.isSelectWedDate()) {
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.wedDate.setTextColor(getColor(R.color.black));
                    viewModel.setSelectWedDate(false);
                } else {
                    binding.wedDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.wedDate.setTextColor(getColor(R.color.white));
                    viewModel.setSelectWedDate(true);
                }
                break;
            case R.id.thu_date:
                if (viewModel.isSelectThuDate()) {
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.thuDate.setTextColor(getColor(R.color.black));
                    viewModel.setSelectThuDate(false);
                } else {
                    binding.thuDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.thuDate.setTextColor(getColor(R.color.white));
                    viewModel.setSelectThuDate(true);
                }
                break;
            case R.id.fri_date:
                if (viewModel.isSelectFriDate()) {
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.friDate.setTextColor(getColor(R.color.black));
                    viewModel.setSelectFriDate(false);
                } else {
                    binding.friDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.friDate.setTextColor(getColor(R.color.white));
                    viewModel.setSelectFriDate(true);
                }
                break;
            case R.id.sat_date:
                if (viewModel.isSelectSatDate()) {
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date));
                    binding.satDate.setTextColor(getColor(R.color.black));
                    viewModel.setSelectSatDate(false);
                } else {
                    binding.satDate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_cir_date_select));
                    binding.satDate.setTextColor(getColor(R.color.white));
                    viewModel.setSelectSatDate(true);
                }
                break;
            default:
                break;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    public void clickBtnDateOfTime(int id) {

        binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAfternoon.setTextColor(getColor(R.color.black));
        binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon);
        viewModel.setSelectAfternoon(false);

        binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tAny.setTextColor(getColor(R.color.black));
        binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any);
        viewModel.setSelectAnytime(false);

        binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tMorning.setTextColor(getColor(R.color.black));
        binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning);
        viewModel.setSelectMorning(false);

        binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name));
        binding.tNight.setTextColor(getColor(R.color.black));
        binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night);
        viewModel.setSelectNight(false);

        switch (id) {

            case R.id.time_afternoon:

                binding.timeAfternoon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAfternoon.setTextColor(getColor(R.color.white));
                binding.lgAfternoon.setBackgroundResource(R.drawable.ic_lg_afternoon_white);
                viewModel.setSelectAfternoon(true);
                break;

            case R.id.time_any:

                binding.timeAny.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tAny.setTextColor(getColor(R.color.white));
                binding.lgAny.setBackgroundResource(R.drawable.ic_lg_any_white);
                viewModel.setSelectAnytime(true);
                break;

            case R.id.time_morning:

                binding.timeMorning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tMorning.setTextColor(getColor(R.color.white));
                binding.lgMorning.setBackgroundResource(R.drawable.ic_lg_morning_white);
                viewModel.setSelectMorning(true);
                break;

            case R.id.time_night:

                binding.timeNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bck_habit_name_select));
                binding.tNight.setTextColor(getColor(R.color.white));
                binding.lgNight.setBackgroundResource(R.drawable.ic_lg_night_white);
                viewModel.setSelectNight(true);
                break;

            default:
                break;

        }

    }

    private void onClickBackBtn() {
        finish();
    }

    private void onClickUpdate() {

        String habitName = binding.hname.getText().toString().trim();

        if (habitName.equals("")) {
            viewModel.toastService.makeHabitNameInputtedIsEmptyToast();
            return;
        }

        viewModel.checkExistHabitByName(habitName,
                new DbService.GetHabitByNameResult() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onGetHabitByNameSuccess(CompositeDisposable disposable, Long habitId) {
                        Log.i("HabitSettingActivity-checkExistHabitByName", String.valueOf(habitId));
                        checkHabitValidName = false;
                        disposable.clear();
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onGetHabitByNameFailure(CompositeDisposable disposable) {
                        Log.i("HabitSettingActivity-checkExistHabitByName", "onGetHabitByNameFailure");
                        checkHabitValidName = true;
                        disposable.clear();
                    }
                }
        );

        if (checkHabitValidName) {
            if (!viewModel.isSelectSunDate() && !viewModel.isSelectMonDate() && !viewModel.isSelectTueDate() && !viewModel.isSelectWedDate() && !viewModel.isSelectThuDate()
                    && !viewModel.isSelectFriDate() && !viewModel.isSelectSatDate()) {
                viewModel.toastService.makeDaysOfWeekInputtedIsEmptyToast();
                return;
            } else {
                viewModel.updateNameOfHabit(habitName, new DbService.UpdateNameOfHabitResult() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onUpdateNameOfHabitSuccess(CompositeDisposable disposable) {
                        Log.i("HabitSettingActivity - updateNameOfHabit", "onUpdateNameOfHabitSuccess");
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onUpdateNameOfHabitFailure(CompositeDisposable disposable) {
                        Log.e("HabitSettingActivity - updateNameOfHabit", "onUpdateNameOfHabitFailure");
                    }
                });

            }
            viewModel.toastService.makeUpdateHabitCompleteToast();

        } else {
            viewModel.toastService.makeHabitNameIsExistedToast();
        }

    }

    private void onCLickTimePicker() {
        viewModel.initService.initTimerDialog(Gravity.BOTTOM);
    }

    @Override
    protected void onDestroy() {
        Log.i("HabitSettingActivity", "onDestroy");
        super.onDestroy();
        viewModel.dispose();
    }
}
