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

<jsp:useBean id="countryList" class="net.sourceforge.myfaces.example.model.SimpleCountryList" scope="request" />

<f:use_faces>

    <x:page_layout id="page" layout="classic" cssClass="<%=pageLayout%>" >
        <x:page_header id="header" cssClass="pageHeader" >
            <f:image id="logo" url="images/logo_mini.jpg" altKey="alt_logo" altBundle="net.sourceforge.myfaces.example.example_messages" />
        </x:page_header>
        <%@ include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

            <f:errors id="errors" />

            <% int rows = 0, cols = 2; %>
            <f:list id="countryList"
                    cssClass="standardTable"
                    headerClass="standardTable_Header"
                    footerClass="standardTable_Footer"
                    rowClasses="standardTable_Row1,standardTable_Row2"
                    columnClasses="standardTable_Column,standardTable_ColumnCentered" >
                <!-- HEADER -->
                <f:group id="header" >
                    <f:output_text id="header.name" text="Country name" />
                    <f:output_text id="header.isoCode" text="Iso-Code" />
                </f:group>
                <!-- DATA -->
                <f:listrow id="countryList.tr" var="country" modelReference="countryList.countries" >
                    <f:output_text id="countryList.name" modelReference="country.name" />
                    <f:group id="g1" >
                        <a href="#"><f:output_text id="countryList.isoCode" modelReference="country.isoCode" /></a>
                        <% rows++; %>
                    </f:group>
                </f:listrow>
                <!-- FOOTER -->
                <f:group id="footer" >
                    <f:output_text id="footer.1" text="take a look at this runtime values ..."/>
                    <f:group id="g2" >
                        <f:message id="row_count" msg="{0} rows" >
                            <f:param id="rows" value="<%=new Integer(rows)%>"/>
                        </f:message>
                        &nbsp; / &nbsp;
                        <f:message id="col_count" msg="{0} cols" >
                            <f:param id="cols" value="<%=new Integer(cols)%>"/>
                        </f:message>
                    </f:group>
                </f:group>
            </f:list>
            <br>

        </x:page_body>

        <x:page_footer id="footer" cssClass="pageFooter" >
            Copyright (C) 2003  <a href="http://myfaces.sourceforge.net" style="color:#FFFFFF">The MyFaces Team</a>
        </x:page_footer>
    </x:page_layout>

</f:use_faces>

</body>

</html>