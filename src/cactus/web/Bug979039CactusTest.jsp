<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
    <h:form id="testForm">
        <h:inputText id="input" value="#{commonRequestScopeBean.primitiveLong}">
            <f:convertNumber />
        </h:inputText>

        <h:commandButton id="submit" value="Submit"/>
    </h:form>
</f:view>
