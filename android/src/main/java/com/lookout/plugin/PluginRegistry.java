package com.lookout.plugin;

import com.lookout.plugin.android.application.ApplicationScope;

import javax.inject.Inject;

/**
 * Registry for a plugin-based application, which aggregates application's plugins and allows
 * access to plugin extensions.
 * <p>
 * The registry is created at application startup in the main Application's dagger graphs.  It
 * needs to be initialized by the Application's {@link android.app.Application#onCreate} with all
 * the plugins that the given application will use.  For example:
 * <pre>
 * class LookoutApplication extends Application {
 *     ...
 *     @Override public void onCreate() {
 *         super.onCreate();
 *         ...
 *          LookoutApplicationComponent component = DaggerLookoutApplicationComponent.builder()
 *          ...
 *          component.pluginRegistry().onApplicationCreate(new Plugin[] {
 *              ServiceRelayComponentProvider.fromContext(this).serviceRelayPlugin(),
 *              PushComponentProvider.fromContext(this).pushPlugin(),
 *              LocatePluginProvider.fromContext(this).locatePlugin(),
 *              ...
 *          });
 *     }
 * </pre>
 * </p>
 */
@ApplicationScope
public class PluginRegistry {

    private Plugin[] mPlugins;
    private volatile boolean mInitialized = false;

    @Inject
    public PluginRegistry() {
    }

    /**
     * Initialize registry with given plugins and call plugins' {@link Plugin#onApplicationCreate()}.
     * <br>
     * Note: this method can only be called once, during application startup.
     * Note: clients should only register listeners and perform dependency injection as needed.  All other work should be scheduled by plugins to run in a service.
     *
     * @param plugins All application plugins.
     */
    public void onApplicationCreate(Plugin[] plugins) {
        if (mPlugins != null) {
            throw new IllegalStateException("PluginRegistry already initialized");
        }
        mPlugins = plugins;
        for (Plugin plugin : mPlugins) {
            plugin.onApplicationCreate();
        }
        mInitialized = true;
    }
}
