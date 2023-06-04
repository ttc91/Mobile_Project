package com.android.mobile_project.ui.activity.main.fragment.step;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.repository.StepHistoryRepository;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.step.StepCounterUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.FragmentScope
public class ScheduleCounterStepViewModel extends ViewModel {

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final String TAG = ScheduleCounterStepViewModel.class.getSimpleName();

    private String tvWorkTodayVal;

    private String tvStepTodayVal;

    private String tvDistanceTodayVal;

    private float sundayPercentVal;

    private float mondayPercentVal;

    private float tuesdayPercentVal;

    private float wednesdayPercentVal;

    private float thursdayPercentVal;

    private float fridayPercentVal;

    private float saturdayPercentVal;

    private float stepWeekPercentVal;

    private String tvStepWeekVal;

    private String tvWorkWeekVal;

    private String tvDistanceWeekVal;

    private Long tvStepTotalWeek = 0L;

    private MutableLiveData<Float> mutableMondayPercentLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> mutableTuesdayPercentLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> mutableWednesdayPercentLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> mutableThursdayPercentLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> mutableFridayPercentLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> mutableSaturdayPercentLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> mutableSundayPercentLiveData = new MutableLiveData<>();

    private MutableLiveData<String> mutableWorkValTodayLiveData = new MutableLiveData<>();

    private MutableLiveData<String> mutableStepValTodayLiveData = new MutableLiveData<>();

    private MutableLiveData<String> mutableDistanceValTodayLiveData = new MutableLiveData<>();

    private  MutableLiveData<String> mutableStepWeekLiveData = new MutableLiveData<>();

    private MutableLiveData<String> mutableWorkLiveData = new MutableLiveData<>();

    private MutableLiveData<String> mutableDistanceLiveData = new MutableLiveData<>();

    public MutableLiveData<Float> getMutableMondayPercentLiveData() {
        return mutableMondayPercentLiveData;
    }

    public MutableLiveData<Float> getMutableTuesdayPercentLiveData() {
        return mutableTuesdayPercentLiveData;
    }

    public MutableLiveData<Float> getMutableWednesdayPercentLiveData() {
        return mutableWednesdayPercentLiveData;
    }

    public MutableLiveData<Float> getMutableThursdayPercentLiveData() {
        return mutableThursdayPercentLiveData;
    }

    public MutableLiveData<Float> getMutableFridayPercentLiveData() {
        return mutableFridayPercentLiveData;
    }

    public MutableLiveData<Float> getMutableSaturdayPercentLiveData() {
        return mutableSaturdayPercentLiveData;
    }

    public MutableLiveData<Float> getMutableSundayPercentLiveData() {
        return mutableSundayPercentLiveData;
    }

    public MutableLiveData<String> getMutableWorkValTodayLiveData() {
        return mutableWorkValTodayLiveData;
    }

    public MutableLiveData<String> getMutableStepValTodayLiveData() {
        return mutableStepValTodayLiveData;
    }

    public MutableLiveData<String> getMutableDistanceValTodayLiveData() {
        return mutableDistanceValTodayLiveData;
    }

    public MutableLiveData<String> getMutableStepWeekLiveData() {
        return mutableStepWeekLiveData;
    }

    public MutableLiveData<String> getMutableWorkLiveData() {
        return mutableWorkLiveData;
    }

    public MutableLiveData<String> getMutableDistanceLiveData() {
        return mutableDistanceLiveData;
    }

    public Long getTvStepTotalWeek() {
        return tvStepTotalWeek;
    }

    public void setTvStepTotalWeek(Long tvStepTotalWeek) {
        this.tvStepTotalWeek = tvStepTotalWeek;
    }

    public String getTvWorkTodayVal() {
        return tvWorkTodayVal;
    }

    public void setTvWorkTodayVal(String tvWorkTodayVal) {
        this.tvWorkTodayVal = tvWorkTodayVal;
    }

    public String getTvStepTodayVal() {
        return tvStepTodayVal;
    }

    public void setTvStepTodayVal(String tvStepTodayVal) {
        this.tvStepTodayVal = tvStepTodayVal;
    }

    public String getTvDistanceTodayVal() {
        return tvDistanceTodayVal;
    }

    public void setTvDistanceTodayVal(String tvDistanceTodayVal) {
        this.tvDistanceTodayVal = tvDistanceTodayVal;
    }

    public float getSundayPercentVal() {
        return sundayPercentVal;
    }

    public void setSundayPercentVal(float sundayPercentVal) {
        this.sundayPercentVal = sundayPercentVal;
    }

    public float getMondayPercentVal() {
        return mondayPercentVal;
    }

    public void setMondayPercentVal(float mondayPercentVal) {
        this.mondayPercentVal = mondayPercentVal;
    }

    public float getTuesdayPercentVal() {
        return tuesdayPercentVal;
    }

    public void setTuesdayPercentVal(float tuesdayPercentVal) {
        this.tuesdayPercentVal = tuesdayPercentVal;
    }

