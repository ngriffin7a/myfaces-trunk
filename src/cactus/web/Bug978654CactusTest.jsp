<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
    <h:form id="testForm">
        <h:inputText value="#{commonRequestScopeBean.integer}" />
        <h:inputHidden id="hidden" value="#{commonRequestScopeBean.integer}" />
        <h:outputText id="value" value="#{commonRequestScopeBean.integer}" />

        <h:commandButton id="submit" value="Submit"/>
    </h:form>
</f:view>
