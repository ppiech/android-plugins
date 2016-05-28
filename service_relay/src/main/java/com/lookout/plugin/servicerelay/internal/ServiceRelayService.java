package com.lookout.plugin.servicerelay.internal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lookout.plugin.android.Components;
import com.lookout.plugin.servicerelay.ServiceRelayComponent;
import com.lookout.plugin.servicerelay.ServiceRelayDelegate;

import javax.inject.Inject;

public class ServiceRelayService extends Service implements ServiceRelayDelegate.Control {

    @Inject
    ServiceRelay mServiceShare;

    @Override
    public void onCreate() {
        super.onCreate();
        Components.from(getApplicationContext(), ServiceRelayComponent.class).inject(this);
        mServiceShare.onServiceCreate(this);
    }

    @Override
    public void onDestroy() {
        mServiceShare.onServiceDestroy(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return mServiceShare.onServiceStartCommand(intent, flags, startId);
    }
}
