/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.myfaces.custom.tree.renderkit.html;

import org.apache.myfaces.custom.tree.HtmlTree;
import org.apache.myfaces.custom.tree.TreeNode;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller </a>
 * @author Sean Schofield
 * @author Hans Bergsten (Some code taken from an example in his O'Reilly JavaServer Faces book. Copied with permission)
 * @version $Revision$ $Date$
 */
public class HtmlTreeRenderer extends Renderer
{
    // see superclass for documentation
    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent uiComponent) throws IOException
    {
        // Rendering occurs in encodeEnd.
    }

    /**
     * Renders the whole tree.  It generates a <code>&lt;span></code> element with an <code>id</code> 
     * attribute if the component has been given an explicit ID.  The model nodes are rendered 
     * recursively by the private <code>encodeNodes</code> method.
     * 
     * @param context FacesContext
     * @param component The component whose children are to be rendered
     * @throws IOException
     */
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException
    {
        if (!component.isRendered()) return;
        
        if (((HtmlTree)component).getValue() == null) return;
        
        ResponseWriter out = context.getResponseWriter();
        String clientId = null;
        if (component.getId() != null && !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
        {
            clientId = component.getClientId(context);
        }
        
        boolean isOuterSpanUsed = false;
        if (clientId != null)
        {
            isOuterSpanUsed = true;
            out.startElement("span", component);
            out.writeAttribute("id", clientId, "id");
        }

        encodeNodes(context, out, (HtmlTree)component, null, 0);
        
        ((HtmlTree)component).setNodeId(null);
        if (isOuterSpanUsed)
        {
            out.endElement("span");
        }
    }

    private void encodeNodes(FacesContext context, ResponseWriter out, HtmlTree tree, String parentId, 
                             int childLevel) 
        throws IOException
    {
        tree.setNodeId(parentId != null ? parentId + NamingContainer.SEPARATOR_CHAR + childLevel : "0");

        TreeNode node = tree.getNode();
        
        UIComponent facet = tree.getFacet(node.getType());        
        if (facet == null)
        {
            throw new IllegalArgumentException("Unable to locate facet with the name: " + node.getType());
        }
        
        encodeRecursive(context, facet);
        
        out.startElement("br", tree);
        out.endElement("br");
        
        // only encode the children if this node is expanded
        if (tree.isNodeExpanded())
        //if (node.isExpanded())
        {
            out.startElement("blockquote", tree);
            int kidId = 0;
            String currId = tree.getNodeId();
        
            List children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                TreeNode kid = (TreeNode) children.get(i);
                encodeNodes(context, out, tree, currId, kidId++);
            }
            out.endElement("blockquote");
        }        
    }

    private void encodeRecursive(FacesContext context, UIComponent component) throws IOException
    {
        /**@todo consider moving this common functionality to a base class or utility class */
        if (!component.isRendered()) return;
        
        component.encodeBegin(context);

        if (component.getRendersChildren())
        {
            component.encodeChildren(context);    
        } 
        else 
        {
            List childList = component.getChildren();
            for (int i=0; i < childList.size(); i++)
            {
                UIComponent child = (UIComponent)childList.get(i);
                encodeRecursive(context, child);
            }
        }

        component.encodeEnd(context);
    }
}
