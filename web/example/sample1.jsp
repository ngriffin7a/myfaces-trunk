<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%><%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="h"
%><%@ taglib uri="/WEB-INF/myfaces_core.tld" prefix="f"
%><%@ taglib uri="/WEB-INF/myfaces_ext.tld" prefix="x"
%><html>

<%@include file="inc/head.inc" %>

<!--
/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
//-->

<body>

<jsp:useBean id="calcForm" class="net.sourceforge.myfaces.examples.diverse.model.CalcForm" scope="request" />
<jsp:useBean id="calcCtrl" class="net.sourceforge.myfaces.examples.diverse.controller.CalcController" scope="application" />

<jsp:useBean id="ucaseForm" class="net.sourceforge.myfaces.examples.diverse.model.UCaseForm" scope="request" />
<jsp:useBean id="ucaseCtrl" class="net.sourceforge.myfaces.examples.diverse.controller.UCaseController" scope="application" />

<h:use_faces>

    <x:page_layout id="page" layoutReference="pageLayout" cssClass="pageLayout" >
        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <x:page_body id="body" cssClass="pageBody" >

            <x:save_state id="save1" modelReference="calcForm.number1" />
            <x:save_state id="save2" modelReference="calcForm.number2" />
            <x:save_state id="save3" modelReference="calcForm.result" />
            <x:save_state id="save4" modelReference="ucaseForm.text" />

            <%
                Date test = new Date();
            %>
            You entered this page on <h:output_text id="test" text="<%=test.toString()%>" /><br>

            <h:errors id="messageList" />

            <h4>A Form</h4>
            <table border="1"><tr><td>
                <h:form id="form1" formName="calcForm">
                    Number 1: <h:textentry_input id="number1" modelReference="calcForm.number1" maxlength="10" size="25" /><h:input_errors id="number1Error" compoundId="/form1/number1" cssClass="error" /><br>
                    Number 2: <h:textentry_input id="number2" modelReference="calcForm.number2" maxlength="10" size="25"/><h:input_errors id="number2Error" compoundId="/form1/number2" cssClass="error" /><br>
                    Result: <h:output_text id="result" modelReference="calcForm.result" /><br>
                    <h:command_button id="addButton" commandName="add" commandReference="calcCtrl.calc" label="Add them">
                        <f:action_listener type="net.sourceforge.myfaces.examples.controller.ActionListenerTest" ></f:action_listener>
                    </h:command_button>
                    <h:command_button id="subtractButton" commandName="subtract" commandReference="calcCtrl.calc" label="Subtract them"/><br>
                </h:form>

                <h:command_hyperlink id="href1" commandName="add" commandReference="calcCtrl.calc">Add them by clicking this link</h:command_hyperlink><br>
                <h:command_hyperlink id="href2" commandName="subtract" commandReference="calcCtrl.calc">Subtract them by clicking this link</h:command_hyperlink>
            </td></tr></table>

            <br>
            <br>

            <h4>Another Form</h4>
            <table border="1"><tr><td>
                <h:form id="form2" formName="ucaseForm">
                    <h:textentry_input id="text" modelReference="ucaseForm.text" /><br>
                    <h:command_button id="ucaseButton" commandName="up" commandReference="ucaseCtrl.calc" label="Make it uppercase" />
                    <h:command_button id="lcaseButton" commandName="low" commandReference="ucaseCtrl.calc" label="Make it lowercase" /><br>
                </h:form>
            </td></tr></table>

    <br><h:command_hyperlink id="jump_home" href="home.jsf" >Go Home</h:command_hyperlink>

        </x:page_body>

        <%@include file="inc/page_footer.jsp" %>
    </x:page_layout>

</h:use_faces>

</body>

</html>