package com.android.mobile_project.data.remote.model;


public class RemainderModel extends BaseModel{

    private Long remainderId;
    private Long habitId;
    private Long hourTime;
    private Long minutesTime;

    public RemainderModel(Long remainderId, Long habitId, Long hourTime, Long minutesTime) {
        this.remainderId = remainderId;
        this.habitId = habitId;
        this.hourTime = hourTime;
        this.minutesTime = minutesTime;
    }

    public Long getRemainderId() {
        return remainderId;
    }

    public void setRemainderId(Long remainderId) {
        this.remainderId = remainderId;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public Long getHourTime() {
        return hourTime;
    }

    public void setHourTime(Long hourTime) {
        this.hourTime = hourTime;
    }

    public Long getMinutesTime() {
        return minutesTime;
    }

    public void setMinutesTime(Long minutesTime) {
        this.minutesTime = minutesTime;
    }
}
