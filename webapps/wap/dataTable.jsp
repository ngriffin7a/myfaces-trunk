<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="dataTable example">
<p>
<f:view>
    <wap:form> 
        <wap:dataTable title="Number of users:" value="#{statistic.records}" var="record" >
            <wap:column>
                <f:facet name="header">
                    <wap:outputText value="day:" />
                </f:facet>

                <b><wap:outputText value="#{record.date}" >
                    <f:convertDateTime dateStyle="short" />
                </wap:outputText></b>
            </wap:column>
            <wap:column>
                <f:facet name="header">
                    <wap:outputText value="no of visitors:" />
                </f:facet>

                <wap:outputText value="#{record.visitors}" />

                <f:facet name="footer">
                    <wap:outputText value="#{statistic.summary}" />
                </f:facet>
            </wap:column>
        </wap:dataTable>  
         
        <wap:commandButton action="backToIndex" type="prev" value="examples" />
    </wap:form>      
</f:view>
</p>
</card>
</wml>

