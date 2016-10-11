package com.lookout.plugin.android;

import android.test.mock.MockContext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ComponentsTest {

    @Mock
    TestContext mContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testFrom_customAndroidComponentProvider() throws Exception {
        // setup:
        AndroidComponent androidComponent = mock(AndroidComponent.class);
        when(mContext.androidComponent()).thenReturn(androidComponent);
        // when:
        AndroidComponent component = Components.from(mContext, AndroidComponent.class);
        // then:
        Assert.assertEquals(androidComponent, component);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrom_throwsIllegalState_ifComponentNotImplemented() throws Exception {
        // setup:
        AndroidComponent androidComponent = mock(AndroidComponent.class);
        when(mContext.androidComponent()).thenReturn(androidComponent);
        // when:
        Components.from(mContext, OtherComponent.class);
        // then: throws IllegalArgumentException

    }

    interface OtherComponent extends AndroidComponent {

    }

    abstract static class TestContext extends MockContext implements AndroidComponentProvider {

    }

}