<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<%@ page contentType="text/vnd.wap.wml" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/wap" prefix="wap" %>

<wml>
<card id="card1" title="panelGrid example">
<p>
<f:view>
    <wap:form> 
        <wap:panelGrid title="title" columns="2">
            <f:facet name="header">
                <wap:panelGroup>
                    <wap:outputText value="column1" />
                    <wap:outputText value="column2" />
                </wap:panelGroup>
            </f:facet>
            
            <wap:panelGroup>
                <!-- grid nested in one panel cell -->
                <wap:panelGrid columns="3">
                    <wap:outputText value="Grid"  />
                    <wap:outputText value=" in "  />
                    <wap:outputText value="grid"  />
                </wap:panelGrid>           
            </wap:panelGroup>

            <!-- elements nested in panelGroup will be rendered to one cell -->
            <wap:panelGroup>
                <wap:outputText value="1" />
                <wap:outputText value="2" />
                <wap:outputText value="3" />
            </wap:panelGroup>
            <wap:outputText value="column1" />
            <wap:outputText value="column2" />
            <wap:outputText value="column1" />
            <wap:outputText value="column2" />
            <f:facet name="footer">
                <wap:panelGroup>
                    <wap:outputText value="footer1" />
                    <wap:outputText value="footer2" />
                </wap:panelGroup>
            </f:facet>
        </wap:panelGrid>      

        <wap:commandButton action="backToIndex" type="prev" value="examples" />     
    </wap:form>      
</f:view>
</p>
</card>
</wml>

