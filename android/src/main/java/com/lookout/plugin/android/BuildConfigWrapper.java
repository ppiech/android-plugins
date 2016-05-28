package com.lookout.plugin.android;

public class BuildConfigWrapper {
    private final boolean mDebug;
    private final String mApplicationId;
    private final int mVersionCode;
    private final String mVersionName;

    public BuildConfigWrapper(boolean debug, String applicationId, int versionCode, String versionName) {
        mDebug = debug;
        mApplicationId = applicationId;
        mVersionCode = versionCode;
        mVersionName = versionName;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public String getApplicationId() {
        return mApplicationId;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public String getVersionName() {
        return mVersionName;
    }
}
