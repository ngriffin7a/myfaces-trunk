<%@ page session="false"
%><%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="h"
%><%@ taglib uri="/WEB-INF/myfaces_core.tld" prefix="f"
%><%@ taglib uri="/WEB-INF/myfaces_ext.tld" prefix="x"
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

<jsp:useBean id="list" class="net.sourceforge.myfaces.examples.listexample.SimpleSortableCarList" scope="request" />
<!--x:save_state id="ss1" modelReference="list.sort" /-->
<!--x:save_state id="ss2" modelReference="list.ascending" /-->

    <x:page_layout id="page" layoutReference="pageLayout" cssClass="pageLayout" >
        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

            <h:errors id="errors" />

            <h:list cssClass="standardTable"
                    headerClass="standardTable_SortHeader"
                    footerClass="standardTable_Footer"
                    rowClasses="standardTable_Row1,standardTable_Row2" >
                <!-- SORTHEADER -->
                <x:sortheader column="type"
                              ascending="<%=true%>"
                              columnReference="list.sort"
                              ascendingReference="list.ascending" >
                    <x:sortcolumn column="type" cssClass="sortLink" >
                        <h:output_text text="Car-Type" />
                    </x:sortcolumn>
                    <x:sortcolumn column="color" cssClass="sortLink" >
                        <h:output_text id="list_header_iso" text="Car-Color"  />
                    </x:sortcolumn>
                </x:sortheader>
                <!-- DATA -->
                <h:listrow var="car" modelReference="list.cars" >
                    <h:output_text modelReference="car.type" />
                    <h:output_text modelReference="car.color" />
                </h:listrow>
                <!-- FOOTER -->
                <h:group>
                    <h:output_text text="..."  />
                    <h:output_text text=""/>
                </h:group>
            </h:list>

        </x:page_body>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>