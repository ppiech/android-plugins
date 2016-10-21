package ${packageName}.dagger;

import com.lookout.plugin.android.AndroidPluginModule;

import ${packageName}.${appClass};

import dagger.Module;
import dagger.Provides;

@Module(includes={
    AndroidPluginModule.class,
    })
public final class ${appClass}Module {
}
