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
package org.apache.myfaces.custom.tree2;


import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.renderkit.html.HTML;

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

    private static final int NOTHING = 0;
    private static final int CHILDREN = 1;
    private static final int EXPANDED = 2;
    private static final int LINES = 4;
    private static final int LAST = 8;

    /**
     * @todo - this is not thread safe
     */
    int nodeLevel;


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
     * @param context   FacesContext
     * @param component The component whose children are to be rendered
     * @throws IOException
     */
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException
    {
        // reset the nodeLevel (should already be zero but it can't hurt)
        nodeLevel = 0;

        if (!component.isRendered()) return;

        if (((HtmlTree) component).getValue() == null) return;

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

        encodeNodes(context, out, (HtmlTree) component, null, 0);

        ((HtmlTree) component).setNodeId(null);
        if (isOuterSpanUsed)
        {
            out.endElement("span");
        }
    }


    private void encodeNodes(FacesContext context, ResponseWriter out, HtmlTree tree, String parentId, int childCount)
        throws IOException
    {

        /** @todo - showNav and showLines should be parameters */
        boolean showNav = true;
        boolean showLines = true;

        tree.setNodeId(parentId != null ? parentId + NamingContainer.SEPARATOR_CHAR + childCount : "0");

        TreeNode node = tree.getNode();

        UIComponent facet = tree.getFacet(node.getType());
        if (facet == null)
        {
            throw new IllegalArgumentException("Unable to locate facet with the name: " + node.getType());
        }

        /** @todo - format rendered html */

        // start node table
        out.startElement(HTML.TABLE_ELEM, tree);
        out.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        out.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        out.writeAttribute(HTML.BORDER_ATTR, "0", null);
        out.startElement(HTML.TR_ELEM, tree);

        // render node padding
        String[] pathInfo = tree.getPathInformation(tree.getNodeId());
        for (int i = 0; i < pathInfo.length - 1; i++)
        {
            String lineSrc = (!tree.isLastChild(pathInfo[i]) && showLines)
                ? AddResource.getResourceMappedPath(HtmlTreeRenderer.class, "images/line-trunk.gif", context)
                : AddResource.getResourceMappedPath(HtmlTreeRenderer.class, "images/spacer.gif", context);
            out.startElement(HTML.TD_ELEM, tree);
            out.writeAttribute(HTML.WIDTH_ATTR, "19", null);
            out.writeAttribute(HTML.HEIGHT_ATTR, "100%", null);
            out.writeURIAttribute("background", lineSrc, null);
            out.startElement(HTML.IMG_ELEM, tree);
            out.writeURIAttribute(HTML.SRC_ATTR, lineSrc, null);
            out.writeAttribute(HTML.WIDTH_ATTR, "19", null);
            out.writeAttribute(HTML.HEIGHT_ATTR, "18", null);
            out.writeAttribute(HTML.BORDER_ATTR, "0", null);
            out.endElement(HTML.IMG_ELEM);
            out.endElement(HTML.TD_ELEM);
        }

        // render navigation
        if (showNav)
        {
            String navSrc = null;
            int bitMask = NOTHING;
            bitMask += (node.getChildCount() > 0) ? CHILDREN : NOTHING;
            bitMask += (tree.isNodeExpanded()) ? EXPANDED : NOTHING;
            bitMask += (tree.isLastChild(tree.getNodeId())) ? LAST : NOTHING;
            bitMask += (showLines) ? LINES : NOTHING;

            switch (bitMask)
            {
                case (NOTHING):
                case (LAST):
                    navSrc = "images/spacer.gif";
                    break;
                case (LINES):
                    navSrc = "images/line-middle.gif";
                    break;
                case (LINES + LAST):
                    navSrc = "images/line-last.gif";
                    break;
                case (CHILDREN):
                case (CHILDREN + LAST):
                    navSrc = "images/nav-plus.gif";
                    break;
                case (CHILDREN + LINES):
                    navSrc = "images/nav-plus-line-middle.gif";
                    break;
                case (CHILDREN + LINES + LAST):
                    navSrc = "images/nav-plus-line-last.gif";
                    break;
                case (CHILDREN + EXPANDED):
                case (CHILDREN + EXPANDED + LAST):
                    navSrc = "images/nav-minus.gif";
                    break;
                case (CHILDREN + EXPANDED + LINES):
                    navSrc = "images/nav-minus-line-middle.gif";
                    break;
                case (CHILDREN + EXPANDED + LINES + LAST):
                    navSrc = "images/nav-minus-line-last.gif";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid bit mask of " + bitMask);
            }

            // render nav cell
            out.startElement(HTML.TD_ELEM, tree);
            out.writeAttribute(HTML.WIDTH_ATTR, "19", null);
            out.writeAttribute(HTML.HEIGHT_ATTR, "100%", null);
            out.writeAttribute("valign", "top", null);
            if ((bitMask & LINES) != 0 && (bitMask & LAST) == 0)
            {
                out.writeURIAttribute("background",
                    AddResource.getResourceMappedPath(HtmlTreeRenderer.class, "images/line-trunk.gif", context),
                    null);
            }
            out.startElement(HTML.IMG_ELEM, tree);
            out.writeURIAttribute(HTML.SRC_ATTR, AddResource.getResourceMappedPath(HtmlTreeRenderer.class, navSrc, context), null);
            out.writeAttribute(HTML.WIDTH_ATTR, "19", null);
            out.writeAttribute(HTML.HEIGHT_ATTR, "18", null);
            out.writeAttribute(HTML.BORDER_ATTR, "0", null);
            out.endElement(HTML.IMG_ELEM);
            out.endElement(HTML.TD_ELEM);
        }

        // render node
        out.startElement(HTML.TD_ELEM, tree);
        encodeRecursive(context, facet);
        out.endElement(HTML.TD_ELEM);

        // end node table
        out.endElement(HTML.TR_ELEM);
        out.endElement(HTML.TABLE_ELEM);

        // only encode the children if this node is expanded
        if (tree.isNodeExpanded())
        {
            nodeLevel++;
            int kidId = 0;
            String currId = tree.getNodeId();
            List children = node.getChildren();
            for (int i = 0; i < children.size(); i++)
            {
                encodeNodes(context, out, tree, currId, kidId++);
            }
            nodeLevel--;
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
        } else
        {
            List childList = component.getChildren();
            for (int i = 0; i < childList.size(); i++)
            {
                UIComponent child = (UIComponent) childList.get(i);
                encodeRecursive(context, child);
            }
        }

        component.encodeEnd(context);
    }
}