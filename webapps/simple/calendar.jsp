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

    <h:panelGroup id="body">

        <h:messages id="messageList" showSummary="true" showDetail="true" />

        <h:outputText  id="cdt" value="#{example_messages['js_form']}"/>

                        <h:form id="calendarForm">
        <x:inputCalendar monthYearRowClass="yearMonthHeader" weekRowClass="weekHeader"
            currentDayCellClass="currentDayCell" value="#{calendar.date}" />
                        </h:form>
        <f:verbatim><br/></f:verbatim>

        <h:outputText value="#{calendar.date}" />

        <f:verbatim><br/><br/><br/></f:verbatim>

        <h:outputText value="#{example_messages['js_popup']}"/>

                        <h:form id="calendarForm2">
        <x:inputCalendar monthYearRowClass="yearMonthHeader" weekRowClass="weekHeader"
            currentDayCellClass="currentDayCell" value="#{calendar.date}" renderAsPopup="true"
            popupTodayString="#{example_messages['popup_today_string']}" popupWeekString="#{example_messages['popup_week_string']}" />
        <h:commandButton value="#{example_messages['js_submit']}"/>
                        </h:form>

        <h:outputText value="#{calendar.date}" />

        <h:form id="calendarForm3">

            <x:dataTable id="data"
                    styleClass="standardTable"
                    headerClass="standardTable_Header"
                    rowClasses="standardTable_Row1,standardTable_Row2"
                    columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                    var="dateHolder"
                    value="#{calendar.dates}"
                    preserveDataModel="true">
                <h:column>
                    <x:inputCalendar monthYearRowClass="yearMonthHeader" weekRowClass="weekHeader"
                        currentDayCellClass="currentDayCell" value="#{dateHolder.date}" renderAsPopup="true"
                        popupTodayString="#{example_messages['popup_today_string']}" popupWeekString="#{example_messages['popup_week_string']}" />
                </h:column>
            </x:dataTable>

            <h:commandButton value="#{example_messages['js_submit']}"/>

        </h:form>
    </h:panelGroup>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
