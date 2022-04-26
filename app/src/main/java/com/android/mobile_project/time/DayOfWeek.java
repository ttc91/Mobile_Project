package com.android.mobile_project.time;

public enum DayOfWeek {

    SUN("Sunday"),
    MON("Monday"),
    TUE("Tuesday"),
    WED("Wednesday"),
    THU("Thursday"),
    FRI("Friday"),
    SAT("Saturday");

    public String dayName;

    private DayOfWeek(String dayName){
        this.dayName = dayName;
    }

}
