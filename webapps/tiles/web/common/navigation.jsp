<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<h:panelGrid columns="1" >
    <h:commandLink action="nav_page1">
        <h:outputText value="Page1" />
    </h:commandLink>
    <h:commandLink action="nav_page2">
        <h:outputText value="Page2" />
    </h:commandLink>
</h:panelGrid>
