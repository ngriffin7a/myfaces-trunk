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
    optionsForm
    optionsCtrl
-->

<f:view>

    <x:save_state id="ss1" valueRef="optionsForm.language" />

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
                <h:output_errors id="messageList" />

                <h4>Options</h4>
                <table border="1"><tr><td>
                    <h:form id="form1" formName="optionsForm">
                        <h:output_message key="option_lang" bundle="example_messages" />
                        <h:selectone_menu id="locale" valueRef="optionsForm.language">
                            <h:selectitems id="available" valueRef="optionsForm.availableLanguages" />
                        </h:selectone_menu><br>

                        <h:output_message key="option_layout" bundle="example_messages" />
                        <h:selectone_menu id="layout" valueRef="globalOptions.pageLayout"  >
                            <h:selectitem id="item101" label="Classic" value="classic" />
                            <h:selectitem id="item102" label="Navigation right" value="navigationRight" />
                            <h:selectitem id="item103" label="Upside down" value="upsideDown" />
                        </h:selectone_menu><br>

                        <h:command_button id="apply" commandName="apply" actionRef="optionsCtrl.localeAction" label="Apply"/>
                    </h:form>

                </td></tr></table>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panel_layout>

</f:view>

</body>

</html>