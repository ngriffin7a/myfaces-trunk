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

                <h:output_errors id="errors" />

                <% int rows = 0, cols = 2; %>
                <h:panel_list panelClass="standardTable"
                        headerClass="standardTable_Header"
                        footerClass="standardTable_Footer"
                        rowClasses="standardTable_Row1,standardTable_Row2"
                        columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column" >
                    <!-- HEADER -->
                    <h:panel_group>
                        <h:output_text id="hdr1" value="Country name" />
                        <h:output_text id="hdr2" value="Iso-Code" />
                        <h:output_text id="hdr3" value="Size" />
                    </h:panel_group>
                    <!-- DATA -->
                    <h:panel_data var="country" valueRef="countryList.countries" >

                        <h:command_hyperlink>
                            <h:output_text valueRef="country.name" />

                            <f:parameter name="isoCode" valueRef="country.isoCode" />
                            <f:parameter name="name" valueRef="country.name" />
                            <f:parameter name="size" valueRef="country.size" />
                            <f:action_listener type="net.sourceforge.myfaces.examples.listexample.SimpleCountryController" />

                        </h:command_hyperlink>

                        <h:output_text id="col2" valueRef="country.isoCode" />
                        <h:output_text id="col3" valueRef="country.size" />
                        <% rows++; %>

                    </h:panel_data>
                    <!-- FOOTER -->
                    <h:panel_group>
                        <h:output_text value="take a look at this runtime values ..."/>
                        <h:panel_group>
                            <h:output_message id="rows_msg" value="{0} rows" >
                                <f:parameter value="<%=new Integer(rows)%>"/>
                            </h:output_message>
                            &nbsp; / &nbsp;
                            <h:output_message id="cols_msg" value="{0} cols" >
                                <f:parameter value="<%=new Integer(cols)%>"/>
                            </h:output_message>
                        </h:panel_group>
                        <h:output_text value=""/>
                    </h:panel_group>
                </h:panel_list>
                <br>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>