<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="outputText example">
<p>
<f:view>    
    <i>Today is:</i><br/>

    <wap:outputText value="#{todayDate.date}" >
        <f:convertDateTime dateStyle="full" />
    </wap:outputText>
    <wap:form>
	    <wap:commandButton action="backToIndex" type="prev" value="examples" />
    </wap:form>
</f:view>
</p>
</card>
</wml>

