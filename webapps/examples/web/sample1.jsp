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

    <x:panelLayout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <%@include file="inc/page_header.jsp" %>
        <f:facet name="navigation">
            <f:subview id="menu" >
                <jsp:include page="inc/navigation.jsp" />
            </f:subview>
        </f:facet>


        <f:facet name="body">
            <h:panelGroup id="body">

                <x:saveState id="save1" value="#{calcForm.number1}" />
                <x:saveState id="save2" value="#{calcForm.number2}" />
                <x:saveState id="save3" value="#{ucaseForm.text}" />

                <h:messages id="messageList" styleClass="error"/>

                <f:verbatim>
                    <h4>A Form</h4>
                    <table border="1"><tr><td>
                </f:verbatim>

                <h:form id="form1" name="calcForm">
                    <h:outputLabel for="form1:number1" value="Number 1" />
                    <h:outputText value="#{validationController.number1ValidationLabel}"/>
                    <f:verbatim>: </f:verbatim>
                    <h:inputText id="number1" value="#{calcForm.number1}" maxlength="10" size="25" >
                       <f:validateLongRange minimum="1" maximum="10" />
                    </h:inputText>
                    <h:message id="number1Error" for="form1:number1" styleClass="error" /><f:verbatim><br></f:verbatim>

                    <h:outputLabel for="form1:number2" value="Number 2" />
                    <h:outputText value="#{validationController.number2ValidationLabel}"/>
                    <f:verbatim>: </f:verbatim>
                    <h:inputText id="number2" value="#{calcForm.number2}" maxlength="10" size="25">
                       <f:validateLongRange minimum="20" maximum="50" />
                    </h:inputText>
                    <h:message id="number2Error" for="form1:number2" styleClass="error" /><f:verbatim><br></f:verbatim>

                    <h:outputLabel for="form1:result" value="Result" /><f:verbatim>: </f:verbatim>
                    <h:outputText id="result" value="#{calcForm.result}" /><f:verbatim><br></f:verbatim>

                    <h:commandButton id="addButton" value="Add them" action="none">
                        <f:actionListener type="net.sourceforge.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
                    </h:commandButton>
                    <h:commandButton id="subtractButton" value="Subtract them" action="none">
                        <f:actionListener type="net.sourceforge.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
                    </h:commandButton>
                    <f:verbatim><br></f:verbatim>

                    <h:commandLink id="href1" action="none"><f:verbatim>Add them by clicking this link</f:verbatim>
                        <f:actionListener type="net.sourceforge.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
                    </h:commandLink><f:verbatim><br></f:verbatim>
                    <h:commandLink id="href2" action="none"><f:verbatim>Subtract them by clicking this link</f:verbatim>
                        <f:actionListener type="net.sourceforge.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
                    </h:commandLink>
                </h:form>

                <f:verbatim>
                    </td></tr></table>
                    <h4>Another Form</h4>
                    <table border="1"><tr><td>
                </f:verbatim>

                <h:form id="form2" name="ucaseForm">
                    <h:outputLabel for="form2:text" value="Text" />
                    <h:outputText value="#{validationController.textValidationLabel}"/>
                    <f:verbatim>: </f:verbatim>
                    <h:inputText id="text" value="#{ucaseForm.text}">
                        <f:validateLength minimum="3" maximum="7"/>
                    </h:inputText>
                    <h:message id="textError" for="form2:text" styleClass="error" /><f:verbatim><br></f:verbatim>
                    <h:commandButton id="ucaseButton" value="Make it uppercase" action="none">
                        <f:actionListener type="net.sourceforge.myfaces.examples.example1.UCaseActionListener" />
                    </h:commandButton>
                    <h:commandButton id="lcaseButton" value="Make it lowercase" action="none">
                        <f:actionListener type="net.sourceforge.myfaces.examples.example1.UCaseActionListener" />
                    </h:commandButton>
                    <f:verbatim><br></f:verbatim>
                </h:form>

                <f:verbatim>
                    </td></tr></table>

                    <h4>Validation</h4>
                    <table border="1"><tr><td>
                </f:verbatim>

                <h:form id="form3" name="valForm">
                    <h:commandButton id="valDisable" value="Disable validation" action="#{validationController.disableValidation}" />
                    <h:commandButton id="valEnable" value="Enable validation" action="#{validationController.enableValidation}" />
                </h:form>

                <f:verbatim>
                    </td></tr></table>
                </f:verbatim>

        <f:verbatim><br></f:verbatim>
        <h:commandLink id="jump_home" action="#{ucaseForm.jumpHome}" ><f:verbatim>Go Home</f:verbatim></h:commandLink>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>