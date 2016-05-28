package com.lookout.plugin.servicerelay;

import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.servicerelay.internal.ServiceRelayService;

//@ApplicationScope
//@Component(
//        modules = {
//        })
public interface ServiceRelayComponent extends AndroidComponent {
    void inject(ServiceRelayService serviceRelayService);

    ServiceRelayIntentFactory serviceRelayIntentFactory();
}
