<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:facet name="header">
    <h:panelGroup id="header">
        <h:graphicImage id="header_logo" url="images/logo_mini.jpg" alt="#{example_messages['alt_logo']}" />
        <f:verbatim>
            &nbsp;&nbsp;
            <font size="+1">MyFaces - The free JavaServer&#8482; Faces Implementation</font>
            <font size="-1">(Version 0.9.1)</font>
        </f:verbatim>
    </h:panelGroup>
</f:facet>