package com.android.mobile_project.data.remote.model;

import java.io.Serializable;


public class HabitInWeekModel extends BaseModel implements Serializable {

    private Long userId;
    private Long habitId;
    private Long dayOfWeekId;
    private Long timerHour;
    private Long timerMinute;
    private Long timerSecond;

    private boolean isTimer = false;

    public HabitInWeekModel() {
    }

    public HabitInWeekModel(Long userId, Long habitId, Long dayOfWeekId, Long timerHour, Long timerMinute, Long timerSecond) {
        this.userId = userId;
        this.habitId = habitId;
        this.dayOfWeekId = dayOfWeekId;
        this.timerHour = timerHour;
        this.timerMinute = timerMinute;
        this.timerSecond = timerSecond;
        this.isTimer = isTimerHabit();
    }

    public boolean isTimerHabit() {
        return timerHour != 0 || timerMinute != 0 || timerSecond != 0;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public Long getDayOfWeekId() {
        return dayOfWeekId;
    }

    public void setDayOfWeekId(Long dayOfWeekId) {
        this.dayOfWeekId = dayOfWeekId;
    }

    public Long getTimerHour() {
        return timerHour;
    }

    public void setTimerHour(Long timerHour) {
        this.timerHour = timerHour;
    }

    public Long getTimerMinute() {
        return timerMinute;
    }

    public void setTimerMinute(Long timerMinute) {
        this.timerMinute = timerMinute;
    }

    public Long getTimerSecond() {
        return timerSecond;
    }

    public void setTimerSecond(Long timerSecond) {
        this.timerSecond = timerSecond;
    }

    public boolean isTimer() {
        return isTimer;
    }

    public void setTimer(boolean timer) {
        isTimer = timer;
    }
}
