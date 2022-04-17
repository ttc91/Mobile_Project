package com.android.mobile_project.time;

public enum DayOfTime {

    ANYTIME ("Anytime"),
    AFTERNOON ("Afternoon"),
    MORNING("Morning"),
    NIGHT("Night");

    public String timeName;

    private DayOfTime(String timeName){
        this.timeName = timeName;
    }

}
