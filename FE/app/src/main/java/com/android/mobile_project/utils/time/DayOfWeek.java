package com.android.mobile_project.utils.time;

public enum DayOfWeek {

    SUN(1L,"Sunday"),
    MON(2L,"Monday"),
    TUE(3L,"Tuesday"),
    WED(4L,"Wednesday"),
    THU(5L,"Thursday"),
    FRI(6L,"Friday"),
    SAT(7L,"Saturday");

    private final Long id;
    private final String dayName;

    public Long getId(){
        return id;
    }

    public String getDayName() {
        return dayName;
    }

    DayOfWeek(Long id, String dayName){
        this.id = id;
        this.dayName = dayName;
    }

}
