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

                <h:panelGrid columns="2">
                    <h:outputLabel for="text1" value="Text:"/>
                    <h:inputText id="text1">
                        <x:jsValueChangeListener for="text2" property="value" expressionValue="($srcElem.value=='yes')?'true':'false'" />
                        <x:jsValueChangeListener for="text3" property="value" expressionValue="$srcElem.value" />
                        <x:jsValueChangeListener for="text4" expressionValue="$destElem.innerHTML = $srcElem.value" />
                        <x:jsValueChangeListener for="text5" expressionValue="($srcElem.value=='hide')?$destElem.style.display='none':$destElem.style.display='inline'" />
                        <x:jsValueSet name="countryMap" value="#{countryList.countryMap}"/>
                        <x:jsValueChangeListener for="text6" expressionValue="$destElem.innerHTML = countryMap[$srcElem.value]" />
                    </h:inputText>
                    <h:inputText id="text2"/>
                    <h:inputText id="text3"/>
                    <h:panelGroup id="text4" />
                    <h:panelGroup id="text5" >
                        <h:outputText value="Hide me - enter hide in first input-field."/>
                    </h:panelGroup>
                    <h:panelGroup id="text6" >
                        <h:outputText value="Countryname - enter ISO Code in first input-field (e.g. AT)"/>
                    </h:panelGroup>
                    <h:panelGroup/>
                    <h:selectOneMenu id="selone_menu_colors" value="red" styleClass="selectOneMenu">
                        <f:selectItem itemValue="" itemLabel="#{example_messages['empty_selitem']}" />
                        <f:selectItems value="#{carconf.colors}" />
                        <x:jsValueChangeListener for="selone_menu_subcolors" expressionValue="($srcElem.item(0).options[$srcElem.item(0).options.selectedIndex].value=='color_black')?$destElem.style.display='inline':$destElem.style.display='none';"/>
                    </h:selectOneMenu>
                    <h:inputText id="selone_menu_subcolors"/>
                </h:panelGrid>
            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>
