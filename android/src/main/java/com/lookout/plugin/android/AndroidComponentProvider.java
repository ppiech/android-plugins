package com.lookout.plugin.android;

import android.content.Context;

/**
 * Accessor interface for the {@link AndroidComponent}.
 * <p>
 * Use of dagger in an Android application requires global access to the graph of objects
 * that share the lifecycle with the Application class.  This graph is to be made accessible to
 * services, activities, etc. through the {@link Context#getApplicationContext()} cast to the
 * {@link}AndroidComponentProvider} interface, then calling {@link #androidComponent()}.
 * </p>
 */
public interface AndroidComponentProvider {
    AndroidComponent androidComponent();
}
