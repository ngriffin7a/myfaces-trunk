<%@ page session="false"
%><%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="f"
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

<%@include file="inc/header.inc" %>

<body>

<jsp:useBean id="list" class="net.sourceforge.myfaces.example.model.SimpleCarList" scope="request" />

<f:use_faces>

    <x:page_layout id="page" layout="classic" cssClass="<%=pageLayout%>" >
        <x:page_header id="header" cssClass="pageHeader" >
            <f:image id="logo" url="images/logo_mini.jpg" altKey="alt_logo" altBundle="net.sourceforge.myfaces.example.example_messages" />
        </x:page_header>
        <%@ include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

            <f:errors id="errors" />

            <f:list id="list"
                    cssClass="standardTable"
                    headerClass="standardTable_SortHeader"
                    footerClass="standardTable_Footer"
                    rowClasses="standardTable_Row1,standardTable_Row2" >
                <!-- SORTHEADER -->
                <x:sortheader id="list_header" column="type"
                                               ascending="<%=true%>"
                                               columnReference="list.sort"
                                               ascendingReference="list.ascending" >
                    <x:sortcolumn id="list_header_col1" column="type" cssClass="sortLink" >
                        <f:output_text id="list_header_name" text="Car-Type" />
                    </x:sortcolumn>
                    <x:sortcolumn id="list_header_col2" column="color" cssClass="sortLink" >
                        <f:output_text id="list_header_iso" text="Car-Color"  />
                    </x:sortcolumn>
                </x:sortheader>
                <!-- DATA -->
                <f:listrow id="list_tr" var="car" modelReference="list.cars" >
                    <f:output_text id="list_name" modelReference="car.type" />
                    <f:output_text id="list_isoCode" modelReference="car.color" />
                </f:listrow>
                <!-- FOOTER -->
                <f:group id="list_footer" >
                    <f:output_text id="list_footer_name" text="..."  />
                    <f:output_text id="list_footer_iso" />
                </f:group>
            </f:list>

        </x:page_body>

        <x:page_footer id="footer" cssClass="pageFooter" >
            Copyright (C) 2003  <a href="http://myfaces.sourceforge.net" style="color:#FFFFFF">The MyFaces Team</a>
        </x:page_footer>
    </x:page_layout>

</f:use_faces>

</body>

</html>