package com.lookout.plugin.android.broadcasts.internal;

import com.lookout.plugin.ApplicationOnCreateListener;
import com.lookout.plugin.android.application.ApplicationScope;
import com.lookout.plugin.android.broadcasts.BroadcastRelay;
import com.lookout.plugin.android.broadcasts.BroadcastRelayDelegate;

import dagger.Module;
import dagger.Provides;

import java.util.HashSet;
import java.util.Set;

@Module
public class BroadcastRelayModule {

    @Provides(type = Provides.Type.SET_VALUES)
    Set<BroadcastRelayDelegate> providesDefaultBroadcastRelayDelegates() {
        return new HashSet<>();
    }

    @Provides(type = Provides.Type.SET)
    @ApplicationScope
    public ApplicationOnCreateListener providesApplicationOnCreateListener(BroadcastRelayImpl broadcastRelayImpl) {
        return broadcastRelayImpl;
    }

    @Provides
    @ApplicationScope
    public BroadcastRelay providesBroadcastRelay(BroadcastRelayImpl broadcastRelayImpl) {
        return broadcastRelayImpl;
    }
}
