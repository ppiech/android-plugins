package ${packageName}.dagger;

import com.lookout.plugin.android.AndroidPluginModule;

import dagger.Module;
import dagger.Provides;

@Module
public final class ${activityClass}Module {
    private final ${activityClass} mActivity;

    public ${activityClass}Module(${activityClass} activity) {
        mActivity = activity;
    }

    @Provides Activity providesActivity() {
        return mActivity;
    }

    @Provides ${activityClass}Screen providesScreen() {
        return mActivity;
    }
}
