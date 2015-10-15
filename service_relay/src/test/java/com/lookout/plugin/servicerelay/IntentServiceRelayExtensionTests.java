package com.lookout.plugin.servicerelay;

import android.content.Intent;
import android.test.suitebuilder.annotation.SmallTest;

import com.lookout.servicerelay.IntentServiceRelayExtension;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 16)
public class IntentServiceRelayExtensionTests {

    private static Intent INTENT = new Intent("action");
    private static int FLAGS = 456;
    private static int START_ID = 123;

    public static class TestIntentServiceExtension extends IntentServiceRelayExtension {
        public TestIntentServiceExtension(ExecutorService executor) {
            super(executor);
        }

        @Override
        protected void onServiceHandleIntent(Intent intent) {
        }

        @Override
        public String[] getActions() {
            return new String[0];
        }
    }

    @Mock
    private ExecutorService mExecutor;

    @Mock
    private ServiceRelayExtension.Control mControl;

    private TestIntentServiceExtension mIntentServiceExtension;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mIntentServiceExtension = spy(new TestIntentServiceExtension(mExecutor));

        // Initialize service extension, as required by the extension API.
        mIntentServiceExtension.onServiceCreate(mControl);
    }

    private static Runnable captureExecutorRunnable(Executor executorMock) {
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(executorMock).execute(captor.capture());
        return captor.getValue();
    }

    private static void configureExecutorToRunImmediately(Executor executorMock) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Runnable)invocation.getArguments()[0]).run();
                return null;
            }
        }).when(executorMock).execute(any(Runnable.class));
    }

    @SmallTest
    @Test
    public void testOnServiceStartCommand_schedulesWorkOnBackgroundThread() {
        // when: send start intent to extension
        mIntentServiceExtension.onServiceStartCommand(INTENT, FLAGS, START_ID);

        // then: extension submits an runnable to executor for background work
        verify(mExecutor).execute(any(Runnable.class));
    }

    @SmallTest
    @Test
    public void testOnServiceStartCommand_callsOnServiceHandleIntent() {
        // setup: executor
        configureExecutorToRunImmediately(mExecutor);

        // when: send start intent to extension
        mIntentServiceExtension.onServiceStartCommand(INTENT, FLAGS, START_ID);

        // then: background thread called onServiceHandleIntent()
        verify(mIntentServiceExtension).onServiceHandleIntent(INTENT);
    }

    @SmallTest
    @Test
    public void testOnServiceStartCommand_doesntCallStopSelfBeforeOnServiceHandleIntent() {
        // setup: executor will ignore submitted runnables

        // when: send intent to extension
        mIntentServiceExtension.onServiceStartCommand(INTENT, FLAGS, START_ID);

        // then: stop self never called
        verify(mControl, never()).stopSelfResult(anyInt());
    }

    @SmallTest
    @Test
    public void testOnServiceStartCommand_callsControlStopSelf() {
        // setup: executor
        configureExecutorToRunImmediately(mExecutor);

        // when: send intent to extension
        mIntentServiceExtension.onServiceStartCommand(INTENT, FLAGS, START_ID);

        // then: stop self called
        verify(mControl).stopSelfResult(anyInt());
    }

}