<%@ taglib uri="http://www.tagunit.org/tagunit/core" prefix="tagunit" %>

<tagunit:setProperty name="ignoreWarnings" value="true"/>

<tagunit:testTagLibrary uri="http://java.sun.com/jsf/core">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_core.tld"/>
</tagunit:testTagLibrary>