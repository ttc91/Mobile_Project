package com.android.mobile_project.utils.dagger.component.sub.receiver;

import com.android.mobile_project.receiver.system.RebootReceiver;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import dagger.Subcomponent;

@Subcomponent
@MyCustomAnnotation.MyScope.ReceiverScope
public interface RebootReceiverComponent {

    @Subcomponent.Factory
    interface Factory{
        RebootReceiverComponent create();
    }

    void inject(RebootReceiver receiver);

}
