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

    /**
     * Returns the extensions provided by this Plugin for the given extension point.  It may be called
     * before this plugin's {@link #onApplicationCreate()} is called (in the case where another plugin's
     * {@code onAppicationCreate()} requests its extensions.
     *
     * @see PluginRegistry#getExtensions(Class)
     * @param extensionPoint Should be examined by implementation before returning its extensions.
     * @param <T> Capture the extension point type from the supplied class parameter to avoid
     *           casting by the client.

     * @return Extensions supplied by this class, or null if no extensions are provided by this plugin.
     */
    public <T> T[] provideExtensions(Class<T> extensionPoint) {
        return null;
    }
}
