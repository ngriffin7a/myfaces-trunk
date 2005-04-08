<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="inputFields example">
<p>
<f:view>
    <wap:form> 
        <i>Name:</i><br/>
        <wap:inputText id="name1" value="#{stringBean.name}" required="true" />
        <i>Password:</i><br/>
        <wap:inputSecret value="#{stringBean.password}" />

        <wap:message for="name1" />

        <wap:commandButton action="login" value="login" />
        <wap:commandButton value="reset" type="reset" />
        <wap:commandButton action="backToIndex" type="prev" value="examples" />
    </wap:form>      
</f:view>
</p>
</card>
</wml>

