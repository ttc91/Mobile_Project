package com.example.back_end_mobile_project.utils.time;

import com.example.back_end_mobile_project.utils.enums.DayOfTime;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DayOfTimeUtils {

    private static DayOfTimeUtils INSTANCE;

    public static DayOfTimeUtils getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DayOfTimeUtils();
        }
        return INSTANCE;
    }

    public String getDayOfTimeNameById(Long id){
        return Arrays.stream(DayOfTime.values())
                .filter(dayOfTime -> dayOfTime.getId().equals(id))
                .collect(Collectors.toList()).get(0).getTimeName();
    }

    public Long getDayOfTimeIdByTimeName(String timeName){
        return Arrays.stream(DayOfTime.values())
                .filter(dayOfTime -> dayOfTime.getTimeName().equals(timeName))
                .collect(Collectors.toList()).get(0).getId();
    }

}
