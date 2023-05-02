package com.android.mobile_project.data.remote.model.api;

import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.api.base.BaseSynchronizeModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HistorySynchronizeModel extends BaseSynchronizeModel<HistoryModel> {
}
