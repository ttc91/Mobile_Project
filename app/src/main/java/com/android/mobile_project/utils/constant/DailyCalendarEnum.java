package com.android.mobile_project.utils.constant;

public enum DailyCalendarEnum {

    MONDAY("MON."),
    TUESDAY("TUE."),
    WEDNESDAY("WED."),
    THURSDAY("THUR."),
    FRIDAY("FRI."),
    SATURDAY("SAT."),
    SUNDAY("SUN.");

    DailyCalendarEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue(){
        return value;
    }

}
