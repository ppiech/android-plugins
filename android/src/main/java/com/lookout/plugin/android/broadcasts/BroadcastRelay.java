package com.lookout.plugin.android.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * An interface which handles BroadcastReceiver's onReceive call
 */
public interface BroadcastRelay {

    /**
     * Receives the broadcast class and the intent and redirects the data to the broadcast delegates
     *
     * @param receiverClass
     * @param context
     * @param data
     */
    void onReceive(Class<? extends BroadcastReceiver> receiverClass, Context context, Intent data);

}
