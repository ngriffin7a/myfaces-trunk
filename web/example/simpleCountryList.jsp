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

            <f:list id="clist" style="standardTable" rowClasses="standard" >
                <f:tr id="clist.tr" var="country" modelReference="list.countries" >
                    <f:output_text id="clist.name" modelReference="country.name" />
                    <f:output_text id="clist.isoCode" modelReference="country.isoCode" />
                </f:tr>
            </f:list>
        </td>
    </tr></table>

</f:use_faces>

</body>

</html>