<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>
<html>

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

<%@include file="inc/head.inc" %>

<body>

<!--
managed beans used:
    countryList
-->

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <x:dataTable id="data"
                 styleClass="scrollerTable"
                 headerClass="standardTable_Header"
                 footerClass="standardTable_Header"
                 rowClasses="standardTable_Row1,standardTable_Row2"
                 columnClasses="standardTable_Column,standardTable_ColumnCentered, standardTable_Column"
                 var="row"
                 value="#{openDataList.data}"
                 preserveDataModel="true"
                 rows="10"
                 sortColumn="#{openDataList.sort}"
                 sortAscending="#{openDataList.ascending}"
                 preserveSort="true">
        <x:columns value="#{openDataList.columnHeaders}" var="columnHeader">
            <f:facet name="header">
                <x:commandSortHeader columnName="#{columnHeader}" arrow="true">
                    <h:outputText value="#{columnHeader}" />
                </x:commandSortHeader>
            </f:facet>
            <!-- row is also available -->
            <h:outputText value="#{openDataList.columnValue}" />
        </x:columns>
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
                        paginatorActiveColumnStyle="font-weight:bold;">
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

    </h:panelGrid>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
