<%@ page session="false"
%><%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="h"
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

<h:use_faces>

    <x:page_layout id="page" layout="classic" cssClass="<%=pageLayout%>" >
        <x:page_header id="header" cssClass="pageHeader" >
            <h:image id="logo" url="images/logo_mini.jpg" altKey="alt_logo" altBundle="net.sourceforge.myfaces.example.example_messages" />
        </x:page_header>
        <%@ include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >
            <h:errors id="messageList" />

            <h4>Options</h4>
            <table border="1"><tr><td>
                <h:form id="form1" formName="calcForm">
                    <h:message id="msg_lang" key="option_lang" bundle="net.sourceforge.myfaces.example.example_messages" ></h:message>
                    <h:selectbox id="locales" modelReference="optionsForm.language"  >
                        <h:selectbox_items id="available" modelReference="optionsForm.availableLanguages" />
                    </h:selectbox>
                    <h:command_button id="apply" commandName="apply" commandReference="optionsCtrl.setLocale" label="Apply"/>
                </h:form>

            </td></tr></table>

        </x:page_body>

        <x:page_footer id="footer" cssClass="pageFooter" >
            Copyright (C) 2003  <a href="http://myfaces.sourceforge.net" style="color:#FFFFFF">The MyFaces Team</a>
        </x:page_footer>
    </x:page_layout>

</h:use_faces>

</body>

</html>