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

<!--
managed beans used:
    carconf
-->

<f:view>


                <h:form name="formName">

                    <h:panelGrid columns="1">

                        <h:panelGroup >
                            <h:selectOneRadio id="r1" value="#{testAdditional.numberPrimitiveLong}" layout="pageDirection"  styleClass="selectOneRadio">
                                <f:selectItem itemValue="1" itemLabel="1" />
                                <f:selectItem itemValue="2" itemLabel="2"  />
                                <f:selectItem itemValue="5" itemLabel="5"  />
                            </h:selectOneRadio>
                        </h:panelGroup>

                        <h:panelGroup >
                            <h:selectOneRadio id="r2" value="#{testAdditional.numberPrimitiveInt}" layout="pageDirection"  styleClass="selectOneRadio">
                                <f:selectItem itemValue="1" itemLabel="1" />
                                <f:selectItem itemValue="15" itemLabel="15"  />
                                <f:selectItem itemValue="25" itemLabel="25"  />
                            </h:selectOneRadio>
                        </h:panelGroup>

                        <h:commandButton action="#{carconf.calcPrice}" value="#{example_messages['button_calcprice']}" />

                    </h:panelGrid>
                </h:form>

</f:view>

</body>

</html>