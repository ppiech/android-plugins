package ${packageName}.dagger;

import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.android.application.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {${appClass}Module.class})
public interface ${appClass}Component extends 
	AndroidComponent
{

}