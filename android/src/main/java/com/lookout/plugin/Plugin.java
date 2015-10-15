package com.lookout.plugin;

import android.app.Application;

/**
 * Interface to be implemented in modules that contribute to a plugin-based application.
 *
 * @see PluginRegistry
 */
public class Plugin {

    /**
     * Called by the PluginRegistry upon initialization.  Allows modules to inject dependencies and
     * schedule initialization tasks to be performed in background.<br>
     * Note: this method is called from {@link Application#onCreate()} on the main looper so any
     * processing performed in its implementation will delay the application's launch.
     */
    public void onApplicationCreate() {
    }
}
