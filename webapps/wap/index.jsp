<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="cart1" title="Examples">
<p>
<f:view>
    <wap:form> 
        <i>Examples:</i>
        <wap:commandLink action="commandButton"><wap:outputText value="commandButton"/></wap:commandLink><br/>
        <wap:commandLink action="commandLink"><wap:outputText value="commandLink"/></wap:commandLink><br/>
        <wap:commandLink action="dataTable"><wap:outputText value="dataTable"/></wap:commandLink><br/>
        <wap:commandLink action="graphicImage"><wap:outputText value="graphicImage"/></wap:commandLink><br/>
        <wap:commandLink action="inputFields"><wap:outputText value="inputFields"/></wap:commandLink><br/>
        <wap:commandLink action="message"><wap:outputText value="message"/></wap:commandLink><br/>
        <wap:commandLink action="messages"><wap:outputText value="messages"/></wap:commandLink><br/>
        <wap:commandLink action="outputText"><wap:outputText value="outputText"/></wap:commandLink><br/>
        <wap:commandLink action="panelGrid"><wap:outputText value="panelGrid"/></wap:commandLink><br/>
        <wap:commandLink action="selectBoolean"><wap:outputText value="selectBoolean"/></wap:commandLink><br/>
        <wap:commandLink action="selectMany"><wap:outputText value="selectMany"/></wap:commandLink><br/>
        <wap:commandLink action="selectOne"><wap:outputText value="selectOne"/></wap:commandLink><br/>
    </wap:form>      
</f:view>
</p>
</card>
</wml>