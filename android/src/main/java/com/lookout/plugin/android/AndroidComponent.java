package com.lookout.plugin.android;

import android.app.Application;
import android.app.KeyguardManager;
import android.os.Vibrator;

import com.lookout.plugin.ApplicationOnCreateListenerDispatcher;
import com.lookout.plugin.android.broadcasts.BroadcastRelay;
import com.lookout.plugin.android.concurrency.Background;
import com.lookout.plugin.android.concurrency.MainLooper;
import rx.Scheduler;

/**
 * Base interface for Dagger components in the ApplicationScope.
 * <p>
 * In order to use Dagger injection in libraries, library modules need consistent access to the
 * Application scoped object graph.  In order to inject all required dependencies, this graph needs
 * to be created by the specific application object, in a project which uses the given library.
 * ApplicationComponent is the base interface for this graph and may be extended through sub-classing
 * by other libraries which contribute objects to the application-scoped graph, e.g.:
 * <pre>
 *     //@ApplicationScope @Component( modules = { CommunicationsModule.class })
 *     public interface CommunicationsCompoennt extends ApplicationComponent {
 *         ...
 *     }
 * </pre>
 * </p>
 * Only the application which uses all these libraries needs to acutaly declare a {@link dagger.Component}
 * which extends these library components and declares all the required modules,  e.g.:
 * <pre>
 *     @ApplicationScope
 *     @Component( modules = { AndroidPluginModule.class, CommunicationsModule.class, MyAppModule.class })
 *     public interface MyAppCompoennt extends ApplicationComponent, CommunicationsCompoennt {
 *         ...
 *     }
 * </pre>
 * </p>
 * <p>
 * Using java sub-classing to extend the Dagger graph is works well because Dagger only needs to
 * construct the full graph when compiling the application project.  Trying to use Dagger's
 * component dependencies or sub-components would not be appropriate because it creates sub-graphs
 * with different life-cycles.
 * </p>
 * @see AndroidComponentProvider
 */
//@ApplicationScope
//@Component( modules = {AndroidPluginModule.class} )
public interface AndroidComponent {
    Application application();
    ApplicationOnCreateListenerDispatcher applicationOnCreateListenerDispatcher();

    @MainLooper Scheduler mainLooper();
    @Background Scheduler backgroundLooper();
    BuildWrapper buildWrapper();
    BuildConfigWrapper buildConfigWrapper();

    KeyguardManager keyguardManagerService();
    Vibrator vibratorService();

    BroadcastRelay broadcastRelay();
}
