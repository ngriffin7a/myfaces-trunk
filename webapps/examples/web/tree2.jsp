<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<!--
/*
 * Copyright 2005 The Apache Software Foundation.
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

              <x:tree2 value="#{treeBacker.treeData}" var="node" varNodeToggler="t">
                <f:facet name="person">
                  <h:panelGroup>
                    <h:commandLink immediate="true" action="#{t.toggleExpanded}">
                      <h:graphicImage value="/images/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                      <h:graphicImage value="/images/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                    </h:commandLink>
                    <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                  </h:panelGroup>
                </f:facet>      
                <f:facet name="foo-folder">
                  <h:panelGroup>
                    <h:commandLink immediate="true" action="#{t.toggleExpanded}">
                      <h:graphicImage value="/images/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                      <h:graphicImage value="/images/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                    </h:commandLink>
                    <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                    <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                  </h:panelGroup>
                </f:facet>
                <f:facet name="bar-folder">
                  <h:panelGroup>
                    <h:commandLink immediate="true" action="#{t.toggleExpanded}">
                      <h:graphicImage value="/images/blue-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                      <h:graphicImage value="/images/blue-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                    </h:commandLink>
                    <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                    <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                  </h:panelGroup>
                </f:facet>        
                <f:facet name="document">
                  <h:panelGroup>
                    <h:commandLink immediate="true" styleClass="document">
                      <h:graphicImage value="/images/document.png" border="0"/>
                      <h:outputText value="#{node.description}"/>
                      <f:param name="userId" value="#{node.identifier}"/>
                    </h:commandLink>
                  </h:panelGroup>
                </f:facet>
              </x:tree2>
      
                <f:verbatim><br></f:verbatim>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>

