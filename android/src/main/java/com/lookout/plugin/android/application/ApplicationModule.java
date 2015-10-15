package com.lookout.plugin.android.application;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Injects the Android Application module which must be implemented by the application project.
 */
@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @ApplicationScope @Provides
    public Application provideApplication() {
        return mApplication;
    }
}
