<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"%>
<html>

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

    <x:saveState id="ss1" value="#{optionsForm.language}" />

    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="example_messages"/>

    <x:panelLayout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <f:facet name="header">
            <f:subview id="header">
                <jsp:include page="inc/page_header.jsp" />
            </f:subview>
        </f:facet>

        <f:facet name="navigation">
            <f:subview id="menu" >
                <jsp:include page="inc/navigation.jsp" />
            </f:subview>
        </f:facet>


        <f:facet name="body">
            <h:panelGroup id="body">
                <h:messages id="messageList" />

<f:verbatim>
                <h4>Options</h4>
                <table border="1"><tr><td>
</f:verbatim>
                    <h:form id="form1" name="optionsForm">
                        <h:outputText value="#{example_messages['option_lang']}" />
<f:verbatim>:&nbsp;</f:verbatim>
                        <h:selectOneMenu id="locale" value="#{optionsForm.language}">
                            <f:selectItems id="available" value="#{optionsForm.availableLanguages}" />
                        </h:selectOneMenu>
<f:verbatim><br></f:verbatim>
                        <h:outputText value="#{example_messages['option_layout']}" />
<f:verbatim>:&nbsp;</f:verbatim>
                        <h:selectOneMenu id="layout" value="#{globalOptions.pageLayout}"  >
                            <f:selectItem id="item101" itemLabel="Classic" itemValue="classic" />
                            <f:selectItem id="item102" itemLabel="Navigation right" itemValue="navigationRight" />
                            <f:selectItem id="item103" itemLabel="Upside down" itemValue="upsideDown" />
                        </h:selectOneMenu>
<f:verbatim><br></f:verbatim>
                        <h:commandButton id="apply" value="Apply" action="#{optionsCtrl.changeLocale}"/>
                    </h:form>

<f:verbatim>
                </td></tr></table>
</f:verbatim>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>