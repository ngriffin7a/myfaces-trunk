/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.ext.UINavigation;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ext.NavigationRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLTableAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.state.StateRestorer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.tree.TreeUtils;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;
import javax.faces.tree.Tree;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationRenderer
    extends HTMLRenderer
    implements
        CommonComponentAttributes,
        CommonRendererAttributes,
        HTMLUniversalAttributes,
        HTMLEventHandlerAttributes,
        HTMLTableAttributes,
        NavigationRendererAttributes
{

    private static final String LEVEL_CLASSES_CACHE = NavigationRenderer.class.getName() + ".itemClasses";

    /*
    public static final String CURRENT_NAVIGATION_ATTR
        = NavigationRenderer.class.getName() + ".GET_CHILDREN_FROM_REQUEST";
        */
    private static final String DECODED_ATTR = NavigationRenderer.class.getName() + ".DECODED";

    protected RenderKitFactory _rkFactory;

    public NavigationRenderer()
    {
        _rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    }

    public static final String TYPE = "Navigation";
    public String getRendererType()
    {
        return TYPE;
    }

    /*
    public boolean supportsComponentType(String s)
    {
        return s.equals(UINavigation.TYPE);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof UINavigation;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIPanel.TYPE, TLD_EXT_URI, "navigation", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_EXT_URI, "navigation", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_EXT_URI, "navigation", HTML_TABLE_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_EXT_URI, "navigation", NAVIGATION_ATTRIBUTES);
    }
    */



    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        super.decode(facesContext, uiComponent);

        //Remember, that we have decoded
        uiComponent.setAttribute(DECODED_ATTR, Boolean.TRUE);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table ");
        HTMLUtil.renderCssClass(writer, uiComponent, PANEL_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_TABLE_ATTRIBUTES);
        String panelClass = (String)uiComponent.getAttribute(PANEL_CLASS_ATTR);
        if (panelClass == null)
        {
            writer.write("border=\"0\"");
        }
        writer.write(">");
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</table>");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        Boolean b = (Boolean)uiComponent.getAttribute(DECODED_ATTR);
        if (b == null || !b.booleanValue())
        {
            //There was no decoding, so we can assume that the state has not been
            //restored yet and we must restore open/close state for children from
            //previous tree.
            ServletRequest servletRequest = (ServletRequest)facesContext.getExternalContext().getRequest();
            StateRestorer stateRestorer
                = (StateRestorer)servletRequest.getAttribute(StateRestorer.STATE_RESTORER_REQUEST_ATTR);
            if (stateRestorer != null)
            {
                Tree previousTree  = stateRestorer.getPreviousTree(facesContext);
                if (previousTree != null && previousTree != facesContext.getTree())
                {
                    for (Iterator it = TreeUtils.treeIterator(uiComponent); it.hasNext();)
                    {
                        UIComponent comp = (UIComponent)it.next();
                        if (comp instanceof UINavigation.UINavigationItem)
                        {
                            String clientId = comp.getClientId(facesContext);
                            UIComponent prevNavItem = previousTree.getRoot().findComponent(clientId);
                            if (prevNavItem != null)
                            {
                                Boolean open = (Boolean)prevNavItem.getAttribute(UINavigation.UINavigationItem.OPEN_ATTR);
                                comp.setAttribute(UINavigation.UINavigationItem.OPEN_ATTR, open);
                            }
                        }
                    }
                }
            }
        }

        renderChildren(facesContext, uiComponent, 0, uiComponent.getChildren());
    }

    protected void renderChildren(FacesContext facesContext,
                                  UIComponent navigation,
                                  int level,
                                  Iterator children)
        throws IOException
    {
        while(children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child.getRendererType().equals(NavigationItemRenderer.TYPE))
            {
                renderMenuItem(facesContext, navigation, level, child);
            }
            else
            {
                throw new FacesException("Unexpected component of renderer type " + child.getRendererType());
            }
        }
    }

    protected void renderMenuItem(FacesContext facesContext,
                                  UIComponent navigation,
                                  int level,
                                  UIComponent item)
        throws IOException
    {
        if (!isComponentVisible(facesContext, item))
        {
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("\n<tr><td");

        String columnClass = (String)item.getAttribute(NavigationItemRenderer.COLUMN_CLASSES_ATTR);
        if (columnClass != null)
        {
            writer.write(" class=\"");
            writer.write(columnClass);
            writer.write("\"");
        }
        writer.write(">");

        String itemClass = calcLevelClass(navigation, level);
        if (itemClass != null)
        {
            writer.write("<span class=\"");
            writer.write(itemClass);
            writer.write("\">");

            item.encodeBegin(facesContext);
            item.encodeEnd(facesContext);
            writer.write("</span>");
        }
        else
        {
            for (int i = 0; i < level; i++)
            {
                writer.write("&nbsp;");
            }
            if (level > 0)
            {
                writer.write("<font size=\"-" + level + "\">");
            }
            item.encodeBegin(facesContext);
            item.encodeEnd(facesContext);

            if (level > 0)
            {
                writer.write("</font>");
            }
        }
        writer.write("</td></tr>");

        if (UIComponentUtils.getBooleanAttribute(item,
                                                 UINavigation.UINavigationItem.OPEN_ATTR,
                                                 false))
        {
            renderChildren(facesContext, navigation, level + 1, item.getChildren());
        }

    }


    private static final String DELIMITER = ",";
    private static final String[] DUMMY = {};
    private String calcLevelClass(UIComponent navigation, int level)
    {
        String[] levelClasses = (String[])navigation.getAttribute(LEVEL_CLASSES_CACHE);
        if (levelClasses == null)
        {
            String levelClassesStr = (String)navigation.getAttribute(LEVEL_CLASSES);
            if (levelClassesStr != null)
            {
                StringTokenizer tokenizer = new StringTokenizer(levelClassesStr, DELIMITER);
                int size = tokenizer.countTokens();
                levelClasses = new String[size];
                for (int i = 0; i < size; i++)
                {
                    levelClasses[i] = tokenizer.nextToken();
                }
            }
            else
            {
                levelClasses = DUMMY;
            }
            navigation.setAttribute(LEVEL_CLASSES_CACHE, levelClasses);
        }
        if (levelClasses == DUMMY ||
            levelClasses.length == 0)
        {
            return null;
        }
        return levelClasses[level % levelClasses.length];
    }
}
