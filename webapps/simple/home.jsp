<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>

    <%@include file="inc/head.inc" %>

    <f:view>

        <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

        <h:panelGrid id="header_group1" columns="2" styleClass="pageHeader"  >
            <h:graphicImage id="header_logo" url="images/logo_mini.jpg" alt="#{example_messages['alt_logo']}" />
            <f:verbatim>
                &nbsp;&nbsp;
                <font size="+1" color="#FFFFFF">MyFaces - The free JavaServer&#8482; Faces Implementation</font>
                <font size="-1" color="#FFFFFF">(Version 1.0.9 beta)</font>
            </f:verbatim>
        </h:panelGrid>

        <br/>

        <h:outputLink value="sample1.jsf" ><f:verbatim>Sample 1</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="sample2.jsf" ><f:verbatim>Sample 2</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="validate.jsf" ><f:verbatim>Validations</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="aliasBean.jsf" ><f:verbatim>Alias Bean</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="dataTable.jsf" ><f:verbatim>Master/Detail Example</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="sortTable.jsf" ><f:verbatim>Sortable Table</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="selectbox.jsf" ><f:verbatim>Select boxes</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="fileupload.jsf" ><f:verbatim>File upload</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="tabbedPane.jsf" ><f:verbatim>Tabbed Pane</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="calendar.jsf" ><f:verbatim>Calendar</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="popup.jsf" ><f:verbatim>Popup</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="jslistener.jsf" ><f:verbatim>Javascript Listener</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="date.jsf" ><f:verbatim>Date</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="htmlEditor.jsf" ><f:verbatim>Html Editor</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="dataList.jsf" ><f:verbatim>Dynamic Lists</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="tree.jsf" ><f:verbatim>Tree</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="treeTable.jsf" ><f:verbatim>Tree Table</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="tree2.jsf" ><f:verbatim>Tree2</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="rssTicker.jsf" ><f:verbatim>RSS Ticker</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="buffer.jsf" ><f:verbatim>Data Scroller</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="panelstack.jsf" ><f:verbatim>Panel Stack</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="css.jsf" ><f:verbatim>Style Sheet</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="newspaperTable.jsf" ><f:verbatim>Newspaper Table</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="forceId.jsf" ><f:verbatim>forceId</f:verbatim></h:outputLink> <br/>

    </f:view>

</html>
