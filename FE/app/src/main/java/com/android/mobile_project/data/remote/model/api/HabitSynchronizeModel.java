package com.android.mobile_project.data.remote.model.api;


import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.api.base.BaseSynchronizeModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HabitSynchronizeModel extends BaseSynchronizeModel<HabitModel> {}
