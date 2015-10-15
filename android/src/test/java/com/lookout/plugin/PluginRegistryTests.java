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

    private static class ExtensionType1 {
    }

    private static class ExtensionType2 {
    }

    private static final ExtensionType1 PLUGIN_1_EXTENSION_TYPE_1_EXTENSION_1 = new ExtensionType1();
    private static final ExtensionType1 PLUGIN_1_EXTENSION_TYPE_1_EXTENSION_2 = new ExtensionType1();
    private static final ExtensionType1 PLUGIN_2_EXTENSION_TYPE_1_EXTENSION_1 = new ExtensionType1();
    private static final ExtensionType2 PLUGIN_2_EXTENSION_TYPE_2_EXTENSION_1 = new ExtensionType2();

    @Mock
    private Plugin mPlugin1;

    @Mock
    private Plugin mPlugin2;

    private PluginRegistry mPluginRegistry;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mPlugin1.provideExtensions(ExtensionType1.class)).thenReturn(new ExtensionType1[]{
                PLUGIN_1_EXTENSION_TYPE_1_EXTENSION_1, PLUGIN_1_EXTENSION_TYPE_1_EXTENSION_2 });
        when(mPlugin1.provideExtensions(ExtensionType2.class)).thenReturn(null);

        when(mPlugin2.provideExtensions(ExtensionType1.class)).thenReturn(new ExtensionType1[]{
                PLUGIN_2_EXTENSION_TYPE_1_EXTENSION_1 });
        when(mPlugin2.provideExtensions(ExtensionType2.class)).thenReturn(new ExtensionType2[]{
                PLUGIN_2_EXTENSION_TYPE_2_EXTENSION_1 });

        mPluginRegistry = new PluginRegistry();
    }

    @After
    public void tearDown() throws Exception {
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

    @SmallTest
    @Test
    public void testGetExtensions_returnsExtensionsFromAllPlugins() {
        // setup: register both plugins
        mPluginRegistry.onApplicationCreate(new Plugin[] { mPlugin1, mPlugin2 });

        // when: get extensions for type that's returned by both plugins
        ExtensionType1[] extensions = mPluginRegistry.getExtensions(ExtensionType1.class);

        // then:
        assertTrue(Arrays.equals(
                new ExtensionType1[]{PLUGIN_1_EXTENSION_TYPE_1_EXTENSION_1,
                        PLUGIN_1_EXTENSION_TYPE_1_EXTENSION_2,
                        PLUGIN_2_EXTENSION_TYPE_1_EXTENSION_1},
                extensions));
    }

    @SmallTest
    @Test
    public void testGetExtensions_returnsExtensionsFromOnePlugin() {
        // setup: register both plugins
        mPluginRegistry.onApplicationCreate(new Plugin[] { mPlugin1, mPlugin2 });

        // when: get extensions for type that's returned by both plugins
        ExtensionType2[] extensions = mPluginRegistry.getExtensions(ExtensionType2.class);

        // then:
        assertTrue(Arrays.equals(
                new ExtensionType2[]{PLUGIN_2_EXTENSION_TYPE_2_EXTENSION_1}, extensions));
    }

    @SmallTest
    @Test
    public void testGetExtensions_throwsOnInvalidExtensionType() {
        // setup: make plugin 1 return an object array for any extension type
        when(mPlugin1.provideExtensions(any())).thenReturn(new Object[] { new Object() });
        // setup: register both plugins
        mPluginRegistry.onApplicationCreate(new Plugin[]{mPlugin1, mPlugin2});

        try {
            // when: get extensions for type that's returned by both plugins
            ExtensionType2[] extensions = mPluginRegistry.getExtensions(ExtensionType2.class);
        } catch (RuntimeException e) {
            // then: exception thrown
        }
    }

}