    public float getWednesdayPercentVal() {
        return wednesdayPercentVal;
    }

    public void setWednesdayPercentVal(float wednesdayPercentVal) {
        this.wednesdayPercentVal = wednesdayPercentVal;
    }

    public float getThursdayPercentVal() {
        return thursdayPercentVal;
    }

    public void setThursdayPercentVal(float thursdayPercentVal) {
        this.thursdayPercentVal = thursdayPercentVal;
    }

    public float getFridayPercentVal() {
        return fridayPercentVal;
    }

    public void setFridayPercentVal(float fridayPercentVal) {
        this.fridayPercentVal = fridayPercentVal;
    }

    public float getSaturdayPercentVal() {
        return saturdayPercentVal;
    }

    public void setSaturdayPercentVal(float saturdayPercentVal) {
        this.saturdayPercentVal = saturdayPercentVal;
    }

    public float getStepWeekPercentVal() {
        return stepWeekPercentVal;
    }

    public void setStepWeekPercentVal(float stepWeekPercentVal) {
        this.stepWeekPercentVal = stepWeekPercentVal;
    }

    public String getTvStepWeekVal() {
        return tvStepWeekVal;
    }

    public void setTvStepWeekVal(String tvStepWeekVal) {
        this.tvStepWeekVal = tvStepWeekVal;
    }

    public String getTvWorkWeekVal() {
        return tvWorkWeekVal;
    }

    public void setTvWorkWeekVal(String tvWorkWeekVal) {
        this.tvWorkWeekVal = tvWorkWeekVal;
    }

    public String getTvDistanceWeekVal() {
        return tvDistanceWeekVal;
    }

    public void setTvDistanceWeekVal(String tvDistanceWeekVal) {
        this.tvDistanceWeekVal = tvDistanceWeekVal;
    }

    private final StepHistoryRepository stepHistoryRepository;

