package com.android.mobile_project.data.remote.model;


public class DayOfTimeModel extends BaseModel {

    private Long dayOfTimeId;
    private String dayOfTimeName;

    public DayOfTimeModel(Long dayOfTimeId, String dayOfTimeName) {
        this.dayOfTimeId = dayOfTimeId;
        this.dayOfTimeName = dayOfTimeName;
    }

    public Long getDayOfTimeId() {
        return dayOfTimeId;
    }

    public void setDayOfTimeId(Long dayOfTimeId) {
        this.dayOfTimeId = dayOfTimeId;
    }

    public String getDayOfTimeName() {
        return dayOfTimeName;
    }

    public void setDayOfTimeName(String dayOfTimeName) {
        this.dayOfTimeName = dayOfTimeName;
    }
}
