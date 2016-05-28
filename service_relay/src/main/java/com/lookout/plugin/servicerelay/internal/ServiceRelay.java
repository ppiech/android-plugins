package com.lookout.plugin.servicerelay.internal;

import android.app.Service;
import android.content.Intent;

import com.lookout.plugin.android.application.ApplicationScope;
import com.lookout.plugin.servicerelay.ServiceRelayDelegate;

import net.jcip.annotations.GuardedBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

@ApplicationScope
public class ServiceRelay {

    private final Logger mLogger = LoggerFactory.getLogger(getClass());

    private final ServiceRelayDelegate.Control mControl = new ControlImpl();

    // Re-use the control interface to represent the underlying service to allow mocking in tests.
    @GuardedBy("this")
    private ServiceRelayDelegate.Control mService;

    // Confined to main looper
    private final Set<ServiceRelayDelegate> mExtensions;

    // Confined to main looper
    private Map<String, ServiceRelayDelegate> mActions = null;

    @GuardedBy("this")
    private Map<Integer, String> mRunningIds = new HashMap<>();

    @GuardedBy("this")
    private int mLastStartId = -1;

    @Inject
    public ServiceRelay(Set<ServiceRelayDelegate> extensions) {
        mExtensions = extensions;
    }

    private void initializeExtensions() {
        if (mActions == null) {
            mActions = new HashMap<>();
            for (ServiceRelayDelegate extension : mExtensions) {
                for (String action : extension.getActions()) {
                    if (mActions.put(action, extension) != null) {
                        throw new IllegalArgumentException("Extension already registered for action: " + action);
                    }
                }
            }
        }
    }

    void onServiceCreate(ServiceRelayDelegate.Control service) {
        synchronized (this) {
            if (mService != null) {
                throw new IllegalArgumentException("Service already created: \nmService = " + mService + "\nservice =" +
                    " " + service);
            }
            mService = service;
        }

        initializeExtensions();

        for (ServiceRelayDelegate extension : mExtensions) {
            extension.onServiceCreate(mControl);
        }
    }

    void onServiceDestroy(ServiceRelayDelegate.Control service) {
        synchronized (this) {
            // Modify mService in synchronized block to force other threads to see the updated variable state.
            if (mService != service) {
                throw new IllegalArgumentException("Service instance doesn't match in onDestroy() \nmService = " +
                    mService + "\nservice = " + service);
            }
            mService = null;

            if (!mRunningIds.isEmpty()) {
                mLogger.error("service_relay", "Service destroyed while actions still running: " + mRunningIds);
            }
        }
        for (ServiceRelayDelegate listener : mExtensions) {
            listener.onServiceDestroy();
        }
    }

    int onServiceStartCommand(Intent intent, int flags, int startId) {
        ServiceRelayDelegate listener = null;
        if (intent != null) {
            String action = intent.getAction();
            listener = mActions.get(action);

            synchronized (this) {
                if (listener == null) {
                    mLogger.error("Listener not found for service intent action:\n action = " +
                            action + "\n mActions = " + mActions.keySet());

                } else {
                    mLastStartId = startId;
                    mRunningIds.put(startId, action);
                }
            }
        }
        if (listener == null) {
            stopSelfOnInvalidStart(startId);
            return Service.START_NOT_STICKY;
        } else {
            return listener.onServiceStartCommand(intent, flags, startId);
        }
    }

    private void stopSelfOnInvalidStart(int startId) {
        synchronized (this) {
            mLastStartId = startId;
            if (mRunningIds.isEmpty()) {
                mService.stopSelfResult(startId);
            }
        }
    }


    private class ControlImpl implements ServiceRelayDelegate.Control {
        @Override
        public boolean stopSelfResult(int startId) {
            synchronized (ServiceRelay.this) {
                if (mService == null) {
                    mLogger.error("service_relay", "Stop requested but service already destroyed:\n startId = "
                        + startId + ",\n action = " + mRunningIds.get(startId));
                }
                if (mRunningIds.remove(startId) == null) {
                    throw new IllegalStateException("Service stop requested for start ID that doesn't match a running" +
                        " action: " + startId);
                }
                if (mRunningIds.isEmpty() && mService != null) {
                    mLogger.info("service_relay", "stopSelfResult() stopping service, no outstanding running " +
                        "startIds:");
                    return mService.stopSelfResult(mLastStartId);
                }
            }
            return false;
        }
    }
}
