package com.lookout.plugin.android.concurrency;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier for executors indicating creating a new thread for every runnable.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface NewThread {
}
