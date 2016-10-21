package ${packageName};

import com.lookout.plugin.android.ActivityScope;

import javax.inject.Inject;

@ActivityScope
public final class ${activityClass}Presenter {
    private final ${appClass}Screen mScreen;

    @Inject
    public ${activityClass}Presenter(${appClass}Screen screen) {
        mScreen = screen;
    }
}