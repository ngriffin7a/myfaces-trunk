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

<!--
managed beans used:
    countryList
-->

<f:view>

    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="example_messages"/>

    <x:panelLayout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <%@include file="inc/page_header.jsp" %>
        <f:facet name="navigation">
            <f:subview id="menu" >
                <jsp:include page="inc/navigation.jsp" />
            </f:subview>
        </f:facet>

        <f:facet name="body">
            <h:panelGroup id="body">

               <h:messages errorClass="error" showSummary="true" showDetail="true" />

                <h:form id="form" style="display:inline" >
                <x:dataTable id="data"
                        styleClass="standardTable"
                        headerClass="standardTable_Header"
                        rowClasses="standardTable_Row1,standardTable_Row2"
                        columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                        var="country"
                        value="#{countryList.countries}"
                        preserveDataModel="true">
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_name']}" />
                       </f:facet>
                       <h:inputText id="cname" value="#{country.name}" required="true" />
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_iso']}" />
                       </f:facet>
                       <h:inputText id="ciso" value="#{country.isoCode}" required="true" >
                                <f:validateLength maximum="2" minimum="2"/>
                       </h:inputText>

                   </h:column>

                   <f:facet name="footer">
                        <h:panelGroup>
                            <h:commandButton action="go_back" value="#{example_messages['button_save']}" />
                            <f:verbatim>&nbsp;</f:verbatim>
                            <h:commandButton action="go_back" immediate="true" value="#{example_messages['button_cancel']}" />
                            <f:verbatim>&nbsp;</f:verbatim>
                            <h:commandButton value="#{example_messages['button_apply']}" />
                        </h:panelGroup>
                   </f:facet>

                </x:dataTable>
                </h:form>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>