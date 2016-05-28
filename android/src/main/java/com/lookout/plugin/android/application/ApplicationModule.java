package com.lookout.plugin.android.application;

import android.app.Application;
import android.content.Context;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Injects the Android Application module which must be implemented by the application project.
 */
@Module
public class ApplicationModule {
    private Application mApplication;


    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface ApplicationQualifier {
    }

    @ApplicationScope
    @ApplicationModule.ApplicationQualifier
    @Provides
    public Context provicesApplicationContext() {
        return mApplication;
    }

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @ApplicationScope @Provides
    public Application provideApplication() {
        return mApplication;
    }
}
