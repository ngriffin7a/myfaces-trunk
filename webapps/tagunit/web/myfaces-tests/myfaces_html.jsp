<%@ taglib uri="http://www.tagunit.org/tagunit/core" prefix="tagunit" %>

<tagunit:setProperty name="ignoreWarnings" value="true"/>

<tagunit:testTagLibrary uri="http://java.sun.com/jsf/html">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_html.tld"/>
</tagunit:testTagLibrary>
