package com.lookout.plugin.android.concurrency;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier for {@link rx.android.schedulers.AndroidSchedulers##mainThread()} scheduler.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface
MainLooper {
}
