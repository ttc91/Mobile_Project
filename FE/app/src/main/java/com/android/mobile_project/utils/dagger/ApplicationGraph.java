package com.android.mobile_project.utils.dagger;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.utils.dagger.component.sub.count.CountDownComponent;
import com.android.mobile_project.utils.dagger.component.sub.create.CreateHabitComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.CreateHistoryReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.DayChangedReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.RebootReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.receiver.TimeTickReceiverComponent;
import com.android.mobile_project.utils.dagger.component.sub.service.AutoInsertServiceComponent;
import com.android.mobile_project.utils.dagger.component.sub.setting.HabitSettingComponent;
import com.android.mobile_project.utils.dagger.component.sub.input.InputComponent;
import com.android.mobile_project.utils.dagger.component.sub.main.MainComponent;
import com.android.mobile_project.utils.dagger.module.ApplicationModule;
import com.android.mobile_project.utils.dagger.module.DatabaseModule;
import com.android.mobile_project.utils.dagger.module.RetrofitModule;
import com.android.mobile_project.utils.dagger.module.SubComponentModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        DatabaseModule.class,
        RetrofitModule.class,
        SubComponentModule.class
})
public interface ApplicationGraph {

    void inject(MyApplication application);

    /**
     * Used to declare Component when start app.
     */

    /**
     * <b>Activity</b>
     * @return activity component
     */
    MainComponent.Factory mMainComponent();
    HabitSettingComponent.Factory mHabitSettingComponent();
    InputComponent.Factory mInputComponent();
    CountDownComponent.Factory mCountDownComponent();
    CreateHabitComponent.Factory mCreateHabitComponent();

    /**
     * <b>Receiver</b>
     * @return receiver component
     */
    CreateHistoryReceiverComponent.Factory mCreateHistoryReceiverComponent();
    DayChangedReceiverComponent.Factory mDayChangedReceiverComponent();
    RebootReceiverComponent.Factory mRebootReceiverComponent();
    TimeTickReceiverComponent.Factory mTimeTickReceiverComponent();

    /**
     * <b>Service</b>
     * @return service component
     */
    AutoInsertServiceComponent.Factory mAutoInsertServiceComponent();

}
