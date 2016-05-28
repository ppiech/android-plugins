package com.lookout.plugin.android.concurrency;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class ConcurrencyModule {
    /**
     * Creates a single-thread pool executor.  E.g. to use with intent service. <br/>  R
     *
     * Note: each service should get it's own thread, so this declaration does not use the
     * {@ link @ApplicationScope}.
     */
    @Provides
    @SingleThread
    Executor providesSingleThreadPoolExecutor() {
        return new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * Creates an rxjava Scheduler that runs tasks in Android UI thread.
     */
    @Provides
    @MainLooper
    Scheduler providesMainLooperScheduler() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * Creates an rxjava Scheduler that runs tasks a background thread pool.
     */
    @Provides
    @Background
    Scheduler providesBackgroundScheduler() {
        return Schedulers.io();
    }

    /**
     * Creates an rxjava Scheduler that runs tasks in a single background thread.
     */
    @Provides
    @SingleThread
    Scheduler providesSingleThreadScheduler(@SingleThread Executor executor) {
        return Schedulers.from(executor);
    }

    /**
     * Creates an rxjava Scheduler that runs tasks a background thread pool, limited by number of CPUs in the system.
     */
    @Provides
    @Computation
    Scheduler providesComputationScheduler() {
        return Schedulers.computation();
    }

    /**
     * Creates an rxjava Scheduler that creates a new thread for each task.
     */
    @Provides
    @NewThread
    Scheduler providesNewThreadScheduler() {
        return Schedulers.newThread();
    }
}
