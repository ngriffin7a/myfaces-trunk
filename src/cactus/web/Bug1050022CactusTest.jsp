<%@ page contentType="text/html; charset=Shift_JIS" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
<HTML>
  <HEAD>
    <meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS">
    <link REL ="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style">
    <TITLE>Hello</TITLE>
  </HEAD>
  <BODY BGCOLOR="white">
<BR>
<BR>
    <h:dataTable width="100%" value="#{ArticleThreadTable.articleThreadTable}"
                 var="thread" border="0">
      <h:column>
        <h:form id="replyForm">
          <h:outputText value="#{thread.title}"/><f:verbatim>:</f:verbatim>
            <h:inputText id="replyTitle" required="true" size="80" value="#{thread.replyTitle}"/>
            <h:message styleClass="ErrorMessage" showSummary="true" for="replyTitle"/>
            <h:commandButton action="#{thread.reply}"/>
          <f:verbatim><BR></f:verbatim>
        </h:form>
      </h:column>
    </h:dataTable>
    Selected:<h:outputText value="#{ArticleThreadTable.result}"/>
  </BODY>
</HTML>  
</f:view>
