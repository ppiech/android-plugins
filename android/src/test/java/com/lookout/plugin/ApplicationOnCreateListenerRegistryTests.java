package com.lookout.plugin;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.verify;

public class ApplicationOnCreateListenerRegistryTests {

    @Mock
    private ApplicationOnCreateListener mApplicationOnCreateListener1;

    @Mock
    private ApplicationOnCreateListener mApplicationOnCreateListener2;

    private ApplicationOnCreateListenerDispatcher mOnApplicationCreateListenerRegistry;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mOnApplicationCreateListenerRegistry = new ApplicationOnCreateListenerDispatcher(
            new HashSet<>(Arrays.asList(mApplicationOnCreateListener1, mApplicationOnCreateListener2)));
    }

    @SmallTest
    @Test
    public void testOnApplicationCreate_callsPluginsOnApplicationCreate() {
        // when: register both plugins
        mOnApplicationCreateListenerRegistry.onApplicationCreate();

        // then: both plugins initialized
        verify(mApplicationOnCreateListener1).applicationOnCreate();
        verify(mApplicationOnCreateListener2).applicationOnCreate();
    }


}