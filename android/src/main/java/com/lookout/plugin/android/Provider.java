package com.lookout.plugin.android;

import android.content.Context;

/**
 * Provider class takes in a dagger component type and cast the androidComponent to the type so that we can have access to them in the calling class.
 * Ex. LmsCommonComponent lmsCC = new Provider<LmsCommonComponent>().fromContext(this);
 * We use the dagger components to inject or get access to necessary objects.
 *
 * @param <V> V is the component type we want.
 */
public class Provider<V extends AndroidComponent> {
    public V fromContext(Context context) {
        // If context is the application use it directly, which simplifies mocking the context in tests.
        AndroidComponentProvider app = (AndroidComponentProvider)
                (context instanceof AndroidComponentProvider ? context : context.getApplicationContext());
        return (V) app.androidComponent();
    }
}

