package com.lookout.plugin.servicerelay.internal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lookout.plugin.android.Provider;
import com.lookout.plugin.servicerelay.ServiceRelayComponent;
import com.lookout.plugin.servicerelay.ServiceRelayExtension;

import javax.inject.Inject;

public class ServiceRelayService extends Service implements ServiceRelayExtension.Control {

    @Inject
    ServiceRelayImpl mServiceShare;

    @Override
    public void onCreate() {
        super.onCreate();
        new Provider<ServiceRelayComponent>().fromContext(getApplicationContext()).inject(this);
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
