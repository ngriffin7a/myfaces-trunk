<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
    <h:form id="testForm">
        <h:inputText value="#{commonRequestScopeBean.primitiveLong}" />
        <h:inputHidden id="hidden" value="#{commonRequestScopeBean.primitiveLong}" />
        <h:outputText id="valueSet" value="#{commonRequestScopeBean.primitiveLongSet}" />

        <h:commandButton id="submit" value="Submit"/>
    </h:form>
</f:view>
