<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
    <h:form id="testForm">
        <h:selectBooleanCheckbox id="checkDisabled" value="#{bug972165Bean.checkDisabled}" disabled="true" />
        <h:selectBooleanCheckbox id="checkCommand" value="#{bug972165Bean.checkCommand}" onclick="submit()" immediate="true" />

        <h:commandButton id="submit" value="Submit"/>
    </h:form>

    <f:verbatim>CheckDisabled:</f:verbatim><h:outputText value="#{bug972165Bean.checkDisabled}" />
</f:view>
