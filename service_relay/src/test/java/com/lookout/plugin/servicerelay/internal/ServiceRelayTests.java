package com.lookout.plugin.servicerelay.internal;

import android.content.Intent;
import android.test.suitebuilder.annotation.SmallTest;

import com.lookout.plugin.servicerelay.ServiceRelayDelegate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.lookout.plugin.servicerelay.BuildConfig.class, emulateSdk = 16)
public class ServiceRelayTests {

    private static String ACTION_EXTENSION1_ACTION1 = "extensions1 action1";
    private static String ACTION_EXTENSION1_ACTION2 = "extensions1 action2";
    private static String ACTION_EXTENSION2_ACTION1 = "extensions2 action1";
    private static String ACTION_EXTENSION2_ACTION2 = "extensions2 action2";
    private static String ACTION_INVALID = "extensions? action?";

    private static int START_ID = 123;
    private static int START_ID_SECOND = 124;
    private static int START_ID_INVALID = 890;
    private static int FLAGS = 456;

    @Mock
    private ServiceRelayDelegate.Control mService;

    @Mock
    private ServiceRelayDelegate mExtension1;

    @Mock
    private ServiceRelayDelegate mExtension2;

    private ServiceRelay mServiceRelayShared;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // setup: two extensions are returned by the plugin registry
        when(mExtension1.getActions()).thenReturn(
                new String[]{ACTION_EXTENSION1_ACTION1, ACTION_EXTENSION1_ACTION2});
        when(mExtension2.getActions()).thenReturn(
                new String[] { ACTION_EXTENSION2_ACTION1, ACTION_EXTENSION2_ACTION2 });

