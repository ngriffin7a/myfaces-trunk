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

<jsp:useBean id="q_form" class="net.sourceforge.myfaces.example.model.QuotationForm" scope="request" />
<jsp:useBean id="q_controller" class="net.sourceforge.myfaces.example.controller.QuotationController" scope="application" />


<f:use_faces>

    <x:page_layout id="page" layout="classic" cssClass="<%=pageLayout%>" >
        <x:page_header id="header" cssClass="pageHeader" >
            <f:image id="logo" url="images/logo_mini.jpg" altKey="alt_logo" altBundle="net.sourceforge.myfaces.example.example_messages" />
        </x:page_header>
        <%@ include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

            <f:errors id="messageList" />

            <f:form id="q_form" formName="q_form">
                <f:textentry_input id="text"
                                   modelReference="q_form.text"
                                   size="60" /><br>
                <br>
                <f:selectbox id="oneoption" modelReference="q_form.quoteChar" >
                    <f:selectbox_item id="item0" value="" label="select a quote character" />
                    <f:selectbox_item id="item1" value="\"" label="Double" />
                    <f:selectbox_item id="item2" value="'" label="Single" />
                    <f:selectbox_items id="moreItems" modelReference="q_form.selectOneItems" />
                </f:selectbox>
                <f:command_button id="button1" commandName="quotationOn" commandReference="q_controller.processEvent" label="Add quotes"/>

                <br><br>
                <f:listbox_many id="manyoptions" modelReference="q_form.selectManyValues" >
                    <f:selectbox_item id="item0" value="" label="select the unquote characters" />
                    <f:selectbox_items id="manyItems" modelReference="q_form.selectManyItems" />
                </f:listbox_many>
                <f:command_button id="button2" commandName="quotationOff"  commandReference="q_controller.processEvent" label="Remove quotes"/><br>

            </f:form>

        </x:page_body>

        <x:page_footer id="footer" cssClass="pageFooter" >
            Copyright (C) 2003  <a href="http://myfaces.sourceforge.net" style="color:#FFFFFF">The MyFaces Team</a>
        </x:page_footer>
    </x:page_layout>

</f:use_faces>

</body>

</html>