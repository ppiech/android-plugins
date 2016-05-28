package com.lookout.plugin;

import com.lookout.plugin.android.application.ApplicationScope;

import java.util.Set;

import javax.inject.Inject;

/**
 * Registry for listeners that need to be called during application startup.
 * <p>
 * The registry is created at application startup in the main Application's dagger graphs.  It
 * needs to be initialized by the Application's {@link android.app.Application#onCreate}
 * For example:
 * <pre>
 * class LookoutApplication extends Application {
 *     ...
 *     @Override public void onCreate() {
 *         super.onCreate();
 *         ...
 *          LookoutApplicationComponent component = DaggerLookoutApplicationComponent.builder()
 *          ...
 *          component.applicationOnCreateListenerDispatcher().applicationOnCreate();
 *     }
 * </pre>
 * </p>
 */
@ApplicationScope
public class ApplicationOnCreateListenerDispatcher {

    private Set<ApplicationOnCreateListener> mApplicationOnCreateListeners;

    @Inject
    public ApplicationOnCreateListenerDispatcher(Set<ApplicationOnCreateListener> applicationOnCreateListeners) {
        mApplicationOnCreateListeners = applicationOnCreateListeners;
    }

    private boolean mInitialized = false;

    /**
     * Call registered listners' {@link ApplicationOnCreateListener#applicationOnCreate()}.
     * <br>
     * Note: this method can only be called once, during application startup.
     * Note: clients should only register listeners and perform dependency injection as needed.  All other work
     * should be scheduled by plugins to run in a service.
     */
    public void onApplicationCreate() {
        if (mInitialized) {
            throw new IllegalStateException("Already Initialized");
        }
        mInitialized = true;
        for (ApplicationOnCreateListener applicationOnCreateListener : mApplicationOnCreateListeners) {
            applicationOnCreateListener.applicationOnCreate();
        }
    }
}
