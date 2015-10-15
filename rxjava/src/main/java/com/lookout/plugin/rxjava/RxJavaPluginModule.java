package com.lookout.plugin.rxjava;

import com.lookout.plugin.rxjava.concurrency.RxJavaModule;

import dagger.Module;

/**
 * Pulls together all the modules in the library so that only this one module needs to be declared
 * by the including component.
 */
@Module(includes = {
        RxJavaModule.class,
})
public class RxJavaPluginModule {
}
