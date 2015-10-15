package com.lookout.plugin.servicerelay;

import com.lookout.plugin.Plugin;
import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.servicerelay.internal.ServiceRelayService;

//@ApplicationScope
//@Component(
//        modules = {
//                AndroidPluginModule.class,
//                ServiceRelayPluginModule.class
//        })
public interface ServiceRelayComponent extends AndroidComponent {
    void inject(ServiceRelayService serviceRelayService);

    ServiceRelay serviceRelay();
    @ServiceRelayPluginModule.PluginQualifier Plugin serviceRelayPlugin();
}
