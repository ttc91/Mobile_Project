package com.android.mobile_project.ui.activity.count;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.ActivityProcessTimeBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.count.service.TimerService;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class CountDownActivity extends AppCompatActivity implements InitLayout, View.OnClickListener{

    private ActivityProcessTimeBinding binding;

    private CountDownComponent component;

    private Long habitId;

    @Inject
    CountDownViewModel viewModel;

    long mTimeLeftInMillis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        component = ((MyApplication) getApplicationContext()).provideCountDownComponent();
        component.inject(this);

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

        binding.setVm(viewModel);

        viewModel.initService = () -> {
            Bundle extras = getIntent().getExtras();

            HabitInWeekModel habitInWeekModel = (HabitInWeekModel) extras.getSerializable("habitInWeek");
            habitId = habitInWeekModel.getHabitId();

            viewModel.setHabitModel(viewModel.getHabitByUserIdAndHabitId(habitId));

            //List<HabitInWeekModel> list = new ArrayList<>();
            //list = viewModel.getDayOfWeekHabitListByUserAndHabitId(habitId);
            //HabitInWeekModel habitInWeekModel = list.get(0);

            viewModel.setStartTimeInMillis(TimeUnit.HOURS.toMillis(habitInWeekModel.getTimerHour()) + TimeUnit.MINUTES.toMillis(habitInWeekModel.getTimerMinute()) + TimeUnit.SECONDS.toMillis(habitInWeekModel.getTimerSecond()));
            mTimeLeftInMillis = viewModel.getStartTimeInMillis();

        };

        viewModel.timerService = new TimerService() {
            @Override
            public void startTimer() {

                viewModel.setMCountDownTimer(new CountDownTimer(mTimeLeftInMillis, 1000) {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onTick(long l) {
                        mTimeLeftInMillis = l;
                        updateCountDownText();

                        int value = (int) mTimeLeftInMillis;
                        int total = Math.toIntExact(viewModel.getStartTimeInMillis());

                        viewModel.setPercent(100 - ((float)value/total) * 100);

                        binding.cirBar.setProgress(viewModel.getPercent());

                    }

                    @Override
                    public void onFinish() {
                        viewModel.setMTimerRunning(false);
                        binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));
                        binding.cirBar.setProgress(100);
                    }
                }.start());

                viewModel.setMTimerRunning(true);

            }

            @Override
            public void pauseTimer() {

                viewModel.getMCountDownTimer().cancel();
                viewModel.setMTimerRunning(false);
                binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));

            }

            @Override
            public void resetTimer() {

                viewModel.getMCountDownTimer().cancel();
                viewModel.setMTimerRunning(false);
                mTimeLeftInMillis = viewModel.getStartTimeInMillis();
                updateCountDownText();
                binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));
                binding.cirBar.setProgress(0);

            }

        };

        viewModel.getLiveDataIsSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                finish();
            }
        });
    }

    public void updateCountDownText() {

        int h = (int) TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis);
        int m = (int) (TimeUnit.MILLISECONDS.toMinutes(mTimeLeftInMillis) % TimeUnit.HOURS.toMinutes(1));
        int s = (int) (TimeUnit.MILLISECONDS.toSeconds(mTimeLeftInMillis) % TimeUnit.MINUTES.toSeconds(1));

        String timeLeftFormatted = String.format(Locale.US, "%02d:%02d:%02d", h, m, s);

        binding.tvTime.setText(timeLeftFormatted);

    }

    public void onClickPlayTimer(){
        if(viewModel.isMTimerRunning()){
            viewModel.timerService.pauseTimer();
            binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_play));
        }else {
            viewModel.timerService.startTimer();
            binding.btnPlay.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_process_pause));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickFinish(){

        String historyTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        viewModel.updateHistoryStatus(habitId, historyTime, "true");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_back){
            finish();
        }else if(id == R.id.btn_play){
            onClickPlayTimer();
        }else if(id == R.id.btn_reset){
            viewModel.timerService.resetTimer();
        }else if(id == R.id.btn_finish){
            onClickFinish();
        }

    }
}
