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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemUtil;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlCheckboxRenderer
extends HtmlRenderer
{

    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        ExternalContext externalContext = facesContext.getExternalContext();
        String clientId = uiComponent.getClientId(facesContext);
        Map requestParametersMap = externalContext.getRequestParameterValuesMap();
        String[] newValues = (String[]) requestParametersMap.get(clientId);

        if (uiComponent instanceof UISelectMany)
        {
            ((UISelectMany) uiComponent).setSelectedValues(newValues);
        }
        else if (uiComponent instanceof UISelectBoolean)
        {
            if (newValues != null && newValues.length>0)
            {
                ((UISelectBoolean) uiComponent).setSelected(newValues[0].equals("1"));
            }
            else
            {
                ValueBinding vb = ((UISelectBoolean) uiComponent).getValueBinding(JSFAttr.VALUE_ATTR);

                if (vb != null)
                {
                    //If there is a model reference, we must beware the model
                    //from being changed later in the update model phase.
                    //Since we cannot avoid the model beeing set, we simply
                    //get the current model value and overwrite the component's
                    //value.

                    ((UISelectBoolean) uiComponent).setValue(vb.getValue(facesContext));
                }
            }
        }
        else
        {
            throw new IllegalArgumentException(
                "CheckboxRenderer does not support components of type '"
                + uiComponent.getClass().getName() + "'.");
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            Set selectedValuesSet =
                SelectItemUtil.getSelectedValuesAsStringSet(
                    facesContext, (UISelectMany) uiComponent);

            boolean breakLine = false;

            for (Iterator it = SelectItemUtil.getSelectItems(facesContext, uiComponent);
                        it.hasNext();)
            {
                if (breakLine)
                {
                    ResponseWriter writer = facesContext.getResponseWriter();
                    writer.write(HTML.BR_ELEM);
                }
                else
                {
                    breakLine = true;
                }

                SelectItem selectItem = (SelectItem) it.next();
                String selectItemStrValue = selectItem.getValue().toString();
                boolean checked = selectedValuesSet.contains(selectItemStrValue);

                drawCheckbox(
                    facesContext,
                    uiComponent,
                    selectItemStrValue,
                    selectItem.getLabel(),
                    checked);
            }
        }
        else if (uiComponent instanceof UISelectBoolean)
        {
            Boolean checked = (Boolean) ((UISelectBoolean) uiComponent).getValue();

            drawCheckbox(
                facesContext, uiComponent, "1", null,
                (checked != null) ? checked.booleanValue() : false);

            //We also render a hidden input with the same name and a value of 0.
            //That way a parameter is always sent when the respective form is
            //submitted and we can distinguish between "false" (i.e checkbox
            //was posted unchecked) and "form not submitted" (i.e checkbox not
            //posted at all).
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR,HTML.INPUT_TYPE_HIDDEN,null);
            writer.writeAttribute(HTML.NAME_ATTR,uiComponent.getClientId(facesContext),null);
            writer.writeAttribute(HTML.VALUE_ATTR,"0",null);
        }
        else
        {
            throw new IllegalArgumentException(
                "CheckboxRenderer does not support components of type '"
                + uiComponent.getClass().getName() + "'.");
        }
    }

    private void drawCheckbox(
        FacesContext facesContext, UIComponent uiComponent, String value, String label,
        boolean checked)
    throws IOException
    {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR,HTML.INPUT_TYPE_CHECKBOX,null);
        writer.writeAttribute(HTML.NAME_ATTR,clientId,null);
        writer.writeAttribute(HTML.ID_ATTR,clientId,null);

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR,HTML.INPUT_CHECKED_VALUE,null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR,value,null);
        }

        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(writer, uiComponent, facesContext);

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.write(label);
        }
    }
}
