package com.android.mobile_project.data.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HabitInWeekModel extends BaseModel{

    private Long userId;
    private Long habitId;
    private Long dayOfWeekId;
    private Long timerHour;
    private Long timerMinute;
    private Long timerSecond;

    private boolean isTimer = false;

    public HabitInWeekModel(Long userId, Long habitId, Long dayOfWeekId, Long timerHour, Long timerMinute, Long timerSecond) {
        this.userId = userId;
        this.habitId = habitId;
        this.dayOfWeekId = dayOfWeekId;
        this.timerHour = timerHour;
        this.timerMinute = timerMinute;
        this.timerSecond = timerSecond;
        this.isTimer = isTimerHabit();
    }

    public boolean isTimerHabit() {
        if (timerHour == 0 && timerMinute == 0 && timerSecond == 0) {
            return true;
        }
        return false;
    }

}
