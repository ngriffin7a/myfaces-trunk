<%@ taglib uri="http://www.tagunit.org/tagunit/core" prefix="tagunit" %>

<tagunit:setProperty name="ignoreWarnings" value="false"/>

<tagunit:testTagLibrary uri="http://java.sun.com/jsf/html">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_html.tld"/>
</tagunit:testTagLibrary>

<tagunit:testTagLibrary uri="http://java.sun.com/jsf/core">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_core.tld"/>
</tagunit:testTagLibrary>

<tagunit:testTagLibrary uri="http://myfaces.apache.org/extensions.tld">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_ext.tld"/>
</tagunit:testTagLibrary>
