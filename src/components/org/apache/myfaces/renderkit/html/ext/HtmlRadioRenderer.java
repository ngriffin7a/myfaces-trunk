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
import org.apache.myfaces.component.html.ext.HtmlSelectOneRadio;
import org.apache.myfaces.custom.radio.HtmlRadio;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRadioRendererBase;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class HtmlRadioRenderer
        extends HtmlRadioRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlRadioRenderer.class);

    private static final String LAYOUT_SPREAD = "spread";

    private static final String PAGE_DIRECTION = "pageDirection";
    private static final String LINE_DIRECTION = "lineDirection";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");

        if (component instanceof HtmlRadio)
        {
            renderRadio(context, (HtmlRadio)component);
        }
        else if (component instanceof UISelectOne)
        {
            String layout = getLayout(component);
            if (layout != null && layout.equals(LAYOUT_SPREAD))
            {
                return; //radio inputs are rendered by spread radio components
            }
            else
            {
                //super.encodeEnd(context, component);
                renderRadio(context, (UISelectOne)component);
            }
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    private void renderRadio(FacesContext facesContext, UISelectOne selectOne) throws IOException
    {
//        RendererUtils.checkParamValidity(facesContext, uiComponent, UISelectOne.class);
        String layout = getLayout(selectOne);

        boolean pageDirectionLayout = false; // Defaults to LINE_DIRECTION
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
            else
            {
                //log.error("Wrong layout attribute for component " + selectOne.getClientId(facesContext) + ": " + layout);
            }
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, selectOne);
        HtmlRendererUtils.renderHTMLAttributes(writer, selectOne,
                                               HTML.SELECT_TABLE_PASSTHROUGH_ATTRIBUTES);
        HtmlRendererUtils.writeIdIfNecessary(writer, selectOne, facesContext);

        if (!pageDirectionLayout) writer.startElement(HTML.TR_ELEM, selectOne);

        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(selectOne);
        try
        {
            converter = RendererUtils.findUIOutputConverter(facesContext, selectOne);
        }
        catch (FacesException e)
        {
            //log.error("Error finding Converter for component with id " + uiComponent.getClientId(facesContext));
            converter = null;
        }

        String currentValueStr = RendererUtils.getStringValue(facesContext, selectOne);

        for (Iterator it = selectItemList.iterator(); it.hasNext(); )
        {
            SelectItem selectItem = (SelectItem)it.next();
            String itemStrValue = RendererUtils.getConvertedStringValue(facesContext, selectOne, converter, selectItem.getValue());

            writer.write("\t\t");
            if (pageDirectionLayout) writer.startElement(HTML.TR_ELEM, selectOne);
            writer.startElement(HTML.TD_ELEM, selectOne);
            writer.startElement(HTML.LABEL_ELEM, selectOne);
            if(selectOne instanceof HtmlSelectOneRadio)
            {
                if(((HtmlSelectOneRadio)selectOne).isDisplayValueOnly())
                    renderRadioValueOnly(facesContext,
                                         (HtmlSelectOneRadio)selectOne,
                                         itemStrValue,
                                         selectItem.getLabel(),
                                         currentValueStr.equals(itemStrValue), false);
                else
                    renderRadio(facesContext,
                                selectOne,
                                itemStrValue,
                                selectItem.getLabel(),
                                currentValueStr.equals(itemStrValue), false);
            }
            else
            {
                renderRadio(facesContext,
                            selectOne,
                            itemStrValue,
                            selectItem.getLabel(),
                            currentValueStr.equals(itemStrValue), false);
            }
            writer.endElement(HTML.LABEL_ELEM);
            writer.endElement(HTML.TD_ELEM);
            if (pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        }
        if (!pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }

    private void renderRadio(FacesContext facesContext, HtmlRadio radio) throws IOException
    {
        String forAttr = radio.getFor();
        if (forAttr == null)
        {
            throw new IllegalStateException("mandatory attribute 'for'");
        }
        int index = radio.getIndex();
        if (index < 0)
        {
            throw new IllegalStateException("positive index must be given");
        }

        UIComponent uiComponent = radio.findComponent(forAttr);
        if (uiComponent == null)
        {
            throw new IllegalStateException("Could not find component '" + forAttr + "' (calling findComponent on component '" + radio.getClientId(facesContext) + "')");
        }
        if (!(uiComponent instanceof UISelectOne))
        {
            throw new IllegalStateException("UISelectOne expected");
        }

        UISelectOne uiSelectOne = (UISelectOne)uiComponent;
        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(uiSelectOne);
        if (index >= selectItemList.size())
        {
            throw new IndexOutOfBoundsException("index " + index + " >= " + selectItemList.size());
        }

        try
        {
            converter = RendererUtils.findUIOutputConverter(facesContext, uiSelectOne);
        }
        catch (FacesException e)
        {
            converter = null;
        }

        Object currentValue = uiSelectOne.getValue();
        SelectItem selectItem = (SelectItem)selectItemList.get(index);
        Object itemValue = selectItem.getValue();
        String itemStrValue;
        if (converter == null)
        {
            itemStrValue = itemValue.toString();
        }
        else
        {
            itemStrValue = converter.getAsString(facesContext, uiSelectOne, itemValue);
        }
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.LABEL_ELEM, uiSelectOne);
        if(uiSelectOne instanceof HtmlSelectOneRadio)
        {
            if(((HtmlSelectOneRadio)uiSelectOne).isDisplayValueOnly())
                renderRadioValueOnly(facesContext,
                                     (HtmlSelectOneRadio)uiSelectOne,
                                     itemStrValue,
                                     selectItem.getLabel(),
                                     currentValue == null && itemValue == null ||
                                     currentValue != null && currentValue.equals(itemValue), false);
            else
                renderRadio(facesContext,
                            uiSelectOne,
                            itemStrValue,
                            selectItem.getLabel(),
                            currentValue == null && itemValue == null ||
                            currentValue != null && currentValue.equals(itemValue), false);
        }
        else
        {
            renderRadio(facesContext,
                        uiSelectOne,
                        itemStrValue,
                        selectItem.getLabel(),
                        currentValue == null && itemValue == null ||
                        currentValue != null && currentValue.equals(itemValue), false);
        }
        writer.endElement(HTML.LABEL_ELEM);
    }



    protected void renderRadioValueOnly(FacesContext facesContext,
                                        HtmlSelectOneRadio radio,
                                        String value,
                                        String label,
                                        boolean checked,
                                        boolean renderId)
        throws IOException
    {
        String clientId = radio.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SPAN_ELEM, radio);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        if (renderId)
        {
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, radio, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        String strValue = RendererUtils.getStringValue(facesContext, radio);
        writer.writeText(strValue, JSFAttr.VALUE_ATTR);
//        if ((value != null) && (value.length() > 0))
//        {
//            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
//        }

        writer.endElement(HTML.SPAN_ELEM);

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
        if (uiComponent instanceof HtmlRadio)
        {
            //nothing to decode
        }
        else
        {
            super.decode(facesContext, uiComponent);
        }
    }

}
