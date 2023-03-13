package com.android.mobile_project.utils.time;

public enum DayOfWeek {

    SUN("Sunday"),
    MON("Monday"),
    TUE("Tuesday"),
    WED("Wednesday"),
    THU("Thursday"),
    FRI("Friday"),
    SAT("Saturday");

    private String dayName;

    public String getDayName() {
        return dayName;
    }

    DayOfWeek(String dayName){
        this.dayName = dayName;
    }

}
