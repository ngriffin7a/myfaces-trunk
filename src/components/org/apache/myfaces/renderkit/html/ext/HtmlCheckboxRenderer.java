/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.renderkit.html.ext;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.html.ext.HtmlSelectManyCheckbox;
import org.apache.myfaces.custom.checkbox.HtmlCheckbox;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.html.HtmlCheckboxRendererBase;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Iterator;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlCheckboxRenderer
        extends HtmlCheckboxRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlRadioRenderer.class);

    private static final String PAGE_DIRECTION = "pageDirection";

    private static final String LINE_DIRECTION = "lineDirection";

    private static final String EXTERNAL_TRUE_VALUE = "true";

    private static final String LAYOUT_SPREAD = "spread";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");

        if (component instanceof HtmlCheckbox)
        {
            renderSingleCheckbox(context, (HtmlCheckbox)component);
        }
        else if (component instanceof UISelectMany)
        {
            String layout = getLayout((UISelectMany)component);
            if (layout != null && layout.equals(LAYOUT_SPREAD))
            {
                return; //checkbox inputs are rendered by spread checkbox components
            }
            else
            {
                //super.encodeEnd(context, component);
                chooseRenderMode(context, component);
            }
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    private void chooseRenderMode(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean)
        {
            Boolean value = RendererUtils.getBooleanValue( uiComponent );
            boolean isChecked = value != null ? value.booleanValue() : false;
            renderCheckbox(facesContext, uiComponent, EXTERNAL_TRUE_VALUE,
                    null, isChecked, true);
        }
        else if (uiComponent instanceof UISelectMany)
        {
            renderCheckboxList(facesContext, (UISelectMany) uiComponent);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class "
                    + uiComponent.getClass().getName());
        }
    }

    public void renderCheckboxList(FacesContext facesContext,
            UISelectMany selectMany) throws IOException
    {

        String layout = getLayout(selectMany);
        boolean pageDirectionLayout = false; //Default to lineDirection
        if (layout != null)
        {
            if (layout.equals(PAGE_DIRECTION))
            {
                pageDirectionLayout = true;
            }
            else if (layout.equals(LINE_DIRECTION))
            {
                pageDirectionLayout = false;
            }
            //else
            //{
            //    log.error("Wrong layout attribute for component "
            //            + selectMany.getClientId(facesContext) + ": " + layout);
            //}
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, selectMany);
        HtmlRendererUtils.renderHTMLAttributes(writer, selectMany,
                HTML.SELECT_TABLE_PASSTHROUGH_ATTRIBUTES);
        HtmlRendererUtils.writeIdIfNecessary(writer, selectMany, facesContext);

        if (!pageDirectionLayout)
            writer.startElement(HTML.TR_ELEM, selectMany);

        Converter converter;
        try
        {
            converter = RendererUtils.findUISelectManyConverter(facesContext,
                    selectMany);
        }
        catch (FacesException e)
        {
            //log.error("Error finding Converter for component with id "
            //        + selectMany.getClientId(facesContext));
            converter = null;
        }

        Set lookupSet = RendererUtils.getSubmittedValuesAsSet(facesContext, selectMany, converter, selectMany);
        boolean useSubmittedValues = lookupSet != null;

        if (!useSubmittedValues) {
            lookupSet = RendererUtils.getSelectedValuesAsSet(facesContext, selectMany, converter, selectMany);
        }

        for (Iterator it = RendererUtils.getSelectItemList(selectMany)
                .iterator(); it.hasNext();) {
            SelectItem selectItem = (SelectItem) it.next();
            Object itemValue = selectItem.getValue(); // TODO : Check here for getSubmittedValue. Look at RendererUtils.getValue

            String itemStrValue;
            if (converter == null) {
                itemStrValue = itemValue.toString();
            } else {
                itemStrValue = converter.getAsString(facesContext, selectMany,
                        itemValue);
            }

            writer.write("\t\t");
            if (pageDirectionLayout)
                writer.startElement(HTML.TR_ELEM, selectMany);
            writer.startElement(HTML.TD_ELEM, selectMany);
            writer.startElement(HTML.LABEL_ELEM, selectMany);

            boolean checked = (useSubmittedValues && lookupSet
                    .contains(itemStrValue))
                    || (!useSubmittedValues && lookupSet.contains(itemValue));

            if(selectMany instanceof HtmlSelectManyCheckbox)
            {
                if(((HtmlSelectManyCheckbox)selectMany).isDisplayValueOnly())
                {
                    renderCheckboxValueOnly(facesContext,
                                            (HtmlSelectManyCheckbox)selectMany,
                                            itemStrValue,
                                            selectItem.getLabel(),
                                            lookupSet.contains(itemStrValue),
                                            true);
                }
                else
                {
                    renderCheckbox(facesContext,
                                   selectMany,
                                   itemStrValue,
                                   selectItem.getLabel(),
                                   checked,
                                   false);
                }
            }
            writer.endElement(HTML.LABEL_ELEM);
            writer.endElement(HTML.TD_ELEM);
            if (pageDirectionLayout)
                writer.endElement(HTML.TR_ELEM);
        }

        if (!pageDirectionLayout)
            writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }

    private void renderSingleCheckbox(FacesContext facesContext, HtmlCheckbox checkbox) throws IOException
    {
        String forAttr = checkbox.getFor();
        if (forAttr == null)
        {
            throw new IllegalStateException("mandatory attribute 'for'");
        }
        int index = checkbox.getIndex();
        if (index < 0)
        {
            throw new IllegalStateException("positive index must be given");
        }

        UIComponent uiComponent = checkbox.findComponent(forAttr);
        if (uiComponent == null)
        {
            throw new IllegalStateException("Could not find component '" + forAttr + "' (calling findComponent on component '" + checkbox.getClientId(facesContext) + "')");
        }
        if (!(uiComponent instanceof UISelectMany))
        {
            throw new IllegalStateException("UISelectMany expected");
        }

        UISelectMany uiSelectMany = (UISelectMany)uiComponent;
        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(uiSelectMany);
        if (index >= selectItemList.size())
        {
            throw new IndexOutOfBoundsException("index " + index + " >= " + selectItemList.size());
        }

        try
        {
            converter = RendererUtils.findUISelectManyConverter(facesContext, uiSelectMany);
        }
        catch (FacesException e)
        {
            converter = null;
        }

        SelectItem selectItem = (SelectItem)selectItemList.get(index);
        Object itemValue = selectItem.getValue();
        String itemStrValue;
        if (converter == null)
        {
            itemStrValue = itemValue.toString();
        }
        else
        {
            itemStrValue = converter.getAsString(facesContext, uiSelectMany, itemValue);
        }

        //TODO: we must cache this Set!
        Set lookupSet = RendererUtils.getSelectedValuesAsSet(facesContext, uiComponent, converter, uiSelectMany);

        if(uiSelectMany instanceof HtmlSelectManyCheckbox &&
           ((HtmlSelectManyCheckbox)uiSelectMany).isDisplayValueOnly())
        {
            renderCheckboxValueOnly(facesContext,
                                    (HtmlSelectManyCheckbox)uiSelectMany,
                                    itemStrValue,
                                    selectItem.getLabel(),
                                    lookupSet.contains(itemStrValue),
                                    true);
        }
        else
        {
            renderCheckbox(facesContext,
                           uiSelectMany,
                           itemStrValue,
                           selectItem.getLabel(),
                           lookupSet.contains(itemStrValue),
                           true);
        }
    }

    protected void renderCheckboxValueOnly(FacesContext facesContext,
                                           HtmlSelectManyCheckbox checkbox,
                                           String value,
                                           String label,
                                           boolean checked,
                                           boolean renderId)
        throws IOException
    {
        String clientId = checkbox.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SPAN_ELEM, checkbox);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        if (renderId)
        {
            HtmlRendererUtils.writeIdIfNecessary(writer, checkbox, facesContext);
        }

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
        }