        Set<ServiceRelayDelegate> extensions = Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList(mExtension1, mExtension2)));

        mServiceRelayShared = new ServiceRelay(extensions);
    }

    @After
    public void tearDown() throws Exception {
    }

    @SmallTest
    @Test
    public void testOnServiceCreate_callsExtensionsOnServiceCreate() {
        // when: service is created and calls onServiceCreate
        mServiceRelayShared.onServiceCreate(mService);

        // then: both extensions care called with onServiceCreate with the control instance
        verify(mExtension1, times(1)).onServiceCreate(any(ServiceRelayDelegate.Control.class));
        verify(mExtension2, times(1)).onServiceCreate(any(ServiceRelayDelegate.Control.class));
    }

    @SmallTest
    @Test
    public void testOnServiceCreate_throwExceptionIfListenerAlreadyRegistered() {
        // setup: two extensions use the same action ID
        String[] duplicateActions = new String[] {"action1"};
        when(mExtension1.getActions()).thenReturn(duplicateActions);
        when(mExtension2.getActions()).thenReturn(duplicateActions);

        // when: service is created
        try {
            mServiceRelayShared.onServiceCreate(mService);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            // then: runtime exception is thrown
        }
    }

    @SmallTest
    @Test
    public void testOnServiceDestroy_callsExtensionsOnServiceDestroy() {
        // setup: Service is created
        mServiceRelayShared.onServiceCreate(mService);

        // when: Service is destroyed
        mServiceRelayShared.onServiceDestroy(mService);

        // then: all extensions are called with onServiceDestroy()
        verify(mExtension1, times(1)).onServiceDestroy();
        verify(mExtension2, times(1)).onServiceDestroy();
    }

    @SmallTest
    @Test
    public void testOnServiceDestroy_throwsExceptionIfServiceNotCreated() {
        // setup: service is never created
        try {
            // when: service calls onDestroy()
            mServiceRelayShared.onServiceDestroy(mService);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            // then: runtime exception is thrown
        }
    }

    @SmallTest
    @Test
    public void testOnServiceStartCommand_callsExtensionMappedToAction() {
        // setup: service is created
        mServiceRelayShared.onServiceCreate(mService);

        // when: service receives an intent with an action for extension 1
        Intent intent = new Intent(ACTION_EXTENSION1_ACTION2);
        mServiceRelayShared.onServiceStartCommand(intent, FLAGS, START_ID);

        // then: extension 1 receives the intent
        verify(mExtension1, times(1)).onServiceStartCommand(intent, FLAGS, START_ID);
        // then: extension 2 doesn't receive the intent
        verify(mExtension2, never()).onServiceStartCommand(intent, FLAGS, START_ID);
    }

    @SmallTest
    @Test
    public void testOnServiceStartCommand_throwsExceptionIfActionNotRecognized() {
        // setup: service is created
        mServiceRelayShared.onServiceCreate(mService);

        try {
            // when: service receives an intent with an action that doesn't have a matching action
            mServiceRelayShared.onServiceStartCommand(new Intent(ACTION_INVALID), FLAGS, START_ID);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            // then: runtime exception is thrown
        }
    }

    private ServiceRelayDelegate.Control captureOnServiceCreateControl(ServiceRelayDelegate extensionMock) {
        ArgumentCaptor<ServiceRelayDelegate.Control> controlCaptor = ArgumentCaptor.forClass(ServiceRelayDelegate.Control.class);
        verify(extensionMock).onServiceCreate(controlCaptor.capture());
        return controlCaptor.getValue();
    }

    @SmallTest
    @Test
    public void testStopSelfResult_stopsService() {
        // setup: service receives and intent for an extension
        mServiceRelayShared.onServiceCreate(mService);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION1_ACTION1), FLAGS, START_ID);

        // when: extension calls stopSelf on the service relay control
        captureOnServiceCreateControl(mExtension1).stopSelfResult(START_ID);

        // then: service relay calls stopSelf on the service
        verify(mService).stopSelfResult(START_ID);
    }

    @SmallTest
    @Test
    public void testStopSelfResult_throwsExceptionWhenStartIdInvalid() {
        // setup: service receives and intent for an extension
        mServiceRelayShared.onServiceCreate(mService);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION1_ACTION1), FLAGS, START_ID);

        try {
            // when: extension calls service relay control stopSelf() with a start ID that's different
            // than what it received in onServiceStartCommand()
            captureOnServiceCreateControl(mExtension1).stopSelfResult(START_ID_INVALID);
            fail("expected exception");
        } catch (IllegalStateException e) {
            // then: runtime exception is thrown
        }
    }

    @SmallTest
    @Test
    public void testStopSelfResult_doesntStopServiceIfFirstExtensionOutOfTwoIsRunning() {
        // setup: send two intents to two extensions
        mServiceRelayShared.onServiceCreate(mService);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION1_ACTION1), FLAGS, START_ID);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION2_ACTION1), FLAGS, START_ID_SECOND);

        // when: second extension calls stopSelf
        captureOnServiceCreateControl(mExtension1).stopSelfResult(START_ID_SECOND);

        // then: service is not called to stopSelf
        verify(mService, never()).stopSelfResult(anyInt());
    }

    @SmallTest
    @Test
    public void testStopSelfResult_doesntStopServiceIfSecondExtensionOutOfTwoIsRunning() {
        // setup: send two intents to two extensions
        mServiceRelayShared.onServiceCreate(mService);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION1_ACTION1), FLAGS, START_ID);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION2_ACTION1), FLAGS, START_ID_SECOND);

        // when: first extension calls stopSelf
        captureOnServiceCreateControl(mExtension1).stopSelfResult(START_ID);

        // then: service is not called to stopSelf
        verify(mService, never()).stopSelfResult(anyInt());
    }

    @SmallTest
    @Test
    public void testStopSelfResult_stopsServiceIfAllExtensionsCallStop() {
        // setup: send two intents to two extensions
        mServiceRelayShared.onServiceCreate(mService);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION1_ACTION1), FLAGS, START_ID);
        mServiceRelayShared.onServiceStartCommand(
                new Intent(ACTION_EXTENSION2_ACTION1), FLAGS, START_ID_SECOND);

        // when: both extensions finish and call stopSelf
        captureOnServiceCreateControl(mExtension1).stopSelfResult(START_ID);
        captureOnServiceCreateControl(mExtension1).stopSelfResult(START_ID_SECOND);

        // then: service called to stopSelf with second start ID
        verify(mService).stopSelfResult(START_ID_SECOND);

        // then: service should not be called with first start ID
        verify(mService, never()).stopSelfResult(START_ID);
    }

}