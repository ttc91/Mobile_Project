package com.android.mobile_project.data.remote.model;


public class HabitModel extends BaseModel {

    private Long habitId;
    private String habitName;
    private String habitLogo;
    private Long numOfLongestSteak;
    private Long userId;
    private Long dayOfTimeId;

    public HabitModel() {
    }

    public HabitModel(Long habitId, String habitName, String habitLogo, Long numOfLongestSteak, Long userId, Long dayOfTimeId) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.habitLogo = habitLogo;
        this.numOfLongestSteak = numOfLongestSteak;
        this.userId = userId;
        this.dayOfTimeId = dayOfTimeId;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getHabitLogo() {
        return habitLogo;
    }

    public void setHabitLogo(String habitLogo) {
        this.habitLogo = habitLogo;
    }

    public Long getNumOfLongestSteak() {
        return numOfLongestSteak;
    }

    public void setNumOfLongestSteak(Long numOfLongestSteak) {
        this.numOfLongestSteak = numOfLongestSteak;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDayOfTimeId() {
        return dayOfTimeId;
    }

    public void setDayOfTimeId(Long dayOfTimeId) {
        this.dayOfTimeId = dayOfTimeId;
    }
}
