<%@ page session="false"
%><%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="h"
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

<jsp:useBean id="countryList" class="net.sourceforge.myfaces.examples.diverse.model.SimpleCountryList" scope="request" />

<h:use_faces>

    <x:page_layout id="page" layoutReference="pageLayout" cssClass="pageLayout" >
        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

            <h:errors id="errors" />

            <% int rows = 0, cols = 2; %>
            <h:list id="countryList"
                    cssClass="standardTable"
                    headerClass="standardTable_Header"
                    footerClass="standardTable_Footer"
                    rowClasses="standardTable_Row1,standardTable_Row2"
                    columnClasses="standardTable_Column,standardTable_ColumnCentered" >
                <!-- HEADER -->
                <h:group id="list_header" >
                    <h:output_text id="header_name" text="Country name" />
                    <h:output_text id="header_isoCode" text="Iso-Code" />
                </h:group>
                <!-- DATA -->
                <h:listrow id="countryList_tr" var="country" modelReference="countryList.countries" >
                    <h:output_text id="countryList_name" modelReference="country.name" />
                    <h:group id="g1" >
                        <a href="#"><h:output_text id="countryList_isoCode" modelReference="country.isoCode" /></a>
                        <% rows++; %>
                    </h:group>
                </h:listrow>
                <!-- FOOTER -->
                <h:group id="list_footer" >
                    <h:output_text id="footer_1" text="take a look at this runtime values ..."/>
                    <h:group id="g2" >
                        <h:message id="row_count" msg="{0} rows" >
                            <h:param id="rows" value="<%=new Integer(rows)%>"/>
                        </h:message>
                        &nbsp; / &nbsp;
                        <h:message id="col_count" msg="{0} cols" >
                            <h:param id="cols" value="<%=new Integer(cols)%>"/>
                        </h:message>
                    </h:group>
                </h:group>
            </h:list>
            <br>

        </x:page_body>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</h:use_faces>

</body>

</html>