package com.android.mobile_project.data.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemainderModel extends BaseModel{

    private Long remainderId;
    private Long habitId;
    private Long hourTime;
    private Long minutesTime;

}
