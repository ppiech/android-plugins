package com.lookout.plugin.android.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

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

    @ApplicationScope @Provides
    PackageManager providesPackageManager(Application application) {
        return application.getPackageManager();
    }

    @ApplicationScope @Provides
    TelephonyManager providesTelephonyManager(Application application) {
        return (TelephonyManager)application.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @ApplicationScope @Provides
    AlarmManager providesAlarmManager(Application application) {
        return (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
    }

    @ApplicationScope @Provides
    ConnectivityManager providesConnectivityManager(Application application) {
        return (ConnectivityManager)application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @ApplicationScope @Provides
    NotificationManager providesNotificationManager(Application application) {
        return (NotificationManager)application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @ApplicationScope @Provides
    WindowManager providesWindowManager(Application application) {
        return (WindowManager)application.getSystemService(Context.WINDOW_SERVICE);
    }

    @ApplicationScope @Provides
    ActivityManager providesActivityManager(Application application) {
        return (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
    }
}