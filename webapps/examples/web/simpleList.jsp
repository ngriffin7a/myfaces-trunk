<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_3.tld" prefix="x"
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

<jsp:useBean id="countryList" class="net.sourceforge.myfaces.examples.listexample.SimpleCountryList" scope="request" />

<f:use_faces>

    <x:page_layout id="page" layoutReference="pageLayout" cssClass="pageLayout" >
        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

            <h:errors id="errors" />

            <% int rows = 0, cols = 2; %>
            <h:list cssClass="standardTable"
                    headerClass="standardTable_Header"
                    footerClass="standardTable_Footer"
                    rowClasses="standardTable_Row1,standardTable_Row2"
                    columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column" >
                <!-- HEADER -->
                <h:group>
                    <h:output_text text="Country name" />
                    <h:output_text text="Iso-Code" />
                    <h:output_text text="Size" />
                </h:group>
                <!-- DATA -->
                <h:listrow var="country" modelReference="countryList.countries" >

                    <h:command_hyperlink>
                        <h:output_text modelReference="country.name" />

                        <f:parameter name="isoCode" modelReference="country.isoCode" />
                        <f:parameter name="name" modelReference="country.name" />
                        <f:parameter name="size" modelReference="country.size" />
                        <f:action_listener type="net.sourceforge.myfaces.examples.listexample.SimpleCountryController" />

                    </h:command_hyperlink>

                    <h:output_text modelReference="country.isoCode" />
                    <h:output_text modelReference="country.size" />
                    <% rows++; %>

                </h:listrow>
                <!-- FOOTER -->
                <h:group>
                    <h:output_text text="take a look at this runtime values ..."/>
                    <h:group>
                        <h:output_message id="rows_msg" msg="{0} rows" >
                            <f:parameter value="<%=new Integer(rows)%>"/>
                        </h:output_message>
                        &nbsp; / &nbsp;
                        <h:output_message id="cols_msg" msg="{0} cols" >
                            <f:parameter value="<%=new Integer(cols)%>"/>
                        </h:output_message>
                    </h:group>
                    <h:output_text text=""/>
                </h:group>
            </h:list>
            <br>

        </x:page_body>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>