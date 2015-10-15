package com.lookout.plugin.android.preferences;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier for executors indicating that the given executor uses a single thread.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Default {
}
