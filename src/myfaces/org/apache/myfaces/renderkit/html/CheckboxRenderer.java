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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.renderkit.attr.CheckboxRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLInputAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemUtil;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

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
        return s.equals(UISelectBoolean.TYPE) ||
               s.equals(UISelectMany.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UISelectBoolean ||
               uicomponent instanceof UISelectMany;
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


    public void decode(FacesContext facescontext, UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent.getComponentType().equals(UISelectMany.TYPE))
        {
            String clientId = uiComponent.getClientId(facescontext);
            String[] newValues = facescontext.getServletRequest().getParameterValues(clientId);
            ((UISelectMany)uiComponent).setSelectedValues(newValues);
            uiComponent.setValid(true);
        }
        else if (uiComponent.getComponentType().equals(UISelectBoolean.TYPE))
        {
            String clientId = uiComponent.getClientId(facescontext);
            String[] newValues = facescontext.getServletRequest().getParameterValues(clientId);
            if (newValues != null)
            {
                ((UISelectBoolean)uiComponent).setSelected(true);
            }
            else
            {
                ((UISelectBoolean)uiComponent).setSelected(false);
            }
            uiComponent.setValid(true);
        }
        else
        {
            throw new IllegalArgumentException("CheckboxRenderer does not support components of type '" + uiComponent.getComponentType() + "'.");
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent.getComponentType().equals(UISelectMany.TYPE))
        {
            Set selectedValuesSet
                = SelectItemUtil.getSelectedValuesAsStringSet(facesContext,
                                                              uiComponent);

            boolean breakLine = false;
            for (Iterator it = SelectItemUtil.getSelectItems(facesContext, uiComponent); it.hasNext(); )
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
                String selectItemStrValue = selectItem.getValue().toString();
                boolean checked = selectedValuesSet.contains(selectItemStrValue);
                drawCheckbox(facesContext,
                             uiComponent,
                             selectItemStrValue,
                             selectItem.getLabel(),
                             checked);
            }
        }
        else if (uiComponent.getComponentType().equals(UISelectBoolean.TYPE))
        {
            Boolean checked = (Boolean)uiComponent.currentValue(facesContext);
            String value = getStringValue(facesContext, uiComponent);
            drawCheckbox(facesContext, uiComponent, value, null,
                         checked != null ? checked.booleanValue() : false);
        }
        else
        {
            throw new IllegalArgumentException("CheckboxRenderer does not support components of type '" + uiComponent.getComponentType() + "'.");
        }
    }

    private void drawCheckbox(FacesContext facesContext,
                              UIComponent uiComponent,
                              String value,
                              String label,
                              boolean checked)
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
