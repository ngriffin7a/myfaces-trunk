<%@ taglib uri="http://www.tagunit.org/tagunit/core" prefix="tagunit" %>

<%--
  Tests for TagUnit tag libraries
  -------------------------------
  This page contains the tests required to automatically test the basics of
  the tag libraries that are a part of the TagUnit framework - warnings are ignored
--%>

<tagunit:setProperty name="ignoreWarnings" value="false"/>

<tagunit:testTagLibrary uri="http://java.sun.com/jsf/html">
  <tagunit:tagLibraryDescriptor jar="myfaces.jar" name="myfaces_html.tld"/>
</tagunit:testTagLibrary>