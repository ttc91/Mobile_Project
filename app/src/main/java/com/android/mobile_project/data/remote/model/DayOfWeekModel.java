package com.android.mobile_project.data.remote.model;


public class DayOfWeekModel extends BaseModel {

    private Long dayOfWeekId;
    private String dayOfWeekName;

    public DayOfWeekModel(Long dayOfWeekId, String dayOfWeekName) {
        this.dayOfWeekId = dayOfWeekId;
        this.dayOfWeekName = dayOfWeekName;
    }

    public Long getDayOfWeekId() {
        return dayOfWeekId;
    }

    public void setDayOfWeekId(Long dayOfWeekId) {
        this.dayOfWeekId = dayOfWeekId;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    public void setDayOfWeekName(String dayOfWeekName) {
        this.dayOfWeekName = dayOfWeekName;
    }
}
