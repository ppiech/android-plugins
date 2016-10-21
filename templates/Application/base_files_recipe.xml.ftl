<recipe>
	<instantiate from="src/app_package/classes/App.java.ftl"
       to="${escapeXmlAttribute(srcOut)}/${appClass}.java" />

    <instantiate from="src/app_package/classes/AppComponent.java.ftl"
       to="${escapeXmlAttribute(srcOut)}/dagger/${appClass}Component.java" />

   <instantiate from="src/app_package/classes/AppModule.java.ftl"
       to="${escapeXmlAttribute(srcOut)}/dagger/${appClass}Module.java" />

</recipe>
