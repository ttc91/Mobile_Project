package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.StepHistoryEntity;

public interface StepHistoryDataSource extends BaseDataSource<StepHistoryEntity>{

    StepHistoryEntity findOneByDate(String date);

}
