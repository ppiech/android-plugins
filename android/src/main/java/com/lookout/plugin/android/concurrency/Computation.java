package com.lookout.plugin.android.concurrency;

import rx.schedulers.Schedulers;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier for {@link Schedulers#computation()} scheduler. */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Computation {
}
