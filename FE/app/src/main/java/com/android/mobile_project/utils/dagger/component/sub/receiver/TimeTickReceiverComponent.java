package com.android.mobile_project.utils.dagger.component.sub.receiver;

import com.android.mobile_project.receiver.system.TimeTickReceiver;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@Subcomponent
@MyCustomAnnotation.MyScope.ReceiverScope
public interface TimeTickReceiverComponent {

    @Subcomponent.Factory
    interface Factory{
        TimeTickReceiverComponent create();
    }

    void inject(TimeTickReceiver receiver);

}
