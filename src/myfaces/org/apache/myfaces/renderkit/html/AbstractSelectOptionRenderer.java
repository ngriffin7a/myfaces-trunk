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

import net.sourceforge.myfaces.component.UISelectMany;
import net.sourceforge.myfaces.renderkit.attr.*;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemHelper;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Base class for ListboxRenderer and MenuRenderer (= renderers that write a HTML select).
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class AbstractSelectOptionRenderer
    extends HTMLRenderer
    implements HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLSelectAttributes,
               CommonRendererAttributes
{

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
    }

    protected void encodeEnd(FacesContext facesContext,
                          UIComponent uiComponent,
                          int size,
                          String rendererType)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        boolean multipleSelect = uiComponent.getComponentType() == UISelectMany.TYPE ? true : false;

        Iterator it = SelectItemHelper.getSelectItems(facesContext, uiComponent);
        if (it.hasNext())
        {
            writer.write("<select");
            writer.write(" name=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write("\"");

            if (rendererType.equals(ListboxRenderer.TYPE))
            {
                writer.write(" size=\"");
                writer.write(new Integer(size).toString());
                writer.write("\"");
            }

            CommonAttributes.renderCssClass(writer, uiComponent, multipleSelect
                                                                 ? SELECT_MANY_CLASS_ATTR
                                                                 : SELECT_ONE_CLASS_ATTR);
            CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
            CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
            CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_SELECT_ATTRIBUTES);

            if (multipleSelect) writer.write(" multiple ");
            writer.write(">\n");

            Object currentValue = uiComponent.currentValue(facesContext);

            while (it.hasNext())
            {
                SelectItem item = (SelectItem)it.next();
                writer.write("\t\t<option");
                Object value = item.getValue();
                if (value != null)
                {
                    String str = value.toString();
                    writer.write(" value=\"");
                    writer.write(HTMLEncoder.encode(str, false, false));
                    writer.write("\"");
                    if (SelectItemHelper.isItemSelected(facesContext, uiComponent, currentValue, item))
                    {
                        writer.write(" selected");
                    }
                }
                writer.write(">");
                writer.write(HTMLEncoder.encode(item.getLabel(), true, true));

                writer.write("</option>\n");
            }

            writer.write("</select>");
        }
    }

}
