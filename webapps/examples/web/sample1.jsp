<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"
%><html>

<%@include file="inc/head.inc" %>

<!--
/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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

<!--
managed beans used:
    calcForm
    ucaseForm
-->

<f:view>

    <f:loadBundle basename="net.sourceforge.myfaces.examples.resource.example_messages" var="example_messages"/>

    <x:panel_layout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <%@include file="inc/page_header.jsp" %>
        <%@include file="inc/navigation.jsp"  %>

        <f:facet name="body">
            <h:panel_group id="body">

                <x:save_state id="save1" value="#{calcForm.number1}" />
                <x:save_state id="save2" value="#{calcForm.number2}" />
                <x:save_state id="save3" value="#{ucaseForm.text}" />

                <h:messages id="messageList" showSummary="true" showDetail="true" />

                <f:verbatim>
                    <h4>A Form</h4>
                    <table border="1"><tr><td>
                </f:verbatim>

                <h:form id="form1" name="calcForm">
                    <h:output_label for="form1:number1" value="Number 1" /><f:verbatim>:</f:verbatim>
                    <h:input_text id="number1" value="#{calcForm.number1}" maxlength="10" size="25" >
                       <f:validate_longrange minimum="1" maximum="10" />
                    </h:input_text>
                    <h:message id="number1Error" for="form1:number1" styleClass="error" /><f:verbatim><br></f:verbatim>

                    <h:output_label for="form1:number2" value="Number 2" /><f:verbatim>:</f:verbatim>
                    <h:input_text id="number2" value="#{calcForm.number2}" maxlength="10" size="25"/>
                    <h:message id="number2Error" for="form1:number2" styleClass="error" /><f:verbatim><br></f:verbatim>

                    <h:output_label for="form1:result" value="Result" /><f:verbatim>:</f:verbatim>
                    <h:output_text id="result" value="#{calcForm.result}" /><f:verbatim><br></f:verbatim>

                    <h:command_button id="addButton" value="Add them" action="none">
                        <f:action_listener type="net.sourceforge.myfaces.examples.example1.CalcController" ></f:action_listener>
                    </h:command_button>
                    <h:command_button id="subtractButton" value="Subtract them" action="none">
                        <f:action_listener type="net.sourceforge.myfaces.examples.example1.CalcController" ></f:action_listener>
                    </h:command_button>
                    <f:verbatim><br></f:verbatim>
                </h:form>

                <h:command_link id="href1" action="none"><f:verbatim>Add them by clicking this link</f:verbatim>
                    <f:action_listener type="net.sourceforge.myfaces.examples.example1.CalcController" ></f:action_listener>
                </h:command_link><f:verbatim><br></f:verbatim>
                <h:command_link id="href2" action="none"><f:verbatim>Subtract them by clicking this link</f:verbatim>
                    <f:action_listener type="net.sourceforge.myfaces.examples.example1.CalcController" ></f:action_listener>
                </h:command_link>

                <f:verbatim>
                    </td></tr></table>
                    <h4>Another Form</h4>
                    <table border="1"><tr><td>
                </f:verbatim>

                <h:form id="form2" name="ucaseForm">
                    <h:input_text id="text" value="#{ucaseForm.text}"/>
                    <h:message id="textError" for="text" styleClass="error" /><f:verbatim><br></f:verbatim>
                    <h:command_button id="ucaseButton" value="Make it uppercase" action="none">
                        <f:action_listener type="net.sourceforge.myfaces.examples.example1.UCaseController" />
                    </h:command_button>
                    <h:command_button id="lcaseButton" value="Make it lowercase" action="none">
                        <f:action_listener type="net.sourceforge.myfaces.examples.example1.UCaseController" />
                    </h:command_button>
                    <f:verbatim><br></f:verbatim>
                </h:form>

                <f:verbatim>
                    </td></tr></table>

                    <h4>Validation</h4>
                    <table border="1"><tr><td>
                </f:verbatim>

                <h:form id="form3" name="valForm">
                    <h:command_button id="valEnable" value="Enable validation" action="none" >
                        <f:action_listener type="net.sourceforge.myfaces.examples.example1.ValidationController" />
                    </h:command_button>
                    <h:command_button id="valDisable" value="Disable validation" action="none" >
                        <f:action_listener type="net.sourceforge.myfaces.examples.example1.ValidationController" />
                    </h:command_button>
                </h:form>

                <f:verbatim>
                    </td></tr></table>
                </f:verbatim>

        <f:verbatim><br></f:verbatim>
        <h:command_link id="jump_home" action="#{ucaseForm.jumpHome}" ><f:verbatim>Go Home</f:verbatim></h:command_link>

            </h:panel_group>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panel_layout>

</f:view>

</body>

</html>