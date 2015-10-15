package com.lookout.plugin.servicerelay;

import com.lookout.plugin.Plugin;
import com.lookout.plugin.android.application.ApplicationScope;
import com.lookout.plugin.servicerelay.internal.ServiceRelayImpl;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Module
public class ServiceRelayPluginModule {

    /**
     * Qualifier for the Plugin provided by this module.
     * <p>It allows multiple modules to provide the type {@link Plugin} without breaking dagger
     * injection rules.</p>
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface PluginQualifier {
    }

    /**
     * Cast the internal shared relay implementation to the relay interface.
     */
    @Provides @ApplicationScope
    ServiceRelay providesServiceRelay(ServiceRelayImpl serviceRelayImpl) {
        return serviceRelayImpl;
    }

    @Provides @ApplicationScope @ServiceRelayPluginModule.PluginQualifier
    Plugin providesServiceRelayPlugin(ServiceRelayImpl serviceRelayImpl) {
        return serviceRelayImpl;
    }
}