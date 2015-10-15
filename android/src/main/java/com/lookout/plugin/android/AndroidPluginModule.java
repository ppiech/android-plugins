package com.lookout.plugin.android;

import com.lookout.plugin.android.application.ApplicationModule;
import com.lookout.plugin.android.concurrency.ConcurrencyModule;
import com.lookout.plugin.android.preferences.PreferencesModule;

import dagger.Module;
import dagger.Provides;

/**
 * Pulls together all the modules in the library so that only this one module needs to be declared
 * by the including component.
 */
@Module(includes = {
        ApplicationModule.class,
        BuildConfigModule.class,
        ConcurrencyModule.class,
        PreferencesModule.class
})
public class AndroidPluginModule {
}
