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

<jsp:useBean id="q_form" class="net.sourceforge.myfaces.examples.example2.QuotationForm" scope="request" />


<f:use_faces>

    <x:page_layout id="page" layoutReference="pageLayout" panelClass="pageLayout" >
        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <x:page_body id="body" panelClass="pageBody" >

            <h:output_errors id="messageList" />

            <h:form id="q_form" formName="q_form">
                <h:input_textarea id="text"
                                  rows="5"
                                  modelReference="q_form.text"
                />
                <br>
                <br>
                <h:selectone_menu id="oneoption" modelReference="q_form.quoteChar" >
                    <h:selectitem id="item0" value="" label="select a quote character" />
                    <h:selectitem id="item1" value="\"" label="Double" />
                    <h:selectitem id="item2" value="'" label="Single" />
                    <h:selectitems id="moreItems" modelReference="q_form.selectOneItems" />
                </h:selectone_menu>
                <h:command_button id="button1" commandName="quotationOn" label="Add quotes">
                    <f:action_listener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:action_listener>
                </h:command_button>

                <br><br>
                <h:selectmany_listbox id="manyoptions" modelReference="q_form.selectManyValues" >
                    <h:selectitem id="item0" value="" label="select the unquote characters" />
                    <h:selectitems id="manyItems" modelReference="q_form.selectManyItems" />
                </h:selectmany_listbox>
                <h:command_button id="button2" commandName="quotationOff" label="Remove quotes"><br>
                    <f:action_listener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:action_listener>
                </h:command_button>

            </h:form>

        </x:page_body>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>