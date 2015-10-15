package com.lookout.plugin.rxjava.concurrency;

import com.lookout.plugin.android.concurrency.Background;
import com.lookout.plugin.android.concurrency.MainLooper;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class RxJavaModule {

    /**
     * Creates an rxjava Scheduelr that runs tasks in Android UI thread.
     */
    @Provides
    @MainLooper
    Scheduler providesMainLooperScheduler() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * Creates an rxjava Scheduelr that runs tasks a background thread pool.
     */
    @Provides
    @Background
    Scheduler providesBackgroundScheduler() {
        return Schedulers.io();
    }
}
