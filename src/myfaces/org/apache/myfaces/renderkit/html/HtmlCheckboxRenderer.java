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

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.io.StringWriter;
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
    private static final String CHECKED_VALUE = "checked";
    private static final String CHECKBOX_INPUT_TYPE = "checkbox";
    private static final String HIDDEN_INPUT_TYPE = "hidden";

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
                //FIXME
                //String valueRef = ((UISelectBoolean) uiComponent).getValueRef();
                String valueRef = null;

                if (valueRef != null)
                {
                    //If there is a model reference, we must beware the model
                    //from being changed later in the update model phase.
                    //Since we cannot avoid the model beeing set, we simply
                    //get the current model value and overwrite the component's
                    //value.
                    //FIXME
                    //ValueBinding vb = getApplication().getValueBinding(valueRef);

                    ValueBinding vb = null;
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

                UISelectItem uiSelectItem = (UISelectItem) it.next();
                String selectItemStrValue = uiSelectItem.getValue().toString();
                boolean checked = selectedValuesSet.contains(selectItemStrValue);

                drawCheckbox(
                    facesContext,
                    uiComponent,
                    selectItemStrValue,
                    uiSelectItem.getItemLabel(),
                    checked);
            }
        }
        else if (uiComponent instanceof UISelectBoolean)
        {
            //FIXME
            //Boolean checked = (Boolean) ((UISelectBoolean) uiComponent).currentValue(facesContext);
            Boolean checked = null;

            //String value = getStringValue(facesContext, (UISelectBoolean)uiComponent);
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
            writer.writeAttribute(HTML.TYPE_ATTR,HIDDEN_INPUT_TYPE,null);
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

        StringWriter buf = new StringWriter();
        ResponseWriter inputElemWriter = writer.cloneWithWriter(buf);

        inputElemWriter.startElement(HTML.INPUT_ELEM, uiComponent);
        inputElemWriter.writeAttribute(HTML.TYPE_ATTR,CHECKBOX_INPUT_TYPE,null);
        inputElemWriter.writeAttribute(HTML.NAME_ATTR,clientId,null);
        inputElemWriter.writeAttribute(HTML.ID_ATTR,clientId,null);

        if (checked)
        {
            inputElemWriter.writeAttribute(HTML.CHECKED_ATTR,CHECKED_VALUE,null);
        }

        if ((value != null) && (value.length() > 0))
        {
            inputElemWriter.writeAttribute(HTML.VALUE_ATTR,value,null);
        }

        HTMLUtil.renderStyleClass(inputElemWriter, uiComponent);
        HTMLUtil.renderHTMLAttributes(inputElemWriter, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(inputElemWriter, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(inputElemWriter, uiComponent, HTML.INPUT_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(inputElemWriter, uiComponent, facesContext);

        inputElemWriter.close();

        writer.write(buf.toString());

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.write(label);
        }
    }
}
