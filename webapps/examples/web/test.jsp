<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x" %>
<html>
<body>
<p>before faces

<f:view>
    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="text" />

    <p>view start

    <x:panel_layout layout="classic" >

        <f:facet name="header">
            <h:messages globalOnly="false" layout="list" showDetail="true" />
        </f:facet>

        <f:facet name="body">
            <h:form id="testform">
                <h:output_text value="#{text['empty_selitem']}" />
                <h:input_text id="testinput1" value="#{calcForm.number1}" />
                    <h:message for="testform:testinput1" showSummary="false" showDetail="true" />
                <h:input_text id="testinput2" value="#{calcForm.number2}" />
                    <h:message for="testform:testinput2" showSummary="false" showDetail="true" />
                <h:command_button id="testbutton" />
                <f:verbatim><p>TEST1</p></f:verbatim>
            </h:form>
        </f:facet>

        <f:verbatim><p>TEST2</p></f:verbatim>

    </x:panel_layout>

    <p>view end
</f:view>

<p>after faces
</body>
</html>