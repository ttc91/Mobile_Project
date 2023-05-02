package com.android.mobile_project.data.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HabitModel extends BaseModel {

    private Long habitId;

    private String habitName;

    private String habitLogo;

    private Long numOfLongestSteak;

    private Long userId;

    private String username;

    private Long dayOfTimeId;

    private Long dateOfTime;

}
