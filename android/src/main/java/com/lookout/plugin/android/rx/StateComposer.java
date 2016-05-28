package com.lookout.plugin.android.rx;

import rx.Observable;

/**
 * Composes states into one single {@link Observable}. The final Observable will emit state change only if state was
 * changed. The final observable will return {@code true} if the latest state of any observable emits {@code true}
 */
public class StateComposer implements Observable.Transformer<Observable<Boolean>, Boolean> {

    @Override
    public Observable<Boolean> call(Observable<Observable<Boolean>> states) {
        checkHasInitialState(states);
        return states
            .toList()
            .flatMap(l -> Observable
                .combineLatest(l, args -> args)
                .flatMap(list -> Observable
                    .from(list)
                    .map(o -> (Boolean) o) // casting item to Boolean
                    .reduce((a, b) -> a || b))) // reducing items to one that represents the enabled state
            .distinctUntilChanged(); // emit item only if it changed
    }

    private void checkHasInitialState(Observable<Observable<Boolean>> states) {
        for (final Observable<Boolean> state : states.toList().toBlocking().first()) {
            if (!state.first().toBlocking().toFuture().isDone()) {
                throw new IllegalArgumentException("State observable has to emit an initial value, state observable: "
                    + state);
            }
        }
    }
}
