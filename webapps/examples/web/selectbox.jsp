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

    <x:panel_layout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <f:facet name="body">
            <h:panel_group id="body">
                <h:form name="formName">

                    <h:panel_grid columns="2">

                        <h:output_label for="selone_lb" value="#{example_messages['label_cars']}" />
                        <h:selectone_listbox value="#{carconf.car}" >
                            <f:selectitems id="selone_lb_cars" value="#{carconf.cars}" />
                        </h:selectone_listbox>

                        <h:output_label for="selone_menu" value="#{example_messages['label_colors']}" />
                        <h:selectone_menu size="3" value="#{carconf.color}" >
                            <f:selectitem itemValue="" itemLabel="#{example_messages['empty_selitem']}" />
                            <f:selectitems value="#{carconf.colors}" />
                        </h:selectone_menu>

                        <h:output_label for="selone_menu" value="#{example_messages['label_extras']}" />
                        <h:selectmany_checkboxlist value="#{carconf.extras}" >
                            <f:selectitems value="#{carconf.extrasList}" />
                        </h:selectmany_checkboxlist>

                        <f:verbatim>&nbsp;</f:verbatim>
                        <h:panel_group >
                            <h:selectone_radio id="r1" value="#{carconf.discount}" layout="PAGE_DIRECTION"  >
                                <f:selectitem itemValue="0" itemLabel="#{example_messages['discount_0']}" />
                                <f:selectitem itemValue="1" itemLabel="#{example_messages['discount_1']}"  />
                                <f:selectitem itemValue="2" itemLabel="#{example_messages['discount_2']}"  />
                            </h:selectone_radio>
                        </h:panel_group>

                        <f:verbatim>&nbsp;</f:verbatim>
                        <h:panel_group >
                            <f:verbatim><br></f:verbatim>
                            <h:selectone_radio id="r2" value="#{carconf.discount2}" layout="PAGE_DIRECTION"  >
                                <f:selectitem itemValue="0" itemLabel="#{example_messages['discount_2_0']}" />
                                <f:selectitem itemValue="1" itemLabel="#{example_messages['discount_2_1']}" />
                            </h:selectone_radio>
                        </h:panel_group>

                        <f:verbatim>&nbsp;</f:verbatim>
                        <h:command_button action="#{carconf.calcPrice}" value="#{example_messages['button_calcprice']}" />

                    </h:panel_grid>
                </h:form>

                <h:output_message value="#{example_messages['msg_price']}" >
                    <f:parameter value="#{carconf.price}" />
                </h:output_message>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panel_layout>

</f:view>

</body>

</html>