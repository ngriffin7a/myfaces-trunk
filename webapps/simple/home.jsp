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
        <h:outputLink value="buffer.jsf" ><f:verbatim>Buffer</f:verbatim></h:outputLink> <br/>
        Data Table<br/>
        &nbsp;&nbsp;&nbsp;<h:outputLink value="masterDetail.jsf" ><f:verbatim>Master-Detail</f:verbatim></h:outputLink><br/>
        &nbsp;&nbsp;&nbsp;<h:outputLink value="dataScroller.jsf" ><f:verbatim>Data Scroller</f:verbatim></h:outputLink><br/>            
        &nbsp;&nbsp;&nbsp;<h:outputLink value="sortTable.jsf" ><f:verbatim>Sortable</f:verbatim></h:outputLink><br/>            
        &nbsp;&nbsp;&nbsp;<h:outputLink value="pagedSortTable.jsf" ><f:verbatim>Paged and Sortable</f:verbatim></h:outputLink><br/>            
        &nbsp;&nbsp;&nbsp;<h:outputLink value="openDataTable.jsf" ><f:verbatim>Paged and Sortable (dynamic number of columns)</f:verbatim></h:outputLink><br/>
        <h:outputLink value="selectbox.jsf" ><f:verbatim>Select boxes</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="fileupload.jsf" ><f:verbatim>File upload</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="tabbedPane.jsf" ><f:verbatim>Tabbed Pane</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="calendar.jsf" ><f:verbatim>Calendar</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="popup.jsf" ><f:verbatim>Popup</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="jscookmenu.jsf" ><f:verbatim>JSCookMenu</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="jslistener.jsf" ><f:verbatim>Javascript Listener</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="date.jsf" ><f:verbatim>Date</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="inputHtml.jsf" ><f:verbatim>Html Editor</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="dataList.jsf" ><f:verbatim>Dynamic Lists</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="tree.jsf" ><f:verbatim>Tree</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="treeTable.jsf" ><f:verbatim>Tree Table</f:verbatim></h:outputLink> <br/>
        Tree2<br/>
        &nbsp;&nbsp;&nbsp;<h:outputLink value="tree2.jsf" ><f:verbatim>Tree2 (client-side toggle, server-side toggle)</f:verbatim></h:outputLink><br/>
        &nbsp;&nbsp;&nbsp;<h:outputLink value="tree2HideRoot.jsf" ><f:verbatim>Tree2 (hide root node)</f:verbatim></h:outputLink><br/>
        &nbsp;&nbsp;&nbsp;<h:outputLink value="tree2NiceWrap.jsf" ><f:verbatim>Tree2 (nice wrap)</f:verbatim></h:outputLink><br/>
        <h:outputLink value="rssTicker.jsf" ><f:verbatim>RSS Ticker</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="panelstack.jsf" ><f:verbatim>Panel Stack</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="css.jsf" ><f:verbatim>Style Sheet</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="newspaperTable.jsf" ><f:verbatim>Newspaper Table</f:verbatim></h:outputLink> <br/>
        <h:outputLink value="forceId.jsf" ><f:verbatim>forceId</f:verbatim></h:outputLink> <br/>

    </f:view>

</html>
