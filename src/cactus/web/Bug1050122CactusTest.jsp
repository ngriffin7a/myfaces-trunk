<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
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

<body>


<f:view>


                <h:form id="testForm">

                    <h:panelGrid columns="1">

                        <h:inputText id="size"
                                size="4"
                                required="true"
                                value="#{testAdditional.map['size']}">
                            <f:convertNumber integerOnly="true"/>
                            <f:validateLongRange minimum="1" maximum="12"/>
                        </h:inputText>

                        <x:message id="error" for="size" styleClass="error" />

                        <h:commandButton id="submit" action="#{testAdditional.doAction}" value="Ok" />

                    </h:panelGrid>
                </h:form>

                <h:outputText value="#{testAdditional.map['size']}#{testAdditional.map['size'].class}"/>
</f:view>

</body>

</html>