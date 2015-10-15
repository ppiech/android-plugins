package com.lookout.plugin.android;

import com.lookout.plugin.android.application.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Injects the debug flag captured from the application's build config flag so that it can be used
 * by libraries.
 */
@Module
public class BuildConfigModule {
    private BuildConfigWrapper mBuildConfigWrapper;

    public BuildConfigModule(BuildConfigWrapper buildConfigWrapper){
        mBuildConfigWrapper = buildConfigWrapper;
    }

    @ApplicationScope
    @Provides
    public BuildConfigWrapper provideBuildConfigWrapper() {
        return mBuildConfigWrapper;
    }

}
