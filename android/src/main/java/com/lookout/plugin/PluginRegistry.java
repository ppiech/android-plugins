package com.lookout.plugin;

import com.lookout.plugin.android.application.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Retrieves and returns the extensions from registered plugins for the given extension point.<br/>
     * Note: The extensions are calculated for every time this method is called for given extension
     * point.  It's up to the client to cache the returned extensions.
     * <p>
     * Plugin extensions allow for injecting specific implementations of API interfaces to be injected into
     * modules that use them.  For example, let's say module CommandReceiver declares a interface:
     * <pre>
     *     package com.lookout.commandreceiver;
     *
     *     interface CommandListener {
     *         void processCommand(String commandId, JSONObject command)
     *     }
     * </pre>
     * <br>
     * The command receiver is used by multiple modules to receiver commands from an out of process
     * service, so those modules have it as a dependency and they implement the CommandListener
     * interface:
     * <pre>
     *     package com.lookout.locate;
     *
     *     class LocateCommandListener implements CommandListener {
     *         void processCommand(String commandId, JSONObject command) {
     *             if ("locate".equals(commandId)) { ... }
     *         }
     *     }
     * </pre>
     *
     * The Locate module injects its command to the command receiver using the plugin extension
     * mechanism.
     * <pre>
     *     class LocatePlugin {
     *         @Inject LocateCommandListner mLocateCommandListener;
     *         ...
     *         @Override public <T> List<T> provideExtensions(Class<T> extensionPoint) {
     *             if (CommandListener.class.equals(extensionPoint)) {
     *                 return new CommandListener[] {  mLocateCommandListener };
     *             }
     *         }
     *     }
     * </pre>
     * </p>
     *
     * Note: clients cannot retrieve extensions from plugin registry during plugin initialization.  Instead plugins should retrieve extensions on demand as required.
     *
     * @param extensionPoint The class type for which to find extensions.
     * @param <T> Capture the extension point type from the supplied class parameter to avoid
     *           casting by the client.
     * @return All implementations of the given extensionPoint supplied by the registered plugins.
     *
     * @throws IllegalArgumentException if called whilie in Plugin#onApplicationCreate
     */
    public <T> T[] getExtensions(Class<T> extensionPoint) {
        if (mInitialized) {
            synchronized (extensionPoint) {
                List<T> list = new ArrayList<>();
                for (Plugin plugin : mPlugins) {
                    T[] pluginExtensions = plugin.provideExtensions(extensionPoint);
                    if (pluginExtensions != null) {
                        for (T extension : pluginExtensions) {
                            list.add(extension);
                        }
                    }
                }
                return covertToArray(list, extensionPoint);
            }
        } else {
            throw new IllegalStateException("Extension can not be called before onApplicationCreate completes");
        }
    }

    private <T> T[] covertToArray(List<T> list, Class<T> extensionPoint) {
        // Creates an array for the given element type
        T[] array = (T[])java.lang.reflect.Array.newInstance(extensionPoint, list.size());
        list.toArray(array);
        return array;
    }

}
