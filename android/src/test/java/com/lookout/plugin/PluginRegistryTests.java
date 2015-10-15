package com.lookout.plugin;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.lookout.plugin.android.BuildConfig.class, emulateSdk = 16)
public class PluginRegistryTests {

    @Mock
    private Plugin mPlugin1;

    @Mock
    private Plugin mPlugin2;

    private PluginRegistry mPluginRegistry;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mPluginRegistry = new PluginRegistry();
    }

    @SmallTest
    @Test
    public void testOnApplicationCreate_callsPluginsOnApplicationCreate() {
        // when: register both plugins
        mPluginRegistry.onApplicationCreate(new Plugin[] { mPlugin1, mPlugin2 });

        // then: both plugins initialized
        verify(mPlugin1).onApplicationCreate();
        verify(mPlugin2).onApplicationCreate();
    }


}