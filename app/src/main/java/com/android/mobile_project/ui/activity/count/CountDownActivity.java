package com.android.mobile_project.ui.activity.count;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.ActivityProcessTimeBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.count.service.DbService;
import com.android.mobile_project.ui.activity.count.service.TimerService;
import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CountDownActivity extends AppCompatActivity implements InitLayout, View.OnClickListener{

    private ActivityProcessTimeBinding binding;

    private Long mStartTimeInMillis;

    private CountDownComponent component;

    private Long habitId;

    @Inject
    CountDownViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        component = ((MyApplication) getApplicationContext()).provideCountDownComponent();
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        initViewModel();

        viewModel.initService.init();
    }

    @Override
    public View initContentView() {
        binding = ActivityProcessTimeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        viewModel.initService = () -> {
            Bundle extras = getIntent().getExtras();
            viewModel.setHabitId(extras.getLong("habitId"));
            viewModel.getDayOfWeekHabitListByUserAndHabitId(viewModel.getHabitId(), new DbService.GetDayOfWeekHabitListByUserAndHabitIdResult() {
                @SuppressLint("LongLogTag")
                @Override
                public void onGetDayOfWeekHabitListByUserAndHabitIdSuccess(Long startTimeInMillis, CompositeDisposable disposable) {
                    Log.i("CountDownActivity-getDayOfWeekHabitListByUserAndHabitId", "onGetDayOfWeekHabitListByUserAndHabitIdSuccess");
                    mStartTimeInMillis = startTimeInMillis;
                    updateCountDownText();
                    disposable.clear();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onGetDayOfWeekHabitListByUserAndHabitIdFailure(CompositeDisposable disposable) {
                    Log.i("CountDownActivity-getDayOfWeekHabitListByUserAndHabitId", "onGetDayOfWeekHabitListByUserAndHabitIdSuccess");
                    disposable.clear();
                }
            });
        };

        viewModel.timerService = new TimerService() {
            @Override
            public void startTimer() {

                viewModel.setMCountDownTimer(new CountDownTimer(mStartTimeInMillis, 1000) {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onTick(long l) {
                        updateCountDownText();
                        mStartTimeInMillis = l;
                        int value = (int) l;
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
                mStartTimeInMillis = viewModel.getStartTimeInMillis();
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

        int h = (int) TimeUnit.MILLISECONDS.toHours(mStartTimeInMillis);
        int m = (int) (TimeUnit.MILLISECONDS.toMinutes(mStartTimeInMillis) % TimeUnit.HOURS.toMinutes(1));
        int s = (int) (TimeUnit.MILLISECONDS.toSeconds(mStartTimeInMillis) % TimeUnit.MINUTES.toSeconds(1));

        String timeLeftFormatted = String.format(Locale.US, "%02d:%02d:%02d", h, m, s);

        binding.tvTime.setText(timeLeftFormatted);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickBack(){
        finish();
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

        viewModel.updateHistoryStatusTrueWithUserIdAndHabitIdAndDate(viewModel.getHabitId(), historyTime, new DbService.UpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateResult() {
            @SuppressLint("LongLogTag")
            @Override
            public void onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateSuccess(CompositeDisposable disposable) {
                Log.i("CountDownActivity-onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateSuccess",
                        "onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateSuccess");
                disposable.clear();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateFailure(CompositeDisposable disposable) {
                Log.e("CountDownActivity-onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateFailure",
                        "onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateSuccess");
                disposable.clear();
            }
        });

        onClickBack();

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
