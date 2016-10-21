package ${packageName};

import android.app.Application;

import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.android.AndroidComponentProvider;
import com.lookout.plugin.android.application.ApplicationModule;

import ${packageName}.dagger.${appClass}Component;
import ${packageName}.dagger.${appClass}Module;
import ${packageName}.dagger.Dagger${appClass}Component;

public final class ${appClass} extends Application implements AndroidComponentProvider {
    private ${appClass}Component m${appClass}Component;

    @Override
    public void onCreate()
    {
        super.onCreate();

        m${appClass}Component = Dagger${appClass}Component.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    @Override
    public AndroidComponent androidComponent() {
        return m${appClass}Component;
    }
}