<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
    <h:form id="testForm">
        <h:inputText value="#{commonRequestScopeBean.primitiveLong}" />
        <h:inputHidden value="#{commonRequestScopeBean.primitiveLong}" />
    </h:form>
</f:view>
