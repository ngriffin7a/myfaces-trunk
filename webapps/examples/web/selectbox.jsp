<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_4.tld" prefix="x"
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

<f:use_faces>

    <x:page_layout id="page" layoutRef="globalOptions.pageLayout"
            panelClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >
        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <f:facet name="body">
            <h:panel_group id="body">
                <h:form formName="formName">

                    <h:panel_grid columns="2">

                        <h:output_label for="selone_lb" key="label_cars" bundle="example_messages"></h:output_label>
                        <h:selectone_listbox valueRef="carconf.car" >
                            <h:selectitems id="selone_lb_cars" valueRef="carconf.cars" />
                        </h:selectone_listbox>

                        <h:output_label for="selone_menu" key="label_colors" bundle="example_messages"></h:output_label>
                        <h:selectone_menu size="3" valueRef="carconf.color" >
                            <h:selectitem key="empty_selitem" bundle="example_messages" ></h:selectitem>
                            <h:selectitems valueRef="carconf.colors" />
                        </h:selectone_menu>

                        <h:output_label for="selone_menu" key="label_extras" bundle="example_messages"></h:output_label>
                        <br>
                        <h:selectmany_checkbox valueRef="carconf.extras" >
                            <h:selectitems valueRef="carconf.extrasList" />
                        </h:selectmany_checkbox>

                        <h:output_text value="" />
                        <h:panel_group >
                            <h:selectone_radio id="r1" valueRef="carconf.discount" layout="PAGE_DIRECTION"  >
                                <h:selectitem value="0" key="discount_0" bundle="example_messages" selected="true" />
                                <h:selectitem value="1" key="discount_1" bundle="example_messages"  />
                                <h:selectitem value="2" key="discount_2" bundle="example_messages"  />
                            </h:selectone_radio>
                            <!-- h:output_label for="cb1" key="discount_1" bundle="example_messages"> /h:output_label> -->
                        </h:panel_group>

                        <h:output_text value="" />
                        <h:panel_group >
                            <br>
                            <x:selectone_radio id="r2" valueRef="carconf.discount2" layout="PAGE_DIRECTION"  >
                                <h:selectitem value="0" key="discount_2_0" bundle="example_messages" selected="true" />
                                <br><b>
                                <h:output_message key="radio_hint" bundle="example_messages" /></b>
                                <h:selectitem value="1" key="discount_2_1" bundle="example_messages"  />
                            </x:selectone_radio>
                            <!-- h:output_label for="cb1" key="discount_1" bundle="example_messages"> /h:output_label> -->
                        </h:panel_group>
                        <h:output_text value="" />
                        <h:command_button actionRef="carconf.calcPriceAction" key="button_calcprice" bundle="example_messages"/>

                    </h:panel_grid>
                </h:form>

                <h:output_message key="msg_price" bundle="example_messages" >
                    <f:parameter valueRef="carconf.price" />
                </h:output_message>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>