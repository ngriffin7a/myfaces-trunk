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

<head>
  <meta HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
  <title>Simple Country List</title>
  <Link rel="stylesheet" type="text/css" href="css/basic.css">
</head>

<body>

<jsp:useBean id="list" class="net.sourceforge.myfaces.example.model.SimpleCountryList" scope="request" />

<f:use_faces>

    <table border="1"><tr>
        <td valign="top" width="140"><%@ include file="inc/navigation.jsp"  %></td>
        <td align="left" width="640">

            <x:message_list id="messageList" />

            <f:list id="clist"
                    style="standardTable"
                    headerClass="standardTable_Header"
                    footerClass="standardTable_Footer"
                    rowClasses="standardTable_Row1,standardTable_Row2" >
                <f:group id="clist.header" >
                    <f:output_text id="clist.header.name" text="Country"  />
                    <f:output_text id="clist.header.iso" text="ISO-3166 Code"  />
                </f:group>
                <f:listrow id="clist.tr" var="country" modelReference="list.countries" >
                    <f:output_text id="clist.name" modelReference="country.name" />
                    <f:output_text id="clist.isoCode" modelReference="country.isoCode" />
                </f:listrow>
                <f:group id="groupList.g1" >
                    <f:output_text id="groupList.g1.name" text="..."  />
                    <f:output_text id="groupList.g1.iso" />
                </f:group>
            </f:list>

        </td>
    </tr></table>

</f:use_faces>

</body>

</html>