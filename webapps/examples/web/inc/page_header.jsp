<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:facet name="header">
    <h:panel_group id="header">
        <h:graphic_image id="header_logo" url="images/logo_mini.jpg" alt="#{example_messages['alt_logo']}" />
        <f:verbatim>
            &nbsp;&nbsp;
            <font size="+1">MyFaces - The free JavaServer&#8482; Faces Implementation</font>
            <font size="-1">(Version 0.9)</font>
        </f:verbatim>
    </h:panel_group>
</f:facet>