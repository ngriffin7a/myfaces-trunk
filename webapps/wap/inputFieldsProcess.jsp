<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="commandLink">
<p>
<f:view>
    <wap:form>
        <i>name:</i><br/>
        <wap:outputText value="#{stringBean.name}" /><br/><br/>
        <i>password:</i><br/>
        <wap:outputText value="#{stringBean.password}" /><br/><br/>

        <wap:commandLink action="back" ><wap:outputText value="back"/></wap:commandLink>
    </wap:form>
</f:view>
</p>
</card>
</wml> 

