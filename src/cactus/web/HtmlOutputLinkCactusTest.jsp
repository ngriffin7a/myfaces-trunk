<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>

    <h:outputLink id="outputLink1" value="http://www.myfaces.org/">
        <h:outputText value="MyFaces1"/>
        <f:param name="p1" value="v1"/>
        <f:param name="p2" value="v2"/>
        <f:param name="p3" value="v3"/>
    </h:outputLink>

    <h:outputLink id="outputLink2" value="http://www.myfaces.org/?p1=v1">
        <h:outputText value="MyFaces2"/>
        <f:param name="p2" value="v2"/>
        <f:param name="p3" value="v3"/>
    </h:outputLink>

</f:view>
