<%@ page import="net.sourceforge.myfaces.custom.tree.DefaultMutableTreeNode,
                 net.sourceforge.myfaces.custom.tree.model.DefaultTreeModel"%>
<%@ page session="true"
%><%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"
%><html>

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

<%@include file="inc/head.inc" %>

<body>

<%
   if (pageContext.getAttribute("treeModel", PageContext.SESSION_SCOPE) == null) {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("XY");
      DefaultMutableTreeNode a = new DefaultMutableTreeNode("A");
      root.insert(a);
      DefaultMutableTreeNode b = new DefaultMutableTreeNode("B");
      root.insert(b);
      DefaultMutableTreeNode c = new DefaultMutableTreeNode("C");
      root.insert(c);

      DefaultMutableTreeNode node = new DefaultMutableTreeNode("a1");
      a.insert(node);
      node = new DefaultMutableTreeNode("a2 ");
      a.insert(node);
      node = new DefaultMutableTreeNode("b ");
      b.insert(node);

      a = node;
      node = new DefaultMutableTreeNode("x1");
      a.insert(node);
      node = new DefaultMutableTreeNode("x2");
      a.insert(node);

      pageContext.setAttribute("treeModel", new DefaultTreeModel(root), PageContext.SESSION_SCOPE);
   }
%>

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

                <x:tree id="tree" value="#{treeModel}"
                        styleClass="tree"
                        nodeClass="treenode"
                        selectedNodeClass="treenodeSelected">
                </x:tree>
                <f:verbatim><br></f:verbatim>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </x:panelLayout>

</f:view>

</body>

</html>