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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.renderkit.attr.*;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemHelper;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLInputAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CheckboxRenderer
    extends HTMLRenderer
    implements CommonComponentAttributes,
               CommonRendererAttributes,
    HTMLUniversalAttributes,
    HTMLEventHandlerAttributes,
    HTMLInputAttributes,
               CheckboxRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Checkbox";
    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UISelectBoolean.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UISelectBoolean;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UISelectBoolean.TYPE, TLD_HTML_URI, "selectboolean_checkbox", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UISelectBoolean.TYPE, TLD_HTML_URI, "selectboolean_checkbox", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UISelectBoolean.TYPE, TLD_HTML_URI, "selectboolean_checkbox", HTML_INPUT_ATTRIBUTES);
        addAttributeDescriptors(UISelectBoolean.TYPE, TLD_HTML_URI, "selectboolean_checkbox", SELECT_BOOLEAN_CHECKBOX_ATTRIBUTES);
        addAttributeDescriptors(UISelectBoolean.TYPE, TLD_HTML_URI, "selectboolean_checkbox", USER_ROLE_ATTRIBUTES);

        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_checkbox", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_checkbox", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_checkbox", HTML_INPUT_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_checkbox", SELECT_MANY_CHECKBOX_ATTRIBUTES);
        addAttributeDescriptors(UISelectMany.TYPE, TLD_HTML_URI, "selectmany_checkbox", USER_ROLE_ATTRIBUTES);
    }


    public void decode(FacesContext facescontext, UIComponent uicomponent)
        throws IOException
    {
        if (uicomponent.getComponentType().equals(UISelectMany.TYPE))
        {
            String clientId = uicomponent.getClientId(facescontext);
            String[] newValues = facescontext.getServletRequest().getParameterValues(clientId);
            ((UISelectMany)uicomponent).setSelectedValues(newValues);
            uicomponent.setValid(true);
        }
        if (uicomponent.getComponentType().equals(UISelectBoolean.TYPE))
        {
            String clientId = uicomponent.getClientId(facescontext);
            String[] newValues = facescontext.getServletRequest().getParameterValues(clientId);
            if (newValues != null)
            {
                ((UISelectBoolean)uicomponent).setSelected(true);
            }
            else
            {
                ((UISelectBoolean)uicomponent).setSelected(false);
            }
            uicomponent.setValid(true);
        }
        else
        {
            // TODO:
        }

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent.getComponentType().equals(UISelectMany.TYPE))
        {
            Object currentValue = uiComponent.currentValue(facesContext);

            boolean breakLine = false;
            for (Iterator it = SelectItemHelper.getSelectItems(facesContext, uiComponent); it.hasNext(); )
            {
                if (breakLine)
                {
                    ResponseWriter writer = facesContext.getResponseWriter();
                    writer.write("<br>");
                }
                else
                {
                    breakLine = true;
                }
                SelectItem selectItem = (SelectItem)it.next();

                boolean checked = SelectItemHelper.isItemSelected(facesContext, uiComponent, currentValue, selectItem);

                Object objValue = selectItem.getValue();
                String selectItemValue = objValue != null ? objValue.toString() : null;

                drawCheckbox(facesContext, uiComponent, selectItemValue, selectItem.getLabel(), checked);
            }
        }
        else if (uiComponent.getComponentType().equals(UISelectBoolean.TYPE))
        {
            Boolean checked = (Boolean)uiComponent.currentValue(facesContext);
            String value = getStringValue(facesContext, uiComponent);
            drawCheckbox(facesContext, uiComponent, value, null, checked != null ? checked.booleanValue() : false);
        }
        else
        {
            LogUtil.getLogger().warning(
                "Component "
                    + UIComponentUtils.toString(uiComponent)
                    + "is not type SelectBoolean.");
            return;
        }
    }

    private void drawCheckbox(FacesContext facesContext, UIComponent uiComponent, String value, String label, boolean checked)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<input type=\"checkbox\"");
        writer.write(" name=\"");
        writer.write(uiComponent.getClientId(facesContext));
        writer.write("\"");
        writer.write(" id=\"");
        writer.write(uiComponent.getClientId(facesContext));
        writer.write("\"");

        if(checked)
        {
            writer.write(" checked ");
        }

        if (value != null && value.length() > 0)
        {
            writer.write(" value=\"");
            writer.write(value);
            writer.write("\"");
        }

        CommonAttributes.renderCssClass(writer, uiComponent, SELECT_BOOLEAN_CLASS_ATTR);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_INPUT_ATTRIBUTES);

        writer.write(">");
        if (label != null && label.length() > 0)
        {
            writer.write("&nbsp;");
            writer.write(label);
        }
    }

}
