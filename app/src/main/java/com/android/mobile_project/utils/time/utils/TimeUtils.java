package com.android.mobile_project.utils.time.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.utils.time.DayOfWeek;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TimeUtils {

    private static TimeUtils mInstance;

    public TimeUtils() {
    }

    public static TimeUtils getInstance() {
        if (mInstance == null) {
            mInstance = new TimeUtils();
        }
        return mInstance;
    }

    private final LocalDate selectedDate = LocalDate.now();

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public String getDateTodayString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return selectedDate.format(formatter);
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> daysInMonthArray() {

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);

        int dayInMonth = yearMonth.lengthOfMonth();

        LocalDate firstDayOfMonth = selectedDate.withDayOfMonth(1);
        int numWeekOfMonth = firstDayOfMonth.getDayOfWeek().getValue();
        Log.i("checkkkk", String.valueOf(numWeekOfMonth));

        for (int i = 1; i <= 42; i++) {

            if (i <= numWeekOfMonth || i > numWeekOfMonth + dayInMonth) {
                daysInMonthArray.add("");
                Log.i("i <= numWeekOfMonth || i >= numWeekOfMonth + dayInMonth", "");
            } else {
                daysInMonthArray.add(String.valueOf(i - numWeekOfMonth));
                Log.i("else", String.valueOf(i - numWeekOfMonth));
            }

        }

        return daysInMonthArray;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getMonthYearFromDate() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return selectedDate.format(formatter);

    }

    public String getDateFromLocalDate() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return selectedDate.format(formatter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<LocalDate> getSixtyDaysArray() {

        ArrayList<LocalDate> days = new ArrayList<>();

        LocalDate previousDates = selectedDate.minusDays(29);

        while (previousDates.isBefore(selectedDate)) {
            days.add(previousDates);
            previousDates = previousDates.plusDays(1);
        }

        days.add(selectedDate);

        LocalDate afterDate = selectedDate.plusDays(1);

        while (days.size() < 60) {

            days.add(afterDate);
            afterDate = afterDate.plusDays(1);

        }

        return days;
    }

    public Long getDayOfWeekId(String date) {
        Long dayOfWeekId = 0L;
        LocalDate localDate = LocalDate.parse(date);
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek.toString().equalsIgnoreCase(DayOfWeek.MON.getDayName())) {
            dayOfWeekId = DayOfWeek.MON.getId();
        } else if (dayOfWeek.toString().equalsIgnoreCase(DayOfWeek.TUE.getDayName())) {
            dayOfWeekId = DayOfWeek.TUE.getId();
        } else if (dayOfWeek.toString().equalsIgnoreCase(DayOfWeek.WED.getDayName())) {
            dayOfWeekId = DayOfWeek.WED.getId();
        } else if (dayOfWeek.toString().equalsIgnoreCase(DayOfWeek.THU.getDayName())) {
            dayOfWeekId = DayOfWeek.THU.getId();
        } else if (dayOfWeek.toString().equalsIgnoreCase(DayOfWeek.FRI.getDayName())) {
            dayOfWeekId = DayOfWeek.FRI.getId();
        } else if (dayOfWeek.toString().equalsIgnoreCase(DayOfWeek.SAT.getDayName())) {
            dayOfWeekId = DayOfWeek.SAT.getId();
        } else if (dayOfWeek.toString().equalsIgnoreCase(DayOfWeek.SUN.getDayName())) {
            dayOfWeekId = DayOfWeek.SUN.getId();
        }
        return dayOfWeekId;
    }

    public Long getCurrentDayOfWeekId(String dayName) {
        Long dayOfWeekId = 0L;
        if (dayName.equals(DayOfWeek.MON.getDayName())) {
            dayOfWeekId = DayOfWeek.MON.getId();
        } else if (dayName.equals(DayOfWeek.TUE.getDayName())) {
            dayOfWeekId = DayOfWeek.TUE.getId();
        } else if (dayName.equals(DayOfWeek.WED.getDayName())) {
            dayOfWeekId = DayOfWeek.WED.getId();
        } else if (dayName.equals(DayOfWeek.THU.getDayName())) {
            dayOfWeekId = DayOfWeek.THU.getId();
        } else if (dayName.equals(DayOfWeek.FRI.getDayName())) {
            dayOfWeekId = DayOfWeek.FRI.getId();
        } else if (dayName.equals(DayOfWeek.SAT.getDayName())) {
            dayOfWeekId = DayOfWeek.SAT.getId();
        } else if (dayName.equals(DayOfWeek.SUN.getDayName())) {
            dayOfWeekId = DayOfWeek.SUN.getId();
        }
        return dayOfWeekId;
    }


}
