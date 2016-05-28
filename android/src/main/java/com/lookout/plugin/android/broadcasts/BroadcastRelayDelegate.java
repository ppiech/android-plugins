package com.lookout.plugin.android.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rx.Observable;

/**
 * Delegate for sharing BroadcastReceiver implementations.  Implementations can be contributed
 * to broadcasts to receive events and control receiver enablement.
 */
public interface BroadcastRelayDelegate {

    /**
     * Receives the broacast intent.
     * @param context
     * @param data
     */
    void onReceive(Context context, Intent data);

    /**
     * Indicates the enabled state of this delegate.  If all delegates for a given receiver are
     * disabled, then the receiver itself will be diabled by the relay.  The enabled state of delegates
     * can be changed at any time.
     * <br/><b>Note: The returned observable must emit current state when subscribed to</b>
     * @return Observable for the state of the delegate.
     */
    Observable<Boolean> getEnableStateChange();

    /**
     * The receiver that this delegate is handling events for.
     * @return
     */
    Class<? extends BroadcastReceiver> getReceiver();

    /**
     * Returns the broadcast intent actions that this delegate handles
     */
    String[] getActions();
}
