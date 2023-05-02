package com.example.back_end_mobile_project.utils.time;

import com.example.back_end_mobile_project.utils.enums.DayOfWeek;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DayOfWeekUtils {

    private static DayOfWeekUtils INSTANCE;

    public static DayOfWeekUtils getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DayOfWeekUtils();
        }
        return INSTANCE;
    }

    public String getDayOfWeekNameById(Long id){
        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> dayOfWeek.getId().equals(id))
                .collect(Collectors.toList()).get(0).getDayName();
    }

    public Long getDayOfWeekIdByName(String name){
        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> dayOfWeek.getDayName().equals(name))
                .collect(Collectors.toList()).get(0).getId();
    }

}
