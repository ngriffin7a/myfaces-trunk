<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="messages example">
<p>
<f:view>
    <wap:form> 
        Enter number from 0 to 10:
        <wap:inputText id="inputNumber1" value="#{integerBean.number}" format="2N" >
            <f:validateLongRange minimum="0" maximum="10" />
        </wap:inputText>

        This value is required:
        <wap:inputText id="inputString1" value="#{stringBean.name}" required="true" />

        <wap:messages />

        <br/>
        <wap:commandLink action="success" ><wap:outputText value="submit" /></wap:commandLink>
        <wap:commandButton action="backToIndex" type="prev" value="examples" />
    </wap:form>      
</f:view>
</p>
</card>
</wml>

