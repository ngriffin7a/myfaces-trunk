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
import net.sourceforge.myfaces.component.ext.UITab;
import net.sourceforge.myfaces.component.ext.UITabHeader;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ext.TabbedPaneRendererAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.GroupRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLTableAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

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
public class TabbedPaneRenderer
    extends GroupRenderer
    implements CallbackRenderer,
               CommonComponentProperties,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLTableAttributes,
               TabbedPaneRendererAttributes
{
    private static final String HEADER_ATTR = TabbedPaneRenderer.class.getName() + ".HEADER";
    private static final String TAB_ATTR = TabbedPaneRenderer.class.getName() + ".TAB";
    private static final String TAB_COUNT_ATTR = TabbedPaneRenderer.class.getName() + ".TAB_COUNT";

    public static final String TYPE = "TabbedPane";
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
            writer.write("border=\"1\"");
        }
        writer.write(">");

        CallbackSupport.addCallbackRenderer(facesContext, uiComponent, this);
    }


    protected boolean isWithinHeader(UIComponent tabbedPane)
    {
        Boolean withinHeader = (Boolean)tabbedPane.getAttribute(HEADER_ATTR);
        return (withinHeader != null && withinHeader.booleanValue());
    }

    protected void setWithinHeader(UIComponent tabbedPane, boolean b)
    {
        tabbedPane.setAttribute(HEADER_ATTR, Boolean.valueOf(b));
    }

    protected boolean isWithinTab(UIComponent tabbedPane)
    {
        Boolean withinTab = (Boolean)tabbedPane.getAttribute(TAB_ATTR);
        return (withinTab != null && withinTab.booleanValue());
    }

    protected void setWithinTab(UIComponent tabbedPane, boolean b)
    {
        tabbedPane.setAttribute(TAB_ATTR, Boolean.valueOf(b));
    }

    protected int getTabCount(UIComponent tabbedPane)
    {
        Integer v = (Integer)tabbedPane.getAttribute(TAB_COUNT_ATTR);
        return v == null ? 0 : v.intValue();
    }

    protected void setTabCount(UIComponent tabbedPane, int v)
    {
        tabbedPane.setAttribute(TAB_COUNT_ATTR, new Integer(v));
    }




    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent child) throws IOException
    {
        UIComponent tabbedPane = child.getParent();
        if (!tabbedPane.getRendererType().equals(TYPE))
        {
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        if (child instanceof UITabHeader)
        {
            if (!isWithinHeader(tabbedPane))
            {
                writer.write("\n<tr>\n");
                setWithinHeader(tabbedPane, true);
            }
            writer.write("\n\t<td");
            String style;
            if (((UITabHeader)child).isActive())
            {
                style = (String)tabbedPane.getAttribute(ACTIVE_HEADER_CLASS_ATTR);
            }
            else
            {
                style = (String)tabbedPane.getAttribute(INACTIVE_HEADER_CLASS_ATTR);
            }
            if (style != null)
            {
                writer.write(" class=\"");
                writer.write(style);
                writer.write("\"");
            }
            writer.write(">");
            setTabCount(tabbedPane, getTabCount(tabbedPane) + 1);
        }
        else if (child instanceof UITab)
        {
            if (isWithinHeader(tabbedPane))
            {
                writer.write("\n</tr><tr>\n");
                setWithinHeader(tabbedPane, false);
            }

            if (((UITab)child).isActive())
            {
                writer.write("\n\t<td colspan=\"");
                writer.write(Integer.toString(getTabCount(tabbedPane)));
                writer.write("\">");
            }
        }
    }


    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent child) throws IOException
    {
        UIComponent tabbedPane = child.getParent();
        if (!tabbedPane.getRendererType().equals(TYPE))
        {
            return;
        }
        ResponseWriter writer = facesContext.getResponseWriter();
        if (child instanceof UITabHeader)
        {
            writer.write("</td>");
        }
        else if (child instanceof UITab)
        {
            if (((UITab)child).isActive())
            {
                writer.write("</td>");
            }
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        CallbackSupport.removeCallbackRenderer(facesContext, uiComponent, this);

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("\n</tr></table>");
    }

}
