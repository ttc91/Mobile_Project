package com.android.mobile_project.utils.time;

public enum DayOfTime {

    ANYTIME ("Anytime"),
    AFTERNOON ("Afternoon"),
    MORNING("Morning"),
    NIGHT("Night");

    private String timeName;

    public String getTimeName() {
        return timeName;
    }

    DayOfTime(String timeName){
        this.timeName = timeName;
    }

}
