<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_4.tld" prefix="x"
%><html>

<!--
/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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

<f:use_faces>

<!--
managed beans used:
    list
-->

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

                <h:panel_list panelClass="standardTable"
                        headerClass="standardTable_SortHeader"
                        footerClass="standardTable_Footer"
                        rowClasses="standardTable_Row1,standardTable_Row2" >
                    <!-- SORTHEADER -->
                    <x:sortheader column="type"
                                  ascending="true"
                                  columnReference="list.sort"
                                  ascendingReference="list.ascending" >
                        <x:sortcolumn id="hdr1" column="type" commandClass="sortLink" key="sort_cartype" bundle="example_messages" />
                        <x:sortcolumn id="hdr2" column="color" commandClass="sortLink" key="sort_carcolor" bundle="example_messages" />
                    </x:sortheader>
                    <!-- DATA -->
                    <h:panel_data var="car" valueRef="list.cars" >
                        <h:output_text id="col1" valueRef="car.type" />
                        <h:output_text id="col2" valueRef="car.color" />
                    </h:panel_data>
                    <!-- FOOTER -->
                    <h:panel_group>
                        <h:output_text id="ftr1" value="..."  />
                        <h:output_text id="ftr2" value=""/>
                    </h:panel_group>
                </h:panel_list>

            </h:panel_group>
        </f:facet>

            <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>