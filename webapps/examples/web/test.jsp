<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
<body>
<p>before faces

<f:view>
    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="text" />

    <p>view start

    <h:messages globalOnly="false" layout="list" showDetail="true" />

    <p>
    <h:form id="testform">
        <h:output_text value="#{text['empty_selitem']}" />
        <h:input_text id="testinput" value="#{calcForm.number1}" />
        <h:message for="testform:testinput" showSummary="false" showDetail="true" />
    </h:form>

    <p>view end
</f:view>

<p>after faces
</body>
</html>