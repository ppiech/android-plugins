package com.lookout.plugin.servicerelay;

import android.content.Intent;

/**
 * Interface for interacting with an Android service.  Allows for multiple modules to share an
 * underlying android service.
 */
public interface ServiceRelay {

    /**
     * Creates an intent to start the android service.
     * @return Intent initialized with correct component.  Caller must call {@link Intent#setAction(String)}
     * to specify action to be invoked.
     */
    Intent createIntent();
}
