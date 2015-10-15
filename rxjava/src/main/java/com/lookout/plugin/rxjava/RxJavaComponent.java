package com.lookout.plugin.rxjava;

import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.android.concurrency.Background;
import com.lookout.plugin.android.concurrency.MainLooper;

import rx.Scheduler;

//@ApplicationScope
//@Component(modules = {
//     AndroidPluginModule.class,
//     RxJavaPluginModule.class
// })
public interface RxJavaComponent extends AndroidComponent {
    @MainLooper Scheduler mainLooper();
    @Background Scheduler backgroundLooper();
}
