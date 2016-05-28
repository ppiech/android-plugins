package com.lookout.plugin.android.services;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Vibrator;

import com.lookout.plugin.android.application.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ServicesModule {
    @ApplicationScope @Provides
    public Vibrator provideVibrator(Application app) {
        return (Vibrator) app.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @ApplicationScope @Provides
    public KeyguardManager provideKeyguardManager(Application app) {
        return (KeyguardManager) app.getSystemService(Context.KEYGUARD_SERVICE);
    }
}
