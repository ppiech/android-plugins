package com.lookout.plugin.android;


import android.os.Build;

import com.lookout.plugin.android.application.ApplicationScope;

import javax.inject.Inject;

/**
 * Wrapper class for obtaining sdk information from {@link android.os.Build}.
 * Methods in this class should be <strong>instance</strong> methods in order to support mocking
 * this class in tests, even though {@link android.os.Build} contains statically-known
 * information.
 */
@ApplicationScope
public class BuildWrapper {

    @Inject
    public BuildWrapper() {}

    public int getSdkInt() {
        return Build.VERSION.SDK_INT;
    }
}