<recipe>
    <instantiate from="src/app_package/classes/ActivitySubComponent.java.ftl"
       to="${escapeXmlAttribute(srcOut)}/dagger/${activityClass}Subcomponent.java" />

   <instantiate from="src/app_package/classes/ActivityModule.java.ftl"
       to="${escapeXmlAttribute(srcOut)}/dagger/${activityClass}Module.java" />

	<instantiate from="src/app_package/classes/Screen.java.ftl"
       to="${escapeXmlAttribute(srcOut)}/${activityClass}Screen.java" />

	<instantiate from="src/app_package/classes/Presenter.java.ftl"
       to="${escapeXmlAttribute(srcOut)}/${activityClass}Presenter.java" />

</recipe>
