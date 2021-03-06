package com.lookout.servicerelay;

import android.app.Service;
import android.content.Intent;

import com.lookout.plugin.android.concurrency.SingleThread;
import com.lookout.plugin.servicerelay.ServiceRelayDelegate;

import java.util.concurrent.Executor;

/**
 * This service relay delegate provides an implementation similar to the
 * {@link android.app.IntentService}.  When an intent is received by the extension, it starts
 * a background thread which calls the abstract method {link #onServiceHandleIntent}, to handle the
 * intent.  Once the onServiceHandleIntent method returns, the extension signals the service relay
 * to stop the service.
 */
public abstract class IntentServiceRelayDelegate implements ServiceRelayDelegate {

    private final Executor mExecutor;

    private Control mControl;

    /**
     * Constructor for the intent service.  Overriding class must supply the executor service.  See
     * {@link SingleThread} for injecting the default executor.
     * @param executor Executor to use with this intent service.
     */
    public IntentServiceRelayDelegate(Executor executor) {
        mExecutor = executor;
    }


    /**
     * Subclasses must override this method to implement service functionality in the background
     * thread.
     * @param intent Intent that started the service.  Will not be null.
     */
    protected abstract void onServiceHandleIntent(Intent intent);


    @Override
    public void onServiceCreate(Control control) {
        synchronized (this) {
            mControl = control;
        }
    }

    @Override
    public void onServiceDestroy() {
        synchronized (this) {
            mControl = null;
        }
    }

    /**
     * NOTE: May override to return Service.REDELIER_INTENT
     */
    @Override
    public int onServiceStartCommand(final Intent intent, int flags, int startId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                onServiceHandleIntent(intent);
                synchronized (this) {
                    if (mControl != null) {
                        mControl.stopSelfResult(startId);
                    }
                }
            }
        });

        return Service.START_NOT_STICKY;
    }
}
