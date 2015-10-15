package com.lookout.plugin.android.concurrency;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;

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
}
