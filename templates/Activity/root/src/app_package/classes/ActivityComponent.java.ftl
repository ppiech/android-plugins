package ${packageName}.dagger;

import com.lookout.plugin.android.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Component(modules = {${activityClass}Module.class})
public interface ${activityClass}Subcomponent {
    void inject(${activityClass} toInject);
}