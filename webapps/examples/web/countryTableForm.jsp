<%@ page session="false" contentType="text/html;charset=utf-8"%>
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

<!--
managed beans used:
    countryList
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

               <h:messages errorClass="error" showSummary="true" showDetail="true" />

                <h:form id="form" style="display:inline" >
                <x:dataTable id="data"
                        styleClass="standardTable"
                        headerClass="standardTable_Header"
                        rowClasses="standardTable_Row1,standardTable_Row2"
                        columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                        var="country"
                        value="#{countryList.countries}"
                        preserveDataModel="true">
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_name']}" />
                       </f:facet>
                       <h:inputText id="cname" value="#{country.name}" required="true" />
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_iso']}" />
                       </f:facet>
                       <h:inputText id="ciso" value="#{country.isoCode}" required="true" >
                                <f:validateLength maximum="2" minimum="2"/>
                       </h:inputText>

                   </h:column>
                   

                   <h:column>
                        <h:selectBooleanCheckbox value="#{country.remove}"/>
                   </h:column>

                   <f:facet name="footer">
                        <h:panelGroup>
                            <h:commandButton action="go_back" value="#{example_messages['button_save']}" />
                            <f:verbatim>&nbsp;</f:verbatim>
                            <h:commandButton action="go_back" immediate="true" value="#{example_messages['button_cancel']}" />
                            <f:verbatim>&nbsp;</f:verbatim>
                            <h:commandButton value="#{example_messages['button_apply']}" />
                        </h:panelGroup>
                   </f:facet>

                </x:dataTable>
                </h:form>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>