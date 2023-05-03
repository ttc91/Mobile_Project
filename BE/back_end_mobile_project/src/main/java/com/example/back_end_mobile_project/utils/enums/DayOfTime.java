package com.example.back_end_mobile_project.utils.enums;

public enum DayOfTime {

    ANYTIME (1L, "Anytime"),
    AFTERNOON (2L, "Afternoon"),
    MORNING(3L, "Morning"),
    NIGHT(4L, "Night");

    private final Long id;
    private final String timeName;

    public String getTimeName() {
        return timeName;
    }

    public Long getId(){
        return id;
    }

    DayOfTime(Long id, String timeName){
        this.id = id;
        this.timeName = timeName;
    }

}
