<%@ page import="java.util.Random"%>
<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_3.tld" prefix="x"
%><html>

<%@include file="inc/head.inc" %>

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

<body>

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

                <h4>File upload</h4>
                <table border="1"><tr><td>
                    <h:form id="form1" formName="form1" enctype="multipart/form-data" >
                        Gimme an image:
                        <x:file_upload id="fileupload"
                                       accept="image/*"
                                       valueRef="fileUploadForm.upFile"
                                       inputClass="fileUploadInput"
                                       size="100" />
                        <h:command_button label="load it up">
                            <f:action_listener type="net.sourceforge.myfaces.examples.misc.FileUploadController" />
                        </h:command_button>
                    </h:form>
                </td></tr></table>

                <%
                if (application.getAttribute("fileupload_file") != null)
                {
                    %>
                    <p>The image you loaded up:</p>
                    <img src="fileupload_showimg.jsp?dummy=<%=Math.random()%>"><%
                }
                %>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</f:use_faces>

</body>

</html>