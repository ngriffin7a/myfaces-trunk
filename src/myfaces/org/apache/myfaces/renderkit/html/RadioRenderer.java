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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemUtil;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RadioRenderer
    extends HTMLRenderer
{
    public static final String TYPE = "Radio";

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (!(uiComponent instanceof UISelectOne))
        {
            LogUtil.getLogger().warning("Expected UISelectOne when rendering input radio. " +
                                        "component: " + uiComponent.getClientId(facesContext));
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        String coumpoundId = uiComponent.getClientId(facesContext);

        Object currentValue = ((UIInput)uiComponent).currentValue(facesContext);
        String currentStrValue = ((currentValue != null) ? currentValue.toString() : null);
        boolean layoutPageDirection = isLayoutPageDirection((UISelectOne)uiComponent);

        Iterator it = SelectItemUtil.getSelectItems(facesContext, uiComponent);

        for (int i = 1; it.hasNext(); i++)
        {
            SelectItem selectItem = (SelectItem)it.next();

            beforeRenderItem(facesContext, selectItem, i, layoutPageDirection);
            writer.write("<input type=\"radio\"");

            writer.write(" name=\"");
            writer.write(coumpoundId);
            writer.write("\" id=\"");
            writer.write(coumpoundId);
            writer.write('"');
            Object itemValue = selectItem.getValue();
            if (itemValue != null)
            {
                writer.write(" value=\"");
                writer.write(itemValue.toString());
                writer.write('"');
            }

            if (currentStrValue != null && itemValue != null &&
                currentStrValue.equals(itemValue))
            {
                writer.write(" checked=\"true\"");
            }

            HTMLUtil.renderCssClass(writer, uiComponent, JSFAttr.SELECT_ONE_CLASS_ATTR);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_ATTRIBUTES);
            HTMLUtil.renderDisabledOnUserRole(facesContext, uiComponent);

            writer.write('>');
            renderLabel(facesContext, (UISelectOne)uiComponent, selectItem);
            afterRenderItem(facesContext, selectItem, i, layoutPageDirection);
        }
    }

    protected void renderLabel(FacesContext facesContext, UISelectOne selectOne, SelectItem item)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        boolean span = selectOne.getAttribute(JSFAttr.OUTPUT_CLASS_ATTR) != null;
        writer.write("&nbsp;");
        if (span)
        {
            writer.write("<span ");
            HTMLUtil.renderCssClass(writer, selectOne, JSFAttr.OUTPUT_CLASS_ATTR);
            writer.write(">");
        }
        writer.write(HTMLEncoder.encode(
                item.getLabel(),
                true,
                true));
        if (span)
        {
            writer.write("</span>");
        }

    }

    protected void beforeRenderItem(FacesContext facesContext,
                                    SelectItem item,
                                    int itemCount,
                                    boolean layoutPageDirection)
        throws IOException
    {
        if (itemCount > 1)
        {
            if (layoutPageDirection)
            {
                facesContext.getResponseWriter().write("<br/>\n");
            }
            else
            {
                facesContext.getResponseWriter().write("&nbsp;&nbsp;&nbsp;");
            }
        }
    }

    protected void afterRenderItem(FacesContext facesContext,
                                   SelectItem item,
                                   int itemCount,
                                   boolean layoutPageDirection)
        throws IOException
    {
    }

    private static final String PAGE_DIRECTION = "PAGE_DIRECTION";

    protected boolean isLayoutPageDirection(UISelectOne uiSelectOne)
    {
        String layout = (String)uiSelectOne.getAttribute(JSFAttr.LAYOUT_ATTR);
        return layout != null && layout.toUpperCase().equals(PAGE_DIRECTION) ? true : false;
    }
}