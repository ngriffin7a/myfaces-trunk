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
package org.apache.myfaces.renderkit.html;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.15  2005/02/08 17:57:23  svieujot
 * Close MYFACES-82 (Use submitted values).
 * Also, use more RendererUtil methods.
 *
 * Revision 1.14  2005/01/22 16:47:17  mmarinschek
 * fixing bug with validation not called if the submitted value is empty; an empty string is submitted instead if the component is enabled.
 *
 * Revision 1.13  2005/01/19 13:34:12  mmarinschek
 * handle case that converter is null
 *
 * Revision 1.12  2005/01/19 13:18:04  mmarinschek
 * better logging of component information
 *
 * Revision 1.11  2005/01/18 22:43:05  svieujot
 * Fix some bugs where converter wasn't used to determine selected values.
 * This caused for examples the list, checkbox and radio based components to bug when the backing bean value type is a primitive.
 *
 * Revision 1.10  2005/01/18 20:49:48  svieujot
 * Set default layout to lineDirection, according to Spec.
 *
 * Revision 1.9  2004/12/23 13:03:09  mmarinschek
 * id's not rendered (or not conditionally rendered); changes in jslistener to support both ie and firefox now
 *
 * Revision 1.8  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.7  2004/10/05 08:49:14  manolito
 * #1038697 h:selectOneRadio generates malformed XHTML
 *
 * Revision 1.6  2004/07/01 22:00:57  mwessendorf
 * ASF switch
 *
 * Revision 1.5  2004/06/04 00:26:16  o_rossmueller
 * modified renderes to comply with JSF 1.1
 *
 * Revision 1.4  2004/05/26 11:10:12  o_rossmueller
 * fix #959926: styleClass support for selectOneRadio, selectOneList, selectManyList
 *
 * Revision 1.3  2004/05/18 14:31:39  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.2  2004/03/31 15:15:58  royalts
 * no message
 *
 * Revision 1.1  2004/03/31 13:26:09  manolito
 * extended radio renderer
 *
 */
public class HtmlRadioRendererBase
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlRadioRendererBase.class);

    private static final String PAGE_DIRECTION = "pageDirection";
    private static final String LINE_DIRECTION = "lineDirection";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UISelectOne.class);

        UISelectOne selectOne = (UISelectOne)uiComponent;

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
                log.error("Wrong layout attribute for component " + selectOne.getClientId(facesContext) + ": " + layout);
            }
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, selectOne);
        HtmlRendererUtils.renderHTMLAttributes(writer, selectOne,
                                               HTML.SELECT_TABLE_PASSTHROUGH_ATTRIBUTES);
        HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);

        if (!pageDirectionLayout) writer.startElement(HTML.TR_ELEM, selectOne);

        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(selectOne);
        try
        {
            converter = RendererUtils.findUIOutputConverter(facesContext, selectOne);
        }
        catch (FacesException e)
        {
            log.error("Error finding Converter for component with id " + uiComponent.getClientId(facesContext));
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
            renderRadio(facesContext,
                        selectOne,
                        itemStrValue,
                        selectItem.getLabel(),
                        currentValueStr.equals(itemStrValue),
                        false);
            writer.endElement(HTML.LABEL_ELEM);
            writer.endElement(HTML.TD_ELEM);
            if (pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        }

        if (!pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }


    protected String getLayout(UIComponent selectOne)
    {
        if (selectOne instanceof HtmlSelectOneRadio)
        {
            return ((HtmlSelectOneRadio)selectOne).getLayout();
        }

        return (String)selectOne.getAttributes().get(JSFAttr.LAYOUT_ATTR);
    }


    protected String getStyleClass(UISelectOne selectOne)
     {
         if (selectOne instanceof HtmlSelectOneRadio)
         {
             return ((HtmlSelectOneRadio)selectOne).getStyleClass();
         }

         return (String)selectOne.getAttributes().get(JSFAttr.STYLE_CLASS_ATTR);
     }


    protected void renderRadio(FacesContext facesContext,
                               UIInput uiComponent,
                               String value,
                               String label,
                               boolean checked, boolean renderId)
            throws IOException
    {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_RADIO, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        if (renderId) {
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        }

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        writer.endElement(HTML.INPUT_ELEM);

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlRadioRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlSelectOneRadio)
        {
            return ((HtmlSelectOneRadio)uiComponent).isDisabled();
        }

        return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UIInput)
        {
            HtmlRendererUtils.decodeUIInput(facesContext, uiComponent);
        }
    }


    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIOutput.class);
        return RendererUtils.getConvertedUIOutputValue(facesContext,
                                                       (UIOutput)uiComponent,
                                                       submittedValue);
    }

}
