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
import javax.faces.model.SelectItemGroup;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
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
                    null, false,isChecked, true); //TODO: the selectBoolean is never disabled
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
            
            renderGroupOrItemCheckbox(facesContext, selectMany, 
            		selectItem, useSubmittedValues, lookupSet, 
            		converter, pageDirectionLayout);
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
    
    protected void renderGroupOrItemCheckbox(FacesContext facesContext,
    		UIComponent uiComponent, SelectItem selectItem,
    		boolean useSubmittedValues, Set lookupSet,
    		Converter converter, boolean pageDirectionLayout) throws IOException{
    	
    	ResponseWriter writer = facesContext.getResponseWriter();
    	
    	boolean isSelectItemGroup = (selectItem instanceof SelectItemGroup);
    	
    	Object itemValue = selectItem.getValue(); // TODO : Check here for getSubmittedValue. Look at RendererUtils.getValue

    	UISelectMany selectMany = (UISelectMany)uiComponent;
    	
        String itemStrValue;
        if (converter == null) {
            itemStrValue = itemValue.toString();
        } else {
            itemStrValue = converter.getAsString(facesContext, selectMany,
                    itemValue);
        }

        if (isSelectItemGroup) {
        	if (pageDirectionLayout)
                writer.startElement(HTML.TR_ELEM, selectMany);
        	
        	writer.startElement(HTML.TD_ELEM, selectMany);
        	writer.write(selectItem.getLabel());
        	writer.endElement(HTML.TD_ELEM);
        	
        	if (pageDirectionLayout) {
	        	writer.endElement(HTML.TR_ELEM);
	        	writer.startElement(HTML.TR_ELEM, selectMany);
        	}
        	writer.startElement(HTML.TD_ELEM, selectMany);
        	
        	writer.startElement(HTML.TABLE_ELEM, selectMany);
        	writer.writeAttribute(HTML.BORDER_ATTR, "0", null);

        	SelectItemGroup group = (SelectItemGroup) selectItem;
        	SelectItem[] selectItems = group.getSelectItems();
        	
        	for (int i=0; i<selectItems.length; i++) {
        		renderGroupOrItemCheckbox(facesContext, selectMany, selectItems[i], useSubmittedValues, lookupSet, converter, pageDirectionLayout);
        	}
        	
        	writer.endElement(HTML.TD_ELEM);
        	writer.endElement(HTML.TR_ELEM);
        	writer.endElement(HTML.TABLE_ELEM);
        	writer.endElement(HTML.TD_ELEM);
        	
        	if (pageDirectionLayout)
                writer.endElement(HTML.TR_ELEM);
        	
        } else {
        
        writer.write("\t\t");
        if (pageDirectionLayout)
            writer.startElement(HTML.TR_ELEM, selectMany);
        writer.startElement(HTML.TD_ELEM, selectMany);
        writer.startElement(HTML.LABEL_ELEM, selectMany);

        boolean checked = (useSubmittedValues && lookupSet
                .contains(itemStrValue))
                || (!useSubmittedValues && lookupSet.contains(itemValue));

        boolean disabled = selectItem.isDisabled();
        
        renderCheckbox(facesContext, selectMany, itemStrValue, selectItem
                .getLabel(), disabled, checked, false);
        writer.endElement(HTML.LABEL_ELEM);
        writer.endElement(HTML.TD_ELEM);
        if (pageDirectionLayout)
            writer.endElement(HTML.TR_ELEM);
        }
    }

    protected void renderCheckbox(FacesContext facesContext,
            UIComponent uiComponent, String value, String label,
            boolean disabled, boolean checked, boolean renderId) throws IOException {
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
        
        if (disabled) {
        	writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
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
            UIComponent component) {
        //TODO: overwrite in extended HtmlCheckboxRenderer and check for
        // enabledOnUserRole
        if (component instanceof HtmlSelectBooleanCheckbox) {
            return ((HtmlSelectBooleanCheckbox) component).isDisabled();
        } else if (component instanceof HtmlSelectManyCheckbox) {
            return ((HtmlSelectManyCheckbox) component).isDisabled();
        } else {
            return RendererUtils.getBooleanAttribute(component,
                    HTML.DISABLED_ATTR, false);
        }
    }

    public void decode(FacesContext facesContext, UIComponent component) {
        RendererUtils.checkParamValidity(facesContext, component, null);
        if (component instanceof UISelectBoolean) {
            HtmlRendererUtils.decodeUISelectBoolean(facesContext, component);
        } else if (component instanceof UISelectMany) {
            HtmlRendererUtils.decodeUISelectMany(facesContext, component);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + component.getClass().getName());
        }
    }

    public Object getConvertedValue(FacesContext facesContext,
            UIComponent component, Object submittedValue)
            throws ConverterException {
        RendererUtils.checkParamValidity(facesContext, component, null);
        if (component instanceof UISelectBoolean) {
            return submittedValue;
        } else if (component instanceof UISelectMany) {
            return RendererUtils.getConvertedUISelectManyValue(facesContext,
                    (UISelectMany) component, submittedValue);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + component.getClass().getName());
        }
    }
}
