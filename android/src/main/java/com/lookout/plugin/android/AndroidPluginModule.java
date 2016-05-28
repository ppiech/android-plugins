package com.lookout.plugin.android;

import com.lookout.plugin.android.application.ApplicationModule;
import com.lookout.plugin.android.broadcasts.internal.BroadcastRelayModule;
import com.lookout.plugin.android.concurrency.ConcurrencyModule;
import com.lookout.plugin.android.preferences.PreferencesModule;
import com.lookout.plugin.android.services.ServicesModule;

import dagger.Module;

/**
 * Pulls together all the modules in the library so that only this one module needs to be declared
 * by the including component.
 */
@Module(includes = {
    ApplicationModule.class,
    BuildConfigModule.class,
    ConcurrencyModule.class,
    PreferencesModule.class,
    ServicesModule.class,
    BroadcastRelayModule.class})
public class AndroidPluginModule {
}
