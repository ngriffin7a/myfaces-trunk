<%@ taglib uri="http://www.tagunit.org/tagunit/core" prefix="tagunit" %>

<tagunit:setProperty name="ignoreWarnings" value="true"/>

<tagunit:testTagLibrary uri="http://myfaces.apache.org/extensions">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_ext.tld"/>
</tagunit:testTagLibrary>
