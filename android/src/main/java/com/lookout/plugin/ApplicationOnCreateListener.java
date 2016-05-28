package com.lookout.plugin;

import android.app.Application;

/**
 * Interface to be implemented in modules that need to perform initialization during application startup.
 *
 * @see ApplicationOnCreateListenerDispatcher
 */
public interface ApplicationOnCreateListener {

    /**
     * Called by the application upon startup.  Allows modules to inject dependencies and
     * schedule initialization tasks to be performed in background.<br>
     * Note: this method is called from {@link Application#onCreate()} on the main looper so any
     * processing performed in its implementation will delay the application's launch.
     */
    public void applicationOnCreate();
}
