<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x" %>
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

<!--
managed beans used:
    q_form
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

                <h:messages id="messageList" />

                <h:form id="q_form" name="q_form">
                    <h:inputTextarea id="text"
                                      rows="5"
                                      value="#{q_form.text}" required="true" />
                    <f:verbatim><br><br></f:verbatim>
                    <%-- Test HtmlInputText start--%>
                    <x:inputText id="in" displayValueOnly="true" value="test" />
                    <f:verbatim><br><br></f:verbatim>
                    <x:inputText id="in1" displayValueOnly="false" value="test" />
                    <f:verbatim><br><br></f:verbatim>
                    <%-- Test HtmlInputText end--%>
                    <%-- Test HtmlSelectOneRadio start--%>
                    <x:selectOneRadio id="r1" value="#{carconf.discount}" layout="pageDirection"  displayValueOnly="true" >
                                <f:selectItem itemValue="1" itemLabel="#{example_messages['discount_0']}" />
                                <f:selectItem itemValue="2" itemLabel="#{example_messages['discount_1']}"  />
                                <f:selectItem itemValue="3" itemLabel="#{example_messages['discount_2']}"  />
                    </x:selectOneRadio>
                    <f:verbatim><br><br></f:verbatim>
                    <x:selectOneRadio id="r2" value="#{carconf.discount}" layout="pageDirection"  displayValueOnly="false" >
                                <f:selectItem itemValue="1" itemLabel="#{example_messages['discount_0']}" />
                                <f:selectItem itemValue="2" itemLabel="#{example_messages['discount_1']}"  />
                                <f:selectItem itemValue="3" itemLabel="#{example_messages['discount_2']}"  />
                    </x:selectOneRadio>
                    <f:verbatim><br><br></f:verbatim>
                    <%-- Test HtmlSelectOneRadio end--%>
                    <%-- Test HtmlInputTextarea start--%>
                    <x:inputTextarea id="text1"
                                      rows="5"
                                      value="#{q_form.text}" required="true"
                                      displayValueOnly="true" />
                    <f:verbatim><br><br></f:verbatim>
                    <x:inputTextarea id="text2"
                                      rows="5"
                                      value="#{q_form.text}" required="true"
                                      displayValueOnly="false" />
                    <f:verbatim><br><br></f:verbatim>
                    <%--Test HtmlInputTextarea end--%>
                    <x:selectOneMenu id="oneoption" value="#{q_form.quoteChar}">
                        <f:selectItem itemValue="" itemLabel="#{example_messages['sample2_select_quote']}" />
                        <f:selectItem itemValue="\"" itemLabel="Double" />
                        <f:selectItem itemValue="'" itemLabel="Single" />
                        <f:selectItems value="#{q_form.selectOneItems}" />
                    </x:selectOneMenu>
                    <h:commandButton id="button1" value="#{example_messages['sample2_add_quote']}" action="none">
                        <f:actionListener type="org.apache.myfaces.examples.example2.QuotationController" ></f:actionListener>
                    </h:commandButton>

                    <%-- Test HtmlSelectManyCheckbox start--%>
                    <f:verbatim><br><br></f:verbatim>
                    <x:selectManyCheckbox id="chkmany" value="#{q_form.selectManyValues}" layout="pageDirection" displayValueOnly="true" >
                        <f:selectItem itemValue="" itemLabel="#{example_messages['sample2_select_unquote']}" />
                        <f:selectItems value="#{q_form.selectManyItems}" />
                    </x:selectManyCheckbox>
                    <%-- Test HtmlSelectManyCheckbox end--%>
                    <f:verbatim><br><br></f:verbatim>
                    <x:selectManyListbox id="manyoptions" value="#{q_form.selectManyValues}" displayValueOnly="true" >
                        <f:selectItem itemValue="" itemLabel="#{example_messages['sample2_select_unquote']}" />
                        <f:selectItems value="#{q_form.selectManyItems}" />
                    </x:selectManyListbox>
                    <x:selectManyListbox id="manyoptions2" value="#{q_form.selectManyValues}" displayValueOnly="false" >
                        <f:selectItem itemValue="" itemLabel="#{example_messages['sample2_select_unquote']}" />
                        <f:selectItems value="#{q_form.selectManyItems}" />
                    </x:selectManyListbox>
                    <h:commandButton id="button2" value="#{example_messages['sample2_remove_quote']}" action="none"><f:verbatim><br></f:verbatim>
                        <f:actionListener type="org.apache.myfaces.examples.example2.QuotationController" ></f:actionListener>
                    </h:commandButton>
                </h:form>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>