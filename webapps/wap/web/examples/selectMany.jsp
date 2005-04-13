<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="selectMany example">
<p>
<f:view>
    <wap:form>         
        <wap:selectMany value="#{selectBean.equipments}" required="true" >
            <f:selectItems value="#{items.equipments}" />
        </wap:selectMany>

        <wap:commandLink action="ok"><wap:outputText value="submit" /></wap:commandLink>
        <wap:commandButton action="backToIndex" type="prev" value="examples" />
    </wap:form>      
</f:view>
</p>
</card>
</wml>

