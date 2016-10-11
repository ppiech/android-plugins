package com.lookout.plugin.android.rx;

import android.test.suitebuilder.annotation.SmallTest;

import com.lookout.plugin.android.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class StateComposerTest {

    @Test
    @SmallTest
    public void testStateComposerEmitsCorrectState() throws Exception {
        // setup:
        List<Observable<Boolean>> states = new ArrayList<>();
        BehaviorSubject<Boolean> subject = BehaviorSubject.create(false);
        states.add(subject);
        states.add(Observable.just(false));
        states.add(Observable.never());

        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        // when:
        Observable<Boolean> composedObservable = Observable.from(states).map(s -> s.startWith(false))
                        .compose(new StateComposer());
        composedObservable.subscribe(testSubscriber);
        subject.onNext(true);
        subject.onNext(false);
        // then:
        testSubscriber.assertValues(false, true, false);
    }

    @Test
    @SmallTest
    public void testStateComposerEmitsFalseByDefault() throws Exception {
        // setup:
        List<Observable<Boolean>> states = new ArrayList<>();
        states.add(Observable.just(false));
        states.add(Observable.never());

        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        // when:
        Observable<Boolean> composedObservable = Observable.from(states)
                .map(s -> s.startWith(false))
                .compose(new StateComposer());
        composedObservable.subscribe(testSubscriber);
        // then:
        testSubscriber.assertValue(false);
    }

    @Test
    @SmallTest
    public void testStateComposerWithSingleState() throws Exception {
        // setup:
        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        // when:
        Observable<Boolean> composedObservable = Observable.just(Observable.just(true))
                .compose(new StateComposer());
        composedObservable.subscribe(testSubscriber);
        // then:
        testSubscriber.assertValue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    @SmallTest
    public void testStateComposerFailsIfSourceIsNotInitiated() throws Exception {
        // setup:
        PublishSubject<Boolean> publishSubject = PublishSubject.create();
        // when: compose, then: should throw an error as state is not initiated
        Observable.just(publishSubject, Observable.just(false)).compose(new StateComposer()).subscribe();
    }
}