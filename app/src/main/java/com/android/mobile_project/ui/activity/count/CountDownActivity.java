package com.android.mobile_project.ui.activity.count;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.ActivityProcessTimeBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.count.service.InitService;
import com.android.mobile_project.ui.activity.count.service.TimerService;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.HomeFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CountDownActivity extends AppCompatActivity implements InitLayout, View.OnClickListener{

    ActivityProcessTimeBinding binding;
    CountDownViewModel viewModel;
    long mTimeLeftInMillis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        initViewModel();

        viewModel.initService.init();
        updateCountDownText();
    }

    @Override
    public View initContentView() {

        binding = ActivityProcessTimeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public void initViewModel() {

        viewModel = new CountDownViewModel();
        binding.setVm(viewModel);

        viewModel.initService = new InitService() {
            @Override
            public void init() {
                Bundle extras = getIntent().getExtras();

                Long habitId = extras.getLong("habitId");
                viewModel.habitEntity = HabitTrackerDatabase.getInstance(getApplicationContext()).habitDao().getHabitByUserIdAndHabitId(DataLocalManager.getUserId(), habitId);

                List<HabitInWeekEntity> list = new ArrayList<>();
                list = HabitTrackerDatabase.getInstance(getApplicationContext()).habitInWeekDao().getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getUserId(), habitId);
                HabitInWeekEntity habitInWeekEntity = list.get(0);

                viewModel.START_TIME_IN_MILLIS = TimeUnit.HOURS.toMillis(habitInWeekEntity.timerHour) + TimeUnit.MINUTES.toMillis(habitInWeekEntity.timerMinute) + TimeUnit.SECONDS.toMillis(habitInWeekEntity.timerSecond);
                mTimeLeftInMillis = viewModel.START_TIME_IN_MILLIS;

            }
        };

        viewModel.timerService = new TimerService() {
            @Override
            public void startTimer() {

                viewModel.mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onTick(long l) {
                        mTimeLeftInMillis = l;
                        updateCountDownText();

                        int value = (int) mTimeLeftInMillis;
                        int total = Math.toIntExact(viewModel.START_TIME_IN_MILLIS);

                        viewModel.percent = 100 - ((float)value/total) * 100;

                        binding.cirBar.setProgress(viewModel.percent);

                    }

                    @Override
                    public void onFinish() {
                        viewModel.mTimerRunning = false;
                        binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));
                        binding.cirBar.setProgress(100);
                    }
                }.start();

                viewModel.mTimerRunning = true;

            }

            @Override
            public void pauseTimer() {

                viewModel.mCountDownTimer.cancel();
                viewModel.mTimerRunning = false;
                binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));

            }

            @Override
            public void resetTimer() {

                viewModel.mCountDownTimer.cancel();
                viewModel.mTimerRunning = false;
                mTimeLeftInMillis = viewModel.START_TIME_IN_MILLIS;
                updateCountDownText();
                binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));
                binding.cirBar.setProgress(0);

            }

        };
    }

    public void updateCountDownText() {

        int h = (int) TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis);
        int m = (int) (TimeUnit.MILLISECONDS.toMinutes(mTimeLeftInMillis) % TimeUnit.HOURS.toMinutes(1));
        int s = (int) (TimeUnit.MILLISECONDS.toSeconds(mTimeLeftInMillis) % TimeUnit.MINUTES.toSeconds(1));

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);

        binding.tvTime.setText(timeLeftFormatted);

    }

    public void onClickBack(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onClickPlayTimer(){
        if(viewModel.mTimerRunning){
            viewModel.timerService.pauseTimer();
            binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));
        }else {
            viewModel.timerService.startTimer();
            binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_pause));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickFinish(){

        LocalDate local = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String historyTime = local.format(formatter);

        HistoryEntity entity = HabitTrackerDatabase.getInstance(getApplicationContext()).historyDao().getHistoryByHabitIdAndDate(viewModel.habitEntity.habitId, historyTime);
        entity.historyHabitsState = "true";
        HabitTrackerDatabase.getInstance(getApplicationContext()).historyDao().updateHistory(entity);

        onClickBack();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_back){
            onClickBack();
        }else if(id == R.id.btn_play){
            onClickPlayTimer();
        }else if(id == R.id.btn_reset){
            viewModel.timerService.resetTimer();
        }else if(id == R.id.btn_finish){
            onClickFinish();
        }

    }
}
