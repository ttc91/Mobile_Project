package com.android.mobile_project.time.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TimeUtils {

    private LocalDate selectedDate = LocalDate.now();

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date){

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int dayInMonth = yearMonth.lengthOfMonth();

        LocalDate firstDayOfMonth = selectedDate.withDayOfMonth(1);
        int numWeekOfMonth = firstDayOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++){

            if(i <= numWeekOfMonth || i >= numWeekOfMonth + dayInMonth){
                daysInMonthArray.add("");
            }else {
                daysInMonthArray.add(String.valueOf(i - numWeekOfMonth));
            }

        }

        return daysInMonthArray;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getMonthYearFromDate(LocalDate date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);

    }

}
