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
    calcForm
    ucaseForm
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

                <h:messages id="messageList" showSummary="true" showDetail="true" />

                <h:outputText  id="cdt" value="Calendar as a form."/>

				<h:form id="calendarForm">
                <x:inputCalendar monthYearRowClass="yearMonthHeader" weekRowClass="weekHeader"
                    currentDayCellClass="currentDayCell" value="#{calendar.date}" />
				</h:form>
                <f:verbatim><br/></f:verbatim>

                <h:outputText value="#{calendar.date}" />

                <f:verbatim><br/><br/><br/></f:verbatim>

                <h:outputText value="Calendar as a JavaScript popup."/>

				<h:form id="calendarForm2">
                <x:inputCalendar monthYearRowClass="yearMonthHeader" weekRowClass="weekHeader"
                    currentDayCellClass="currentDayCell" value="#{calendar.date}" renderAsPopup="true"
                    popupTodayString="#{example_messages['popup_today_string']}" popupWeekString="#{example_messages['popup_week_string']}" />
                <h:commandButton value="Submit"/>
				</h:form>

                <h:outputText value="#{calendar.date}" />

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>
