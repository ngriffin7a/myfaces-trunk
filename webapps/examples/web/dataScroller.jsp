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

                <x:dataTable id="data"
                        styleClass="scrollerTable"
                        headerClass="standardTable_Header"
                        footerClass="standardTable_Header"
                        rowClasses="standardTable_Row1,standardTable_Row2"
                        columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                        var="car"
                        value="#{scrollerList.list}"
                        preserveDataModel="true"
                        rows="10"
                   >
                   <h:column>
                       <f:facet name="header">
                       </f:facet>
                       <h:outputText value="#{car.id}" />
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_cars']}" />
                       </f:facet>
                       <h:outputText value="#{car.type}" />
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_color']}" />
                       </f:facet>
                       <h:outputText value="#{car.color}" />
                   </h:column>

                </x:dataTable>

                <h:panelGrid columns="1" styleClass="scrollerTable2" columnClasses="standardTable_ColumnCentered" >
                    <x:dataScroller id="scroll_1"
                            for="data"
                            fastStep="10"
                            pageCountVar="pageCount"
                            pageIndexVar="pageIndex"
                            styleClass="scroller"
                            paginator="true"
                            paginatorMaxPages="9"
                            paginatorTableClass="paginator"
                            paginatorActiveColumnStyle="font-weight:bold;"
                            >
                        <f:facet name="first" >
                            <h:graphicImage url="images/arrow-first.gif" border="1" />
                        </f:facet>
                        <f:facet name="last">
                            <h:graphicImage url="images/arrow-last.gif" border="1" />
                        </f:facet>
                        <f:facet name="previous">
                            <h:graphicImage url="images/arrow-previous.gif" border="1" />
                        </f:facet>
                        <f:facet name="next">
                            <h:graphicImage url="images/arrow-next.gif" border="1" />
                        </f:facet>
                        <f:facet name="fastforward">
                            <h:graphicImage url="images/arrow-ff.gif" border="1" />
                        </f:facet>
                        <f:facet name="fastrewind">
                            <h:graphicImage url="images/arrow-fr.gif" border="1" />
                        </f:facet>
                    </x:dataScroller>
                    <x:dataScroller id="scroll_2"
                            for="data"
                            pageCountVar="pageCount"
                            pageIndexVar="pageIndex"
                            >
                        <h:outputFormat value="#{example_messages['dataScroller_pages']}" styleClass="standard" >
                            <f:param value="#{pageIndex}" />
                            <f:param value="#{pageCount}" />
                        </h:outputFormat>
                    </x:dataScroller>
                </h:panelGrid>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>