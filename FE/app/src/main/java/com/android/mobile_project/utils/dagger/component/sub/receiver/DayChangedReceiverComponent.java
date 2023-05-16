package com.android.mobile_project.utils.dagger.component.sub.receiver;

import com.android.mobile_project.receiver.local.DayChangedReceiver;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@Subcomponent
@MyCustomAnnotation.MyScope.ReceiverScope
public interface DayChangedReceiverComponent {

    @Subcomponent.Factory
    interface Factory{
        DayChangedReceiverComponent create();
    }

    void inject(DayChangedReceiver receiver);

}
