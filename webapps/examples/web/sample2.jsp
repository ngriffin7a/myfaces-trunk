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

    <x:panelLayout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <%@include file="inc/page_header.jsp" %>
        <f:facet name="navigation">
            <f:subview id="menu" >
                <jsp:include page="inc/navigation.jsp" />
            </f:subview>
        </f:facet>

        <f:facet name="body">
            <h:panelGroup id="body">

                <h:messages id="messageList" />

                <h:form id="q_form" name="q_form">
                    <h:inputTextarea id="text"
                                      rows="5"
                                      value="#{q_form.text}" required="true" />
                    <f:verbatim><br><br></f:verbatim>
                    <h:selectOneMenu id="oneoption" value="#{q_form.quoteChar}" >
                        <f:selectItem itemValue="" itemLabel="select a quote character" />
                        <f:selectItem itemValue="\"" itemLabel="Double" />
                        <f:selectItem itemValue="'" itemLabel="Single" />
                        <f:selectItems value="#{q_form.selectOneItems}" />
                    </h:selectOneMenu>
                    <h:commandButton id="button1" value="Add quotes" action="none">
                        <f:actionListener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:actionListener>
                    </h:commandButton>

                    <f:verbatim><br><br></f:verbatim>
                    <h:selectManyListbox id="manyoptions" value="#{q_form.selectManyValues}" >
                        <f:selectItem itemValue="" itemLabel="select the unquote characters" />
                        <f:selectItems value="#{q_form.selectManyItems}" />
                    </h:selectManyListbox>
                    <h:commandButton id="button2" value="Remove quotes" action="none"><f:verbatim><br></f:verbatim>
                        <f:actionListener type="net.sourceforge.myfaces.examples.example2.QuotationController" ></f:actionListener>
                    </h:commandButton>

                </h:form>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>