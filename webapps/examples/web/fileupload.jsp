<%@ page import="java.util.Random"%>
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

<f:view>

    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="example_messages"/>

    <x:saveState value="#{fileUploadForm.name}"/>

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

                <h:messages id="messageList" showSummary="true" showDetail="true" />

                <f:verbatim>
                    <h4>File upload</h4>
                    <table border="1"><tr><td>
                </f:verbatim>

                    <h:form id="form1" name="form1" enctype="multipart/form-data" >
                        <h:outputText value="Gimme an image: "/>
                        <x:inputFileUpload id="fileupload"
                                           accept="image/*"
                                           value="#{fileUploadForm.upFile}"
                                           styleClass="fileUploadInput" />
                        <f:verbatim><br></f:verbatim>
                        <h:outputText value="and give it a name: "/>
                        <h:inputText value="#{fileUploadForm.name}"/>
                        <h:commandButton value="load it up" action="#{fileUploadForm.upload}" />
                    </h:form>

                <f:verbatim>
                    </td></tr></table>
                <%
                if (application.getAttribute("fileupload_file") != null)
                {
                    %>
                    <p>The image you loaded up:</p>
                    <img src="fileupload_showimg.jsp?dummy=<%=Math.random()%>">
                    <br><%
                }
                %>
                </f:verbatim>
                <h:outputText value="#{fileUploadForm.name}"/>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>