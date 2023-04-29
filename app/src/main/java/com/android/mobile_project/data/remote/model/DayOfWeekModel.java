package com.android.mobile_project.data.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DayOfWeekModel extends BaseModel {

    private Long dayOfWeekId;
    private String dayOfWeekName;

}
