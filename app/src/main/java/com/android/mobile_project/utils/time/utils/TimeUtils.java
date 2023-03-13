package com.android.mobile_project.utils.time.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TimeUtils {

    public TimeUtils(){}

    private final LocalDate selectedDate = LocalDate.now();

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> daysInMonthArray(){

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);

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
    public String getMonthYearFromDate(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return selectedDate.format(formatter);

    }

    public String getDateFromLocalDate(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return selectedDate.format(formatter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<LocalDate> getSixtyDaysArray()
    {

        ArrayList<LocalDate> days = new ArrayList<>();

        LocalDate previousDates = selectedDate.minusDays(29);

        while (previousDates.isBefore(selectedDate))
        {
            days.add(previousDates);
            previousDates = previousDates.plusDays(1);
        }

        days.add(selectedDate);

        LocalDate afterDate = selectedDate.plusDays(1);

        while (days.size() < 60){

            days.add(afterDate);
            afterDate = afterDate.plusDays(1);

        }

        return days;
    }

}
