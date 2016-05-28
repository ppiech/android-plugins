package com.lookout.plugin.servicerelay;

import android.content.Intent;

/**
 * Light-weight replacement for an Android service.  ServiceRelay delegates implement the
 * <code>ServiceRelayDelegate</code> interface, which mirrors Android's {@link android.app.Service}
 * API.  ServiceRelay uses a single Android service and delegates service calls to the delegates
 * injected through dagger.  <code>ServiceRelayDelegate</code> implementations must manage their own
 * background threads and must honor the API contract by calling  {@link Control#stopSelfResult(int)}
 * upon completion of every task.
 * <p>
 * To provide ServiceRelayDelegate extensions, clients should use the{@link Provides.Type#SET}
 * dagger injection, such as:
 * <pre>
 *    @Provides(type = Provides.Type.SET)
 *    @Singleton
 *    ServiceRelayDelegate providesServiceExtension(MyServiceDelegateImpl service) {
 *        return service;
 *    }
 * </pre>
 * </p>
 */
public interface ServiceRelayDelegate {

    /**
     * Returns the intent actions that this extension handles.
     * Note: extension actions must be unique for a given service.  If multiple extension use the
     * same action ID's the service will throw an exception upon creation.
     */
    String[] getActions();

    /**
     * Control interface used to interact with the underlying android service.
     */
    interface Control {
        /**
         * Requests that the service be stopped.  The underlying service will be stopped only
         * if all relay listeners have completed their commands.  If called after onServiceDestroy()
         * is called, the request will be ignored.
         * @param startId ID that was used in call to {@link ServiceRelayDelegate#onServiceStartCommand}.
         * @return true if the underlying android service will be stopped
         * @see android.app.Service#stopSelfResult(int)
         */
        boolean stopSelfResult(int startId);
    }

    /**
     * Called when the underlying android service is created.  Implementations may cache the provided
     * control and use it until {@link #onServiceDestroy()} is called.
     * @param control Control for the underlying android service.
     */
    void onServiceCreate(Control control);

    /**
     * Called when the underlying android service is destroyed.  The service may be destroyed
     * by the OS while a service command is still being processed.  Implementations need to
     * try to shut down running operations if possible.
     */
    void onServiceDestroy();

    /**
     * Called when a service intent is received that this listener is registered for.
     *
     * @see android.app.Service#onStartCommand(Intent, int, int)
     */
    int onServiceStartCommand(Intent intent, int flags, int startId);
}
