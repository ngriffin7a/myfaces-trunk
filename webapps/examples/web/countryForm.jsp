<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"
%><html>

<!--
/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
//-->

<%@include file="inc/head.inc" %>

<body>

<f:view>

    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="example_messages"/>

    <x:panelLayout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <f:facet name="body">
            <h:panelGroup>
                <h:form id="countryForm" name="countryForm">
                    <x:saveState value="#{countryForm.id}" />
                    <h:panelGrid columns="2" styleClass="standardTable" >
                        <h:outputLabel for="name" value="#{example_messages['label_country_name']}"/>
                        <h:panelGroup>
                            <h:inputText id="name" value="#{countryForm.name}" required="true" />
                            <h:message for="countryForm:name" styleClass="error" showDetail="true" showSummary="false" />
                        </h:panelGroup>


                        <h:outputLabel for="isoCode" value="#{example_messages['label_country_iso']}"/>
                        <h:panelGroup>
                            <h:inputText id="isoCode" value="#{countryForm.isoCode}" required="true">
                                <f:validateLength maximum="2" minimum="2"/>
                            </h:inputText>
                            <h:message for="countryForm:isoCode" styleClass="error" showDetail="true" showSummary="false" />
                        </h:panelGroup>

                        <h:panelGroup/>
                        <h:panelGroup>
                            <h:commandButton action="#{countryForm.save}" value="#{example_messages['button_save']}" />
                            <f:verbatim>&nbsp;</f:verbatim>
                            <h:commandButton action="cancel" immediate="true" value="#{example_messages['button_cancel']}" />
                            <f:verbatim>&nbsp;</f:verbatim>
                            <h:commandButton action="#{countryForm.delete}" immediate="true" value="#{example_messages['button_delete']}" />
                            <f:verbatim>&nbsp;</f:verbatim>
                            <h:commandButton action="#{countryForm.apply}" value="#{example_messages['button_apply']}" />
                        </h:panelGroup>

                    </h:panelGrid>
                </h:form>
            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>
