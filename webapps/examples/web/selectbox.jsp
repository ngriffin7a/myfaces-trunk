<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"
%><html>

<%@include file="inc/head.inc" %>

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

<body>

<!--
managed beans used:
    carconf
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
                <h:form name="formName">

                    <h:panelGrid columns="2">

                        <h:outputLabel for="selone_lb" value="#{example_messages['label_cars']}" />
                        <h:selectOneListbox size="3" value="#{carconf.car}">
                            <f:selectItems id="selone_lb_cars" value="#{carconf.cars}" />
                        </h:selectOneListbox>

                        <h:outputLabel for="selone_menu" value="#{example_messages['label_colors']}" />
                        <h:selectOneMenu value="#{carconf.color}" >
                            <f:selectItem itemValue="" itemLabel="#{example_messages['empty_selitem']}" />
                            <f:selectItems value="#{carconf.colors}" />
                        </h:selectOneMenu>

                        <h:outputLabel for="selone_menu" value="#{example_messages['label_extras']}" />
                        <h:selectManyCheckbox value="#{carconf.extras}" >
                            <f:selectItems value="#{carconf.extrasList}" />
                        </h:selectManyCheckbox>

                        <f:verbatim>&nbsp;</f:verbatim>
                        <h:panelGroup >
                            <h:selectOneRadio id="r1" value="#{carconf.discount}" layout="PAGE_DIRECTION"  >
                                <f:selectItem itemValue="0" itemLabel="#{example_messages['discount_0']}" />
                                <f:selectItem itemValue="1" itemLabel="#{example_messages['discount_1']}"  />
                                <f:selectItem itemValue="2" itemLabel="#{example_messages['discount_2']}"  />
                            </h:selectOneRadio>
                        </h:panelGroup>

                        <f:verbatim>&nbsp;</f:verbatim>
                        <h:panelGroup >
                            <f:verbatim><br></f:verbatim>
                            <h:selectOneRadio id="r2" value="#{carconf.discount2}" layout="PAGE_DIRECTION"  >
                                <f:selectItem itemValue="0" itemLabel="#{example_messages['discount_2_0']}" />
                                <f:selectItem itemValue="1" itemLabel="#{example_messages['discount_2_1']}" />
                            </h:selectOneRadio>
                        </h:panelGroup>

                        <f:verbatim>&nbsp;</f:verbatim>
                        <h:commandButton action="#{carconf.calcPrice}" value="#{example_messages['button_calcprice']}" />

                    </h:panelGrid>
                </h:form>

                <h:outputFormat value="#{example_messages['msg_price']}" >
                    <f:param value="#{carconf.price}" />
                </h:outputFormat>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>