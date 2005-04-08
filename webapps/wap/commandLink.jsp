<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="cart1" title="CommandLink example">
<p>
<f:view>
    <wap:form> 
        <i>Name:</i>
        <wap:inputText value="#{stringBean.name}" />
           
        <wap:commandLink action="ok">
            <b><wap:outputText value="send"/></b>
        </wap:commandLink><br/>
        <wap:commandLink action="commandButton"><wap:outputText value="commandButton example"/></wap:commandLink>
        <wap:commandButton action="backToIndex" type="prev" value="examples" />
    </wap:form>      
</f:view>
</p>
</card>
</wml>

