<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_4.tld" prefix="x"
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

<!--
managed beans used:
    q_form
-->

<f:use_faces>

    <x:page_layout id="page" layoutRef="globalOptions.pageLayout"
            panelClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >
        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <f:facet name="body">
            <h:panel_group id="body">

                <h:output_errors id="messageList" />

                <h:form id="q_form" formName="q_form">
                    <h:input_textarea id="text"
                                      rows="5"
                                      valueRef="q_form.text">
                            <f:validate_required />
                    </h:input_textarea>
                    <br>
                    <br>
                    <h:selectone_menu id="oneoption" valueRef="q_form.quoteChar" >
                        <h:selectitem id="item0" value="" label="select a quote character" />
                        <h:selectitem id="item1" value="\"" label="Double" />
                        <h:selectitem id="item2" value="'" label="Single" />
                        <h:selectitems id="moreItems" valueRef="q_form.selectOneItems" />
                    </h:selectone_menu>
                    <h:command_button id="button1" commandName="quotationOn" label="Add quotes" action="none">
                        <f:action_listener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:action_listener>
                    </h:command_button>

                    <br><br>
                    <h:selectmany_listbox id="manyoptions" valueRef="q_form.selectManyValues" >
                        <h:selectitem id="item0" value="" label="select the unquote characters" />
                        <h:selectitems id="manyItems" valueRef="q_form.selectManyItems" />
                    </h:selectmany_listbox>
                    <h:command_button id="button2" commandName="quotationOff" label="Remove quotes" action="none"><br>
                        <f:action_listener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:action_listener>
                    </h:command_button>

                </h:form>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>