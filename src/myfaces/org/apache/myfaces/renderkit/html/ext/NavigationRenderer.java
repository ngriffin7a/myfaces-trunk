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

import net.sourceforge.myfaces.component.CommonComponentProperties;
import net.sourceforge.myfaces.component.ext.UINavigation;
import net.sourceforge.myfaces.component.ext.UINavigationItem;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ext.NavigationRendererAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLTableAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationRenderer
    extends HTMLRenderer
    implements
    CallbackRenderer,
        CommonComponentProperties,
        CommonRendererAttributes,
        HTMLUniversalAttributes,
        HTMLEventHandlerAttributes,
        HTMLTableAttributes,
        NavigationRendererAttributes
{

    private static final String LEVEL_CLASSES_CACHE = NavigationRenderer.class.getName() + ".itemClasses";

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
            LogUtil.getLogger().severe("UINavigationItem '" + item.getClientId(FacesContext.getCurrentInstance()) + " has no UINavigation parent ?!");
            return;
        }

        writer.write("\n<tr><td");
        String tdClass = null;
        if (item instanceof UINavigationItem)
        {
            tdClass = calcLevelClass((UINavigation)findNav, level);
        }
        else if (item.getRendererType().equals(NavigationSeparatorRenderer.TYPE))
        {
            tdClass = (String)findNav.getAttribute(SEPARATOR_CLASS);
        }
        if (tdClass != null)
        {
            writer.write(" class=\"");
            writer.write(tdClass);
            writer.write("\"");
        }
        writer.write(">");
    }


    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</td></tr>");
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        CallbackSupport.removeCallbackRenderer(facesContext, uiComponent, this);

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</table>");
    }



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

}
