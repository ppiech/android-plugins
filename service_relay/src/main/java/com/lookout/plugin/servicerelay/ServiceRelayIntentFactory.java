package com.lookout.plugin.servicerelay;

import android.app.Application;
import android.content.Intent;

import com.lookout.plugin.servicerelay.internal.ServiceRelayService;

import javax.inject.Inject;

/**
 * Factory for intents to start Service Relay.
 * @see ServiceRelayDelegate
 */
public class ServiceRelayIntentFactory {

    private final Application mApplication;

    @Inject
    public ServiceRelayIntentFactory(Application application) {
        mApplication = application;
    }

    /**
     * Creates an intent to start the Service Relay android service. Caller must call {@link Intent#setAction(String)}
     * on the returned intnet to specify action to be invoked.
     *
     * @return Intent initialized with correct component.
     */
    public Intent createIntent() {
        return new Intent(mApplication, ServiceRelayService.class);
    }

}
