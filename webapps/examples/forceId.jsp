<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>
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

<!--
managed beans used:
    validateForm
-->

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

			<h:form id="forceIdForm" >
			   <h:panelGrid columns="3">

                    <h:outputText id="forceOneOutput" value="#{example_messages['forceOne']}"/>
                    <h:inputText required="true" id="forceOne" value="#{forceIdBean.valueOne}"/>
                    <h:message id="forceOneMessage" for="forceOne" styleClass="error" />

                    <h:outputText id="forceTwoOutput" value="#{example_messages['forceTwo']}"/>
                    <x:inputText required="true" id="forceTwo" value="#{forceIdBean.valueTwo}" forceId="true"/>
                    <h:message id="forceTwoMessage" for="forceTwo" styleClass="error" />

				<h:panelGroup/>
			  	<x:commandLink forceId="true" id="button" value="#{example_messages['button_submit']}" action="go_home"/>
                    <h:panelGroup/>

			    </h:panelGrid>
			</h:form>
			
			<h:form id="dataTable">
			<h:dataTable value="#{forceIdBean.users}" var="user">
		    <h:column>
			<h:outputText value="Username"/>
      		<x:inputText id="username" value="#{user.username}" forceId="true"/>
			<h:outputText value="Password"/>
      		<x:inputText id="passwd" value="#{user.password}" forceId="true"/>
			<x:commandButton id="button" forceId="true" value="Update" action="#{user.update}"/>
   			</h:column>
			</h:dataTable>
			</h:form>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>