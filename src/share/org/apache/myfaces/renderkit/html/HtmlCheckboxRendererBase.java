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

import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.15  2005/02/08 18:01:01  svieujot
 * Typo.
 *
 * Revision 1.14  2005/01/18 22:43:05  svieujot
 * Fix some bugs where converter wasn't used to determine selected values.
 * This caused for examples the list, checkbox and radio based components to bug when the backing bean value type is a primitive.
 *
 * Revision 1.13  2005/01/07 01:54:36  svieujot
 * radioRenderer wasn't looking at the submitted value.
 *
 * Revision 1.9 2004/10/13 11:51:01 matze renamed packages to org.apache
 * 
 * Revision 1.8 2004/07/01 22:00:56 mwessendorf ASF switch
 * 
 * Revision 1.7 2004/06/21 23:23:37 o_rossmueller fix #976411: removed </input>
 * tag for selectMultiCheckbox fix #972165: also check for readonly and also
 * check for selectMultiCheckbox
 * 
 * Revision 1.6 2004/06/04 00:26:16 o_rossmueller modified renderes to comply
 * with JSF 1.1
 * 
 * Revision 1.5 2004/05/18 14:31:39 manolito user role support completely moved
 * to components source tree
 * 
 * Revision 1.4 2004/05/05 18:03:00 o_rossmueller fix #948110: render span
 * element for styleClass
 * 
 * Revision 1.3 2004/04/02 13:57:13 manolito extended HtmlSelectManyCheckbox
 * with layout "spread" and custom Checkbox component
 * 
 * Revision 1.2 2004/04/02 13:37:49 manolito no message
 *  
 */
public class HtmlCheckboxRendererBase extends HtmlRenderer {
    private static final Log log = LogFactory
            .getLog(HtmlCheckboxRendererBase.class);

    private static final String PAGE_DIRECTION = "pageDirection";

    private static final String LINE_DIRECTION = "lineDirection";

    private static final String EXTERNAL_TRUE_VALUE = "true";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean) {
            Boolean value = RendererUtils.getBooleanValue( uiComponent );
            boolean isChecked = value != null ? value.booleanValue() : false;
            renderCheckbox(facesContext, uiComponent, EXTERNAL_TRUE_VALUE,
                    null, isChecked, true);
        } else if (uiComponent instanceof UISelectMany) {
            renderCheckboxList(facesContext, (UISelectMany) uiComponent);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + uiComponent.getClass().getName());
        }
    }

    public void renderCheckboxList(FacesContext facesContext,
            UISelectMany selectMany) throws IOException {

        String layout = getLayout(selectMany);
        boolean pageDirectionLayout = false; //Default to lineDirection
        if (layout != null) {
            if (layout.equals(PAGE_DIRECTION)) {
                pageDirectionLayout = true;
            } else if (layout.equals(LINE_DIRECTION)) {
                pageDirectionLayout = false;
            } else {
                log.error("Wrong layout attribute for component "
                        + selectMany.getClientId(facesContext) + ": " + layout);
            }
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, selectMany);
        HtmlRendererUtils.renderHTMLAttributes(writer, selectMany,
                HTML.SELECT_TABLE_PASSTHROUGH_ATTRIBUTES);
        HtmlRendererUtils.writeIdIfNecessary(writer, selectMany, facesContext);

        if (!pageDirectionLayout)
            writer.startElement(HTML.TR_ELEM, selectMany);

        Converter converter;
        try {
            converter = RendererUtils.findUISelectManyConverter(facesContext,
                    selectMany);
        } catch (FacesException e) {
            log.error("Error finding Converter for component with id "
                    + selectMany.getClientId(facesContext));
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

            renderCheckbox(facesContext, selectMany, itemStrValue, selectItem
                    .getLabel(), checked, false);
            writer.endElement(HTML.LABEL_ELEM);
            writer.endElement(HTML.TD_ELEM);
            if (pageDirectionLayout)
                writer.endElement(HTML.TR_ELEM);
        }

        if (!pageDirectionLayout)
            writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }

    protected String getLayout(UISelectMany selectMany) {
        if (selectMany instanceof HtmlSelectManyCheckbox) {
            return ((HtmlSelectManyCheckbox) selectMany).getLayout();
        } else {
            return (String) selectMany.getAttributes().get(JSFAttr.LAYOUT_ATTR);
        }
    }

    protected void renderCheckbox(FacesContext facesContext,
            UIComponent uiComponent, String value, String label,
            boolean checked, boolean renderId) throws IOException {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        if (renderId) {
            HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
        }

        if (checked) {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
        }

        if ((value != null) && (value.length() > 0)) {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, uiComponent)) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }
        writer.writeText("", null); // close input
        if ((label != null) && (label.length() > 0)) {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }
    }

    protected boolean isDisabled(FacesContext facesContext,
            UIComponent uiComponent) {
        //TODO: overwrite in extended HtmlCheckboxRenderer and check for
        // enabledOnUserRole
        if (uiComponent instanceof HtmlSelectBooleanCheckbox) {
            return ((HtmlSelectBooleanCheckbox) uiComponent).isDisabled();
        } else if (uiComponent instanceof HtmlSelectManyCheckbox) {
            return ((HtmlSelectManyCheckbox) uiComponent).isDisabled();
        } else {
            return RendererUtils.getBooleanAttribute(uiComponent,
                    HTML.DISABLED_ATTR, false);
        }
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean) {
            HtmlRendererUtils.decodeUISelectBoolean(facesContext, uiComponent);
        } else if (uiComponent instanceof UISelectMany) {
            HtmlRendererUtils.decodeUISelectMany(facesContext, uiComponent);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + uiComponent.getClass().getName());
        }
    }

    public Object getConvertedValue(FacesContext facesContext,
            UIComponent uiComponent, Object submittedValue)
            throws ConverterException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean) {
            return submittedValue;
        } else if (uiComponent instanceof UISelectMany) {
            return RendererUtils.getConvertedUISelectManyValue(facesContext,
                    (UISelectMany) uiComponent, submittedValue);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + uiComponent.getClass().getName());
        }
    }
}