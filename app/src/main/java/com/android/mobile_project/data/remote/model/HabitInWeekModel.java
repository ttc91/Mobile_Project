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

}
