package com.lookout.plugin.servicerelay.internal;

import android.app.Application;
import android.content.Intent;

import com.lookout.plugin.Plugin;
import com.lookout.plugin.PluginRegistry;
import com.lookout.plugin.android.application.ApplicationScope;
import com.lookout.plugin.servicerelay.ServiceRelay;
import com.lookout.plugin.servicerelay.ServiceRelayExtension;

import net.jcip.annotations.GuardedBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

@ApplicationScope
public class ServiceRelayImpl extends Plugin implements ServiceRelay {

    private final Logger mLogger = LoggerFactory.getLogger(getClass());

    private final Application mApplication;
    private final PluginRegistry mPluginRegistry;
    private final ServiceRelayExtension.Control mControl = new ControlImpl();

    // Re-use the control interface to represent the underlying service to allow mocking in tests.
    @GuardedBy("this")
    private ServiceRelayExtension.Control mService;

    // Confined to main looper
    private ServiceRelayExtension[] mExtensions;

    // Confined to main looper
    private Map<String, ServiceRelayExtension> mActions = new HashMap<>();

    @GuardedBy("this")
    private Map<Integer, String> mRunningIds = new HashMap<>();

    @GuardedBy("this")
    private int mLastStartId = -1;

    @Inject
    public ServiceRelayImpl(Application application, PluginRegistry pluginRegistry) {
        mApplication = application;
        mPluginRegistry = pluginRegistry;
    }

    @Override
    public void onApplicationCreate() {
        // No app initialization needed.
    }

    @Override
    public <T> T[] provideExtensions(Class<T> extensionPoint) {
        // Doesn't implement any extensions.
        return null;
    }

    private void initializeExtensions() {
        if (mExtensions == null) {
            mExtensions = mPluginRegistry.getExtensions(ServiceRelayExtension.class);
            for (ServiceRelayExtension extension : mExtensions) {
                for (String action : extension.getActions()) {
                    if (mActions.put(action, extension) != null) {
                        throw new IllegalArgumentException("Extension already registered for action: " + action);
                    }
                }
            }
        }
    }

    void onServiceCreate(ServiceRelayExtension.Control service) {
        synchronized (this) {
            if (mService != null) {
                throw new IllegalArgumentException("Service already created: \nmService = " + mService + "\nservice = " + service);
            }
            mService = service;
        }

        initializeExtensions();

        for (ServiceRelayExtension extension : mExtensions) {
            extension.onServiceCreate(mControl);
        }
    }

    void onServiceDestroy(ServiceRelayExtension.Control service) {
        synchronized (this) {
            // Modify mService in synchronized block to force other threads to see the updated variable state.
            if (mService != service) {
                throw new IllegalArgumentException("Service instance doesn't match in onDestroy() \nmService = " + mService + "\nservice = " + service);
            }
            mService = null;

            if (!mRunningIds.isEmpty()) {
                mLogger.error("service_relay", "Service destroyed while actions still running: " + mRunningIds);
            }
        }
        for (ServiceRelayExtension listener : mExtensions) {
            listener.onServiceDestroy();
        }
    }

    int onServiceStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        ServiceRelayExtension listener = mActions.get(action);

        synchronized (this) {
            mLastStartId = startId;
            if (listener == null) {
                throw new IllegalArgumentException("Listener not found for service intent action:\n action = " + action + "\n mActions = " + mActions.keySet());
            }
            mRunningIds.put(startId, action);
        }

        return listener.onServiceStartCommand(intent, flags, startId);
    }

    @Override
    public Intent createIntent() {
        return new Intent(mApplication, ServiceRelayService.class);
    }

    private class ControlImpl implements ServiceRelayExtension.Control {
        @Override
        public boolean stopSelfResult(int startId) {
            synchronized (this) {
                if (mService == null) {
                    mLogger.error("service_relay", "Stop requested but service already destroyed:\n startId = "
                            + startId + ",\n action = " + mRunningIds.get(startId));
                }
                if (mRunningIds.remove(startId) == null) {
                    throw new IllegalStateException("Service stop requested for start ID that doesn't match a running action: " + startId);
                }
                if (mRunningIds.isEmpty() && mService != null) {
                    mLogger.info("service_relay", "stopSelfResult() stopping service, no outstanding running startIds:");
                    return mService.stopSelfResult(mLastStartId);
                } else {
                    mLogger.info("service_relay", "stopSelfResult() not stopping service, outstanding running startIds:" + mRunningIds);
                }
            }
            return false;
        }
    }
}
