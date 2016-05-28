package com.lookout.plugin.android.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.android.Components;

import java.util.Arrays;

public abstract class BroadcastRelayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Arrays.asList(getActions()).contains(intent.getAction())) {
            Components.from(context, AndroidComponent.class)
                    .broadcastRelay().onReceive(getClass(), context, intent);
        }
    }

    /**
     * Returns the broadcast intent actions that this receiver handles
     */
    public abstract String[] getActions();
}
