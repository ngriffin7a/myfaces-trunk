<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%>
<%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="f"
%>
<%@ taglib uri="/WEB-INF/myfaces_ext.tld" prefix="x"
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

<f:use_faces>

    <x:page_layout id="page" layout="classic" cssClass="<%=pageLayout%>" >
        <x:page_header id="header" cssClass="pageHeader" >
            <f:image id="logo" url="images/logo_mini.jpg" altKey="alt_logo" altBundle="net.sourceforge.myfaces.example.example_messages" />
        </x:page_header>
        <%@ include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

<%@ include file="inc/features.html"  %>

        </x:page_body>

        <x:page_footer id="footer" cssClass="pageFooter" >
            Copyright (C) 2003  <a href="http://myfaces.sourceforge.net" style="color:#FFFFFF">The MyFaces Team</a>
        </x:page_footer>
    </x:page_layout>

</f:use_faces>

</body>

</html>