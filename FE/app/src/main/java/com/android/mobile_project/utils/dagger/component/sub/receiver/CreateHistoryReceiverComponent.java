package com.android.mobile_project.utils.dagger.component.sub.receiver;

import com.android.mobile_project.receiver.local.CreateHistoryReceiver;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@Subcomponent
@MyCustomAnnotation.MyScope.ReceiverScope
public interface CreateHistoryReceiverComponent {

    @Subcomponent.Factory
    interface Factory{
        CreateHistoryReceiverComponent create();
    }

    void inject(CreateHistoryReceiver receiver);

}
