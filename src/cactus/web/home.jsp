<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
   <title>Invoice</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
</head>
<body>
<f:view>
<h:form id="invoiceForm">
<h:messages/><p>
Invoice Number: <h:inputText value="#{invoice.invoiceNumber}"/><br>
Purchaser: <h:inputText value="#{invoice.purchaser}"/><br>
<p>
<h:dataTable
    rows="0"
    value="#{invoice.lineItems}"
    var="item"
>
    <h:column>
        <f:facet name="header">
            <h:outputText  value="Product"/>
        </f:facet>
        <h:inputText value="#{item.product}"/>
    </h:column>
    <h:column>
        <f:facet name="header">
            <h:outputText  value="Quantity"/>
        </f:facet>
        <h:inputText value="#{item.quantity}"/>
    </h:column>
</h:dataTable>
<h:commandButton id="addButton"
    value="Add Line Item"
    action="#{invoice.addLineItem}"
    >
</h:commandButton>
<h:commandButton id="removeButton"
    value="Remove Line Item"
    action="#{invoice.removeLineItem}"
    >
</h:commandButton>
</h:form>
</f:view>
</body>
</html>

