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

    <table border="1"><tr>
        <td valign="top" width="150" class="standard"><%@ include file="inc/navigation.jsp"  %></td>
        <td align="left" width="640" valign="top">
<h3>Webpages</h3>
 <a href="http://myfaces.sourceforge.net/" target="_blank">Project Homepage</a><br>
 <a href="http://www.sourceforge.net/projects/myfaces" target="_blank">MyFaces@Sourceforge</a><br>
<h3>Project Members</h3>
 <a href="http://sourceforge.net/project/memberlist.php?group_id=69709">MyFaces Team Members@Sourceforge</a><br>

</f:use_faces>

</body>

</html>