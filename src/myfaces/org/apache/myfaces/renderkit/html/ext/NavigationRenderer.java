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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.component.ext.UINavigation;
import net.sourceforge.myfaces.component.ext.UINavigationItem;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.attr.ext.NavigationRendererAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationRenderer
    extends HTMLRenderer
    implements CallbackRenderer, NavigationRendererAttributes
{
    private static final Log log = LogFactory.getLog(NavigationRenderer.class);

    public NavigationRenderer()
    {
    }

    public static final String TYPE = "Navigation";
    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table ");
        HTMLUtil.renderStyleClass(writer, uiComponent);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_ATTRIBUTES);
        String panelClass = (String)uiComponent.getAttributes().get(JSFAttr.PANEL_CLASS_ATTR);
        if (panelClass == null)
        {
            writer.write("border=\"0\"");
        }
        writer.write(">");

        CallbackSupport.addCallbackRenderer(facesContext, uiComponent, this);
    }


    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent item) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        int level = 0;
        UIComponent findNav = item.getParent();
        while (findNav != null)
        {
            if (findNav instanceof UINavigation)
            {
                break;
            }
            else if (findNav instanceof UINavigationItem)
            {
                level++;
            }
            findNav = findNav.getParent();
        }
        if (findNav == null)
        {
            log.error("UINavigationItem '" + item.getClientId(FacesContext.getCurrentInstance()) + " has no UINavigation parent ?!");
            return;
        }

        writer.write("\n<tr>");
        openColumn(writer, findNav, item, level);
    }

    protected void openColumn(ResponseWriter writer, UIComponent uiNavigation, UIComponent item,  int level)
        throws IOException
    {
        writer.write("<td");

        String style = null;

        if (item instanceof UINavigationItem)
        {

            boolean open = ((UINavigationItem)item).isOpen();
            boolean active = ((UINavigationItem)item).isActive();

            if (active)
            {
                style = (String)uiNavigation.getAttributes().get(NavigationRendererAttributes.ACTIVE_ITEM_CLASS_ATTR);
            }
            else
            {
                style = open ? (String)uiNavigation.getAttributes().get(NavigationRendererAttributes.OPEN_ITEM_CLASS_ATTR) :
                                    (String)uiNavigation.getAttributes().get(NavigationRendererAttributes.ITEM_CLASS_ATTR);;
            }
        }
        else if (item.getRendererType().equals(NavigationSeparatorRenderer.TYPE))
        {
            style = (String)uiNavigation.getAttributes().get(SEPARATOR_CLASS);
        }

        if (style != null)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }

        writer.write(">");
        indent(writer, level);
    }

    protected void closeColumn(ResponseWriter writer)
        throws IOException
    {
        writer.write("</td>");
    }

    protected void indent(ResponseWriter writer, int level)
        throws IOException
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < level; i++)
        {
            buf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        writer.write(buf.toString());
    }

    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        closeColumn(writer);
        writer.write("</tr>");
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        CallbackSupport.removeCallbackRenderer(facesContext, uiComponent, this);

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</table>");
    }



    /*
    private static final String LEVEL_CLASSES_CACHE = NavigationRenderer.class.getName() + ".itemClasses";
    private static final String DELIMITER = ",";
    private static final String[] DUMMY = {};
    private String calcLevelClass(UINavigation navigation, int level)
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
    */

}
