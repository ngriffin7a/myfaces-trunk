<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"
%><html>

<%@include file="inc/head.inc" %>

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

<body>

<!--
managed beans used:
    calcForm
    ucaseForm
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

        <h:output_errors />

        <f:facet name="body">
            <h:panel_group id="body">

                <x:tabbed_pane bgcolor="#FFFFCC" >

                    <p> A common paragraph </p>

                    <x:tab id="tab1" label="Tab1">
                        <h:input_text id="inp1"/><br>
                        <h:input_text id="inp2"/>
                    </x:tab>

                    <x:tab id="tab2" label="Tab2">
                        <h:input_textarea ></h:input_textarea>
                    </x:tab>

                    <x:tab id="tab3" label="Tab3">
                        <h:input_text id="inp3"/><br>
                        <h:input_text id="inp4"/><br>
                        <h:input_text id="inp5"/><br>
                    </x:tab>

                    <br>
                    <h:command_button label="Common submit button" />

                </x:tabbed_pane>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panel_layout>

</f:view>

</body>

</html>