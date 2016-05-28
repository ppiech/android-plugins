package com.lookout.plugin.android.concurrency;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier for executor that uses a single thread for all tasks (like IntentService).
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface SingleThread {
}
