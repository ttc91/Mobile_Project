package com.android.mobile_project.data.remote.model;


public class HistoryModel extends BaseModel {

    private Long historyId;
    private String historyDate;
    private String historyHabitsState;
    private Long userId;
    private Long habitId;

    public HistoryModel() {
    }

    public HistoryModel(Long historyId, String historyDate, String historyHabitsState, Long userId, Long habitId) {
        this.historyId = historyId;
        this.historyDate = historyDate;
        this.historyHabitsState = historyHabitsState;
        this.userId = userId;
        this.habitId = habitId;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }

    public String getHistoryHabitsState() {
        return historyHabitsState;
    }

    public void setHistoryHabitsState(String historyHabitsState) {
        this.historyHabitsState = historyHabitsState;
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
}
