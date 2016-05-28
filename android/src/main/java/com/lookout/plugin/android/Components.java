package com.lookout.plugin.android;

import android.content.Context;

/**
 * This class contains methods to work with Dagger components.
 */
public class Components {

    /**
     * Returns the {@link AndroidComponent} by provided {@link Context}
     * instance and the component {@link Class}. If provided {@link Context}
     * is a subclass of {@link AndroidComponentProvider} then returns the component from
     * the {@link AndroidComponentProvider#androidComponent()}
     * otherwise looks for the component in the {@link Context#getApplicationContext()}
     *
     * @param context Should implement {@link AndroidComponentProvider}
     * @param componentClass A dagger component class, should implement {@link AndroidComponent}
     * @param <T> extents {@link AndroidComponent}
     * @return The dagger component, implementation of {@link AndroidComponent}
     */
    public static <T extends AndroidComponent> T from(Context context, Class<T> componentClass) {

        // If context is the application use it directly, which simplifies mocking the context in tests.
        AndroidComponentProvider app = (AndroidComponentProvider) (context instanceof AndroidComponentProvider ? context
                : context.getApplicationContext());

        AndroidComponent androidComponent = app.androidComponent();
        if (!componentClass.isAssignableFrom(androidComponent.getClass())) {
            throw new IllegalArgumentException(androidComponent.getClass().getName() + " should implement "
                    + componentClass.getName());
        }

        return (T) androidComponent;
    }

    // Suppresses default constructor, ensuring non-instantiability.
    private Components() {
    }

}