    @Inject
    public ScheduleCounterStepViewModel(StepHistoryRepository stepHistoryRepository) {
        this.stepHistoryRepository = stepHistoryRepository;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scheduleToday(){
        try {
            Log.i(TAG, "scheduleToday");

            tvStepTodayVal = String.valueOf(StepCounterUtils.currentStep());
            mutableStepValTodayLiveData.postValue(tvStepTodayVal);

            tvDistanceTodayVal = String.valueOf(StepCounterUtils.currentStepToKm());
            mutableDistanceValTodayLiveData.postValue(tvDistanceTodayVal);

            tvWorkTodayVal = String.valueOf(StepCounterUtils.currentStepToKcal());
            mutableStepValTodayLiveData.postValue(tvStepTodayVal);

        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void scheduleWeek(){

        Log.i(TAG, "scheduleWeek");

        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        String mondayFormat = monday.format(DateTimeFormatter.ofPattern(DAY_FORMAT));

        LocalDate tuesday = LocalDate.now().with(DayOfWeek.TUESDAY);
        String tuesdayFormat = tuesday.format(DateTimeFormatter.ofPattern(DAY_FORMAT));

        LocalDate wednesday = LocalDate.now().with(DayOfWeek.WEDNESDAY);
        String wednesdayFormat = wednesday.format(DateTimeFormatter.ofPattern(DAY_FORMAT));

        LocalDate thursday = LocalDate.now().with(DayOfWeek.THURSDAY);
        String thursdayFormat = thursday.format(DateTimeFormatter.ofPattern(DAY_FORMAT));

        LocalDate friday = LocalDate.now().with(DayOfWeek.FRIDAY);
        String fridayFormat = friday.format(DateTimeFormatter.ofPattern(DAY_FORMAT));

        LocalDate saturday = LocalDate.now().with(DayOfWeek.SATURDAY);
        String saturdayFormat = saturday.format(DateTimeFormatter.ofPattern(DAY_FORMAT));

        LocalDate sunday = LocalDate.now().with(DayOfWeek.SUNDAY);
        String sundayFormat = sunday.format(DateTimeFormatter.ofPattern(DAY_FORMAT));

        //get percent for monday
        try{
            Log.i(TAG, "cal monday percent !");
            mondayPercentVal = (float) (stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(mondayFormat).getStepValue() /
                    DataLocalManager.getInstance().getCounterStepValue());
            mutableMondayPercentLiveData.postValue(mondayPercentVal);
            Log.i("MondayPercent", String.valueOf(mondayPercentVal));
            tvStepTotalWeek = stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(mondayFormat).getStepValue();
        } catch (Exception e) {
            mondayPercentVal = 0.0F;
            Log.i("MondayPercent", String.valueOf(mondayPercentVal));
            mutableMondayPercentLiveData.postValue(mondayPercentVal);
            Log.e(TAG, e.getMessage());
        }

        //get percent for tuesday
        try{
            Log.i(TAG, "cal tuesday percent !");
            tuesdayPercentVal = (float) (stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(tuesdayFormat).getStepValue() /
                    DataLocalManager.getInstance().getCounterStepValue());
            mutableTuesdayPercentLiveData.postValue(tuesdayPercentVal);
            Log.i("TuesdayPercent", String.valueOf(tuesdayPercentVal));
            tvStepTotalWeek += stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(tuesdayFormat).getStepValue();
        } catch (Exception e) {
            tuesdayPercentVal = 0.0F;
            Log.i("TuesdayPercent", String.valueOf(tuesdayPercentVal));
            mutableTuesdayPercentLiveData.postValue(tuesdayPercentVal);
            Log.e(TAG, e.getMessage());
        }

        //get percent for wednesday
        try{
            Log.i(TAG, "cal wednesday percent !");
            wednesdayPercentVal = (float) (stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(wednesdayFormat).getStepValue() /
                    DataLocalManager.getInstance().getCounterStepValue());
            mutableWednesdayPercentLiveData.postValue(wednesdayPercentVal);
            Log.i("WednesdayPercent", String.valueOf(wednesdayPercentVal));
            tvStepTotalWeek += stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(wednesdayFormat).getStepValue();
        } catch (Exception e) {
            wednesdayPercentVal = 0.0F;
            Log.i("WednesdayPercent", String.valueOf(wednesdayPercentVal));
            mutableWednesdayPercentLiveData.postValue(wednesdayPercentVal);
            Log.e(TAG, e.getMessage());
        }

        //get percent for thursday
        try{
            Log.i(TAG, "cal thursday percent !");
            thursdayPercentVal = (float) (stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(thursdayFormat).getStepValue() /
                    DataLocalManager.getInstance().getCounterStepValue());
            mutableThursdayPercentLiveData.postValue(thursdayPercentVal);
            Log.i("ThursdayPercent", String.valueOf(thursdayPercentVal));
            tvStepTotalWeek += stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(thursdayFormat).getStepValue();
        } catch (Exception e) {
            thursdayPercentVal = 0.0F;
            Log.i("ThursdayPercent", String.valueOf(thursdayPercentVal));
            mutableThursdayPercentLiveData.postValue(thursdayPercentVal);
            Log.e(TAG, e.getMessage());
        }

        //get percent for friday
        try{
            Log.i(TAG, "cal friday percent !");
            fridayPercentVal = (float) (stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(fridayFormat).getStepValue() /
                    DataLocalManager.getInstance().getCounterStepValue());
            mutableFridayPercentLiveData.postValue(fridayPercentVal);
            Log.i("FridayPercent", String.valueOf(fridayPercentVal));
            tvStepTotalWeek += stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(fridayFormat).getStepValue();
        } catch (Exception e) {
            fridayPercentVal = 0.0F;
            Log.i("FridayPercent", String.valueOf(fridayPercentVal));
            mutableFridayPercentLiveData.postValue(fridayPercentVal);
            Log.e(TAG, e.getMessage());
        }

        //get percent for saturday
        try{
            Log.i(TAG, "cal saturday percent !");
            saturdayPercentVal = (float) (stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(saturdayFormat).getStepValue() /
                    DataLocalManager.getInstance().getCounterStepValue());
            mutableSaturdayPercentLiveData.postValue(saturdayPercentVal);
            Log.i("SaturdayPercent", String.valueOf(saturdayPercentVal));
            tvStepTotalWeek += stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(saturdayFormat).getStepValue();
        } catch (Exception e) {
            saturdayPercentVal = 0.0F;
            Log.i("SaturdayPercent", String.valueOf(saturdayPercentVal));
            mutableSaturdayPercentLiveData.postValue(saturdayPercentVal);
            Log.e(TAG, e.getMessage());
        }

        //get percent for sunday
        try{
            Log.i(TAG, "cal sunday percent !");
            sundayPercentVal = (float) (stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(sundayFormat).getStepValue() /
                    DataLocalManager.getInstance().getCounterStepValue());
            mutableSundayPercentLiveData.postValue(sundayPercentVal);
            Log.i("SundayPercent", String.valueOf(sundayPercentVal));
            tvStepTotalWeek += stepHistoryRepository.getMStepHistoryDataSource()
                    .findOneByDate(sundayFormat).getStepValue();
        } catch (Exception e) {
            sundayPercentVal = 0.0F;
            Log.i("SundayPercent", String.valueOf(sundayPercentVal));
            mutableSundayPercentLiveData.postValue(sundayPercentVal);
            Log.e(TAG, e.getMessage());
        }

        Log.i("tvWorkWeekVal", String.valueOf(tvStepTotalWeek));
        tvWorkWeekVal = String.valueOf(StepCounterUtils
                .particularStepToKcal(tvStepTotalWeek));
        tvDistanceWeekVal = String.valueOf(StepCounterUtils
                .particularStepToKm(tvStepTotalWeek));
        mutableStepWeekLiveData.postValue(String.valueOf(tvStepTotalWeek));


    }

}
