<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%><%@ taglib uri="/WEB-INF/myfaces_basic.tld" prefix="f"
%><%@ taglib uri="/WEB-INF/myfaces_ext.tld" prefix="x"
%><html>

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

<jsp:useBean id="calcForm" class="net.sourceforge.myfaces.example.model.CalcForm" scope="request" />
<jsp:useBean id="calcCtrl" class="net.sourceforge.myfaces.example.controller.CalcController" scope="request" />

<jsp:useBean id="ucaseForm" class="net.sourceforge.myfaces.example.model.UCaseForm" scope="request" />
<jsp:useBean id="ucaseCtrl" class="net.sourceforge.myfaces.example.controller.UCaseController" scope="request" />

<f:use_faces>

    <table border="1"><tr>
        <td valign="top" width="140"><%@ include file="inc/navigation.jsp"  %></td>
        <td align="left" width="640">

            <x:save_state id="save1" modelReference="calcForm.number1" />
            <x:save_state id="save2" modelReference="calcForm.number2" />
            <x:save_state id="save3" modelReference="calcForm.result" />

            <x:message_list id="messageList" />

            <%
                Date test = new Date();
            %>
            You entered this page on <f:output_text id="test" text="<%=test.toString()%>" /><br>


            <h4>A Form</h4>
            <table border="1"><tr><td>
                <f:form id="form1" formName="calcForm">
                    Number 1: <f:textentry_input id="number1" modelReference="calcForm.number1" maxLength="10" size="25"/><br>
                    Number 2: <f:textentry_input id="number2" modelReference="calcForm.number2" maxLength="10" size="25"/><br>
                    Result: <f:output_text id="result" modelReference="calcForm.result" /><br>
                    <f:command_button id="addButton" commandName="add" commandReference="calcCtrl.calc" label="Add them"/>
                    <f:command_button id="subtractButton" commandName="subtract" commandReference="calcCtrl.calc" label="Subtract them"/><br>
                </f:form>

                <f:command_hyperlink id="href1" commandName="add" commandReference="calcCtrl.calc">Add them by use of a link</f:command_hyperlink><br>
                <f:command_hyperlink id="href2" commandName="subtract" commandReference="calcCtrl.calc">Subtract them by use of a link</f:command_hyperlink>
            </td></tr></table>

            <br>
            <br>

            <h4>Another Form</h4>
            <table border="1"><tr><td>
                <f:form id="form2" formName="ucaseForm">
                    <f:textentry_input id="text" modelReference="ucaseForm.text" /><br>
                    <f:command_button id="ucaseButton" commandName="up" commandReference="ucaseCtrl.calc" label="Make it uppercase" />
                    <f:command_button id="lcaseButton" commandName="low" commandReference="ucaseCtrl.calc" label="Make it lowercase" /><br>
                </f:form>
            </td></tr></table>

    <br><f:command_hyperlink id="jump_home" href="index.jsf" >Go Home</f:command_hyperlink>

        </td>
    </tr></table>

</f:use_faces>

</body>

</html>