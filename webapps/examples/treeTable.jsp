<%@ page session="true" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>
<html>

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

<%@include file="inc/head.inc" %>

<body>
<head>
<link rel="stylesheet" type="text/css" href="css/basic.css">
</head>

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

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

    <h:form>
        <x:tree id="tree" value="#{treeTable.treeModel}"
        		var="treeItem"
        		styleClass="tree"
        	    nodeClass="treenode"
        	    headerClass="treeHeader"
        	    footerClass="treeFooter"
    	        rowClasses="a, b"
    	        columnClasses="col1, col2"
	            selectedNodeClass="treenodeSelected"
	            expandRoot="true">
	        <h:column>
	        	<f:facet name="header">
                	<h:outputText value="Header 1" />
                </f:facet>
               	<h:outputText value="#{treeItem.isoCode}" />
            </h:column>
	        <x:treeColumn>
	        	<f:facet name="header">
                	<h:outputText value="Header 2" />
                </f:facet>
	        	<h:outputText value="#{treeItem.name}" />
	        </x:treeColumn>
	        <h:column>
	        	<f:facet name="header">
                	<h:outputText value="Header 3" />
                </f:facet>
        		<h:outputText value="#{treeItem.description}" />
            </h:column>
            <f:facet name="footer">
            	<h:outputText value="Footer" />
            </f:facet>
     	</x:tree>
		<f:verbatim><br></f:verbatim>

   </h:form>

   </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>