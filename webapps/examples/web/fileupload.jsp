<%@ page import="java.util.Random"%>
<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"%>
<html>

<%@include file="inc/head.inc" %>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
										   storage="file"
                                           styleClass="fileUploadInput" />
                        <f:verbatim><br></f:verbatim>
                        <h:outputText value="and give it a name: "/>
                        <h:inputText value="#{fileUploadForm.name}"/>
                        <h:commandButton value="load it up" action="#{fileUploadForm.upload}" />
                    </h:form>

                <f:verbatim>
                    </td></tr></table>
                <%
                if (application.getAttribute("fileupload_bytes") != null)
                {
                    %>
                    <p>The image you loaded up:</p>
                    <img src="fileupload_showimg.jsp">
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