<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"%>
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
    list
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

                <x:dataTable styleClass="standardTable"
                        headerClass="standardTable_SortHeader"
                        footerClass="standardTable_Footer"
                        rowClasses="standardTable_Row1,standardTable_Row2"
                        var="car"
                        value="#{list.cars}"
                        sortColumn="#{list.sort}"
                        sortAscending="#{list.ascending}"
                        preserveDataModel="true"
                        preserveSort="true">

                    <f:facet name="header">
                        <h:outputText value="(header table)"  />
                    </f:facet>
                    <f:facet name="footer">
                        <h:outputText value="(footer table)"  />
                    </f:facet>

                    <h:column>
                        <f:facet name="header">
                            <x:commandSortHeader columnName="type" arrow="true">
                                <h:outputText value="#{example_messages['sort_cartype']}" />
                            </x:commandSortHeader>
                        </f:facet>
                        <h:outputText value="#{car.type}" />
                        <f:facet name="footer">
                            <h:outputText id="ftr1" value="(footer col1)"  />
                        </f:facet>
                    </h:column>

                    <h:column>
                        <f:facet name="header">
                            <x:commandSortHeader columnName="color" arrow="true">
                                <h:outputText value="#{example_messages['sort_carcolor']}" />
                            </x:commandSortHeader>
                        </f:facet>
                        <h:outputText value="#{car.color}" />
                        <f:facet name="footer">
                            <h:outputText id="ftr2" value="(footer col2)"  />
                        </f:facet>
                    </h:column>

                </x:dataTable>

            </h:panelGroup>
        </f:facet>

            <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>