//        if ((value != null) && (value.length() > 0))
//        {
//            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
//        }

        HtmlRendererUtils.renderHTMLAttributes(writer, checkbox,
                HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        if(checkbox.getDisplayValueOnlyStyle() != null)
            writer.writeAttribute(HTML.STYLE_ATTR, checkbox.getDisplayValueOnlyStyle(), null);
        if(checkbox.getDisplayValueOnlyStyleClass() != null)
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, checkbox.getDisplayValueOnlyStyleClass(), null);

        String strValue = RendererUtils.getStringValue(facesContext, checkbox);
        writer.writeText(strValue, JSFAttr.VALUE_ATTR);
        //writer.writeText("", null); // close input
        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }
        //writer.endElement(HTML.SPAN_ELEM);
    }

    protected void renderCheckbox(FacesContext facesContext,
                                  HtmlSelectManyCheckbox checkbox,
                                  String value,
                                  String label,
                                  boolean checked,
                                  boolean renderId)
        throws IOException
    {
        String clientId = checkbox.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, checkbox);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        if (renderId)
        {
            HtmlRendererUtils.writeIdIfNecessary(writer, checkbox, facesContext);
        }

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, checkbox,
                HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        if(checkbox.getStyle() != null)
            writer.writeAttribute(HTML.STYLE_ATTR, checkbox.getStyle(), null);
        if(checkbox.getStyleClass() != null)
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, checkbox.getStyleClass(), null);

        if (isDisabled(facesContext, checkbox))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }
        writer.writeText("", null); // close input
        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }
    }

    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!UserRoleUtils.isEnabledOnUserRole(uiComponent))
        {
            return false;
        }
        else
        {
            return super.isDisabled(facesContext, uiComponent);
        }
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        if (uiComponent instanceof HtmlCheckbox)
        {
            //nothing to decode
        }
        else
        {
            super.decode(facesContext, uiComponent);
        }
    }
}
