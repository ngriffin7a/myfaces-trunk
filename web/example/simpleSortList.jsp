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
<jsp:useBean id="controller" class="net.sourceforge.myfaces.example.controller.SimpleCarController" scope="request" />

<f:use_faces>

    <table border="1"><tr>
        <td valign="top" width="150"><%@ include file="inc/navigation.jsp"  %></td>
        <td align="left" width="640" valign="top">
            <br>
            <f:errors id="errors" />

            <f:list id="list"
                    style="standardTable"
                    headerClass="standardTable_SortHeader"
                    footerClass="standardTable_Footer"
                    rowClasses="standardTable_Row1,standardTable_Row2" >
                <!-- SORTHEADER -->
                <x:sortheader id="list.header" commandReference="controller.sort" modelReference="list.sort" >
                    <x:sortcolumn id="list.header.col1" commandName="type">
                        <f:output_text id="list.header.name" text="Car-Type" />
                    </x:sortcolumn>
                    <x:sortcolumn id="list.header.col2" commandName="color" >
                        <f:output_text id="list.header.iso" text="Car-Color"  />
                    </x:sortcolumn>
                </x:sortheader>
                <!-- DATA -->
                <f:listrow id="list.tr" var="car" modelReference="list.cars" >
                    <f:output_text id="list.name" modelReference="car.type" />
                    <f:output_text id="list.isoCode" modelReference="car.color" />
                </f:listrow>
                <!-- FOOTER -->
                <f:group id="list.footer" >
                    <f:output_text id="list.footer.name" text="..."  />
                    <f:output_text id="list.footer.iso" />
                </f:group>
            </f:list>

        </td>
    </tr></table>

</f:use_faces>

</body>

</html>