package com.lookout.plugin.android.broadcasts.internal;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.lookout.plugin.ApplicationOnCreateListener;
import com.lookout.plugin.android.application.ApplicationScope;
import com.lookout.plugin.android.broadcasts.BroadcastRelay;
import com.lookout.plugin.android.broadcasts.BroadcastRelayDelegate;

import rx.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

@ApplicationScope
public class BroadcastRelayImpl implements ApplicationOnCreateListener, BroadcastRelay {

    private final Observable<BroadcastRelayDelegate> mRelayDelegateObservable;
    private final Context mAppContext;
    private final PackageManager mPackageManager;

    @Inject
    public BroadcastRelayImpl(Application application, PackageManager packageManager, Set<BroadcastRelayDelegate>
        broadcastRelayDelegates)
    {
        mAppContext = application;
        mPackageManager = packageManager;
        mRelayDelegateObservable = Observable.from(broadcastRelayDelegates);
    }

    private Observable<List<Observable<Boolean>>> collectDelegateStates(Observable<BroadcastRelayDelegate>
        delegatesObservable)
    {
        return delegatesObservable.collect(
            () -> new ArrayList<>(),
            (list, broadcastRelayDelegate) ->
                list.add(broadcastRelayDelegate.getEnableStateChange().startWith(false))
        );
    }

    private Observable<Boolean> combineLatestDelegateStates(List<Observable<Boolean>> statesList) {
        return Observable
            .combineLatest(
                statesList,
                // states is an Object[], force it to be a List<Boolean>
                states -> (List<Boolean>) (List) Arrays.asList(states))
            .flatMap(states -> Observable
                .from(states) // emit each boolean from the list one by one
                .reduce((a, b) -> a || b)) // combine emitted booleans into one with OR
            .distinctUntilChanged();
    }

    @Override
    public void applicationOnCreate() {
        mRelayDelegateObservable
            .groupBy(BroadcastRelayDelegate::getReceiver) // sort delegates based on the receiver they're using
            .flatMap(group -> collectDelegateStates(group)
                    .flatMap(delegateStates -> combineLatestDelegateStates(delegateStates))
                    .map(combinedState -> new ReceiverState(group.getKey(), combinedState))
            )
            .subscribe(this::onReceiverStateChange);
    }

    private void onReceiverStateChange(ReceiverState receiverState) {
        mPackageManager.setComponentEnabledSetting(new ComponentName(mAppContext, receiverState.mReceiverClass),
            (receiverState.mEnabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED),
            PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onReceive(Class receiverClass, Context context, Intent intent) {
        mRelayDelegateObservable
            .flatMap(delegate ->
                // filter only mEnabled delegates
                delegate.getEnableStateChange()
                    // only take the current state, (otherwise the observable stream will remain open indefinitely)
                    .first()
                    .filter(isEnabled -> isEnabled) // only take the listeners that are enable
                    .map(st -> delegate) // map back to the delegate
            )
            .filter(delegate ->
                    // filter delegate by receiver class & action
                    Arrays.asList(delegate.getReceiver()).contains(receiverClass)
                        && Arrays.asList(delegate.getActions()).contains(intent.getAction())
            ).subscribe(delegate -> delegate.onReceive(context, intent));
    }

    private static class ReceiverState {
        final Class<? extends BroadcastReceiver> mReceiverClass;
        final Boolean mEnabled;

        private ReceiverState(Class<? extends BroadcastReceiver> receiverClass, Boolean enabled) {
            this.mReceiverClass = receiverClass;
            this.mEnabled = enabled;
        }
    }
}
