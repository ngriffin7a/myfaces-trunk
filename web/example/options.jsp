<%@ page session="false"
%><%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="f"
%><%@ taglib uri="/WEB-INF/myfaces_ext.tld" prefix="x"
%><html>

<%@include file="inc/header.inc" %>

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

<body>

<jsp:useBean id="optionsForm" class="net.sourceforge.myfaces.example.model.OptionsForm" scope="request" />
<jsp:useBean id="optionsCtrl" class="net.sourceforge.myfaces.example.controller.OptionsController" scope="application" />

<f:use_faces>

    <table border="1"><tr>
        <td valign="top" width="150"><%@ include file="inc/navigation.jsp"  %></td>
        <td align="left" width="640" valign="top">
            <br>

            <f:errors id="messageList" />

            <h4>Options</h4>
            <table border="1"><tr><td>
                <f:form id="form1" formName="calcForm">
                    View this page in
                    <f:selectbox id="locales" modelReference="optionsForm.language"  >
                        <f:selectbox_items id="available" modelReference="optionsForm.availableLanguages" />
                    </f:selectbox>
                    <f:command_button id="apply" commandName="apply" commandReference="optionsCtrl.setLocale" label="Apply"/>
                </f:form>

            </td></tr></table>

        </td>
    </tr></table>

</f:use_faces>

</body>

</html>