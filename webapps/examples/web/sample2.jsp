<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"
%><html>

<!--
/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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

<f:view>

    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="example_messages"/>

    <x:panel_layout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <f:facet name="body">
            <h:panel_group id="body">

                <h:messages id="messageList" />

                <h:form id="q_form" name="q_form">
                    <h:input_textarea id="text"
                                      rows="5"
                                      value="#{q_form.text}" required="true" />
                    <f:verbatim><br><br></f:verbatim>
                    <h:selectone_menu id="oneoption" value="#{q_form.quoteChar}" >
                        <f:selectitem itemValue="" itemLabel="select a quote character" />
                        <f:selectitem itemValue="\"" itemLabel="Double" />
                        <f:selectitem itemValue="'" itemLabel="Single" />
                        <f:selectitems value="#{q_form.selectOneItems}" />
                    </h:selectone_menu>
                    <h:command_button id="button1" value="Add quotes" action="none">
                        <f:action_listener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:action_listener>
                    </h:command_button>

                    <f:verbatim><br><br></f:verbatim>
                    <h:selectmany_listbox id="manyoptions" value="#{q_form.selectManyValues}" >
                        <f:selectitem itemValue="" itemLabel="select the unquote characters" />
                        <f:selectitems value="#{q_form.selectManyItems}" />
                    </h:selectmany_listbox>
                    <h:command_button id="button2" value="Remove quotes" action="none"><f:verbatim><br></f:verbatim>
                        <f:action_listener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:action_listener>
                    </h:command_button>

                </h:form>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panel_layout>

</f:view>

</body>

</html>