<%@ taglib uri="http://www.tagunit.org/tagunit/core" prefix="tagunit" %>

<tagunit:setProperty name="ignoreWarnings" value="true"/>

<tagunit:testTagLibrary uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_3.tld">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_ext.tld"/>
</tagunit:testTagLibrary>
