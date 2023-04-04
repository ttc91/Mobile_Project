package com.android.mobile_project.data.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryModel extends BaseModel {

    private Long historyId;
    private String historyDate;
    private String historyHabitsState;
    private Long userId;
    private Long habitId;


}
