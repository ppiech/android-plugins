package com.lookout.plugin.android;

import android.content.Context;

public class Provide<V extends AndroidComponent> {
    public V fromContext(Context context) {
        // If context is the application use it directly, which simplifies mocking the context in tests.
        AndroidComponentProvider app = (AndroidComponentProvider)
                (context instanceof AndroidComponentProvider ? context : context.getApplicationContext());
        return (V) app.androidComponent();
    }
}

