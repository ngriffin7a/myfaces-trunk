<?xml version="1.0" encoding="UTF-8"?>
<jsp:root version="2.0" xmlns:h="http://java.sun.com/jsf/html"
  xmlns:f="http://java.sun.com/jsf/core"
  xmlns:x="http://myfaces.apache.org/extensions"
  xmlns:jsp="http://java.sun.com/JSP/Page">
  <jsp:directive.page contentType="text/html;charset=UTF-8" 
                      pageEncoding="UTF-8"/>
<f:view>
  <h:form id="testForm">
    <x:div style="bar">
      <h:outputText value="Should be in a div1"/>
    </x:div>
    <x:div styleClass="foo">
      <h:outputText value="Should be in a div2"/>
    </x:div>
  </h:form>
</f:view>
</jsp:root>
