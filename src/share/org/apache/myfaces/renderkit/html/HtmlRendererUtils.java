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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.config.MyfacesConfig;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.renderkit.html.util.JavascriptUtils;
import org.apache.myfaces.component.DisplayValueOnlyCapable;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public final class HtmlRendererUtils {
    private static final Log log = LogFactory.getLog(HtmlRendererUtils.class);

    //private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String LINE_SEPARATOR = System.getProperty(
            "line.separator", "\r\n");

    private static final String HIDDEN_COMMANDLINK_FIELD_NAME = "_link_hidden_";


    private HtmlRendererUtils() {
        // utility class, do not instantiate
    }

    /**
     * X-CHECKED: tlddoc h:inputText
     *
     * @param facesContext
     * @param component
     */
    public static void decodeUIInput(FacesContext facesContext,
            UIComponent component) {
        if (!(component instanceof EditableValueHolder)) {
            throw new IllegalArgumentException("Component "
                    + component.getClientId(facesContext)
                    + " is not an EditableValueHolder");
        }
        Map paramMap = facesContext.getExternalContext()
                .getRequestParameterMap();
        String clientId = component.getClientId(facesContext);
        if (paramMap.containsKey(clientId)) {
            //request parameter found, set submittedValue
            ((EditableValueHolder) component).setSubmittedValue(paramMap
                    .get(clientId));
        } else {
            //request parameter not found, nothing to decode - set submitted value to empty
            //if the component has not been disabled
            if(!isDisabledOrReadOnly(component))
            {
                ((EditableValueHolder) component).setSubmittedValue( RendererUtils.EMPTY_STRING );
            }
        }
    }

    /**
     * X-CHECKED: tlddoc h:selectBooleanCheckbox
     *
     * @param facesContext
     * @param component
     */
    public static void decodeUISelectBoolean(FacesContext facesContext,
            UIComponent component) {
        if (!(component instanceof EditableValueHolder)) {
            throw new IllegalArgumentException("Component "
                    + component.getClientId(facesContext)
                    + " is not an EditableValueHolder");
        }
        Map paramMap = facesContext.getExternalContext()
                .getRequestParameterMap();
        String clientId = component.getClientId(facesContext);
        if (paramMap.containsKey(clientId)) {
            String reqValue = (String) paramMap.get(clientId);
            if (reqValue != null
                    && (reqValue.equalsIgnoreCase("on")
                            || reqValue.equalsIgnoreCase("yes") || reqValue
                            .equalsIgnoreCase("true"))) {
                ((EditableValueHolder) component)
                        .setSubmittedValue(Boolean.TRUE);
            } else {
                ((EditableValueHolder) component)
                        .setSubmittedValue(Boolean.FALSE);
            }
        } else {
            //request parameter not found, nothing to decode - set submitted value to empty
            //if the component has not been disabled
            if(!isDisabledOrReadOnly(component))
            {
                ((EditableValueHolder) component).setSubmittedValue( Boolean.FALSE );
                // Necessary for unchecked chek box
            }
        }
    }

    public static boolean isDisabledOrReadOnly(UIComponent component)
    {
        return (component instanceof DisplayValueOnlyCapable &&
                ((DisplayValueOnlyCapable) component).isDisplayValueOnly()) ||
                isTrue(component.getAttributes().get("disabled")) ||
                    isTrue(component.getAttributes().get("readOnly"));
    }

    private static boolean isTrue(Object obj)
    {
        if(!(obj instanceof Boolean))
            return false;

        return ((Boolean) obj).booleanValue();
    }

    /**
     * X-CHECKED: tlddoc h:selectManyListbox
     *
     * @param facesContext
     * @param component
     */
    public static void decodeUISelectMany(FacesContext facesContext,
            UIComponent component) {
        if (!(component instanceof EditableValueHolder)) {
            throw new IllegalArgumentException("Component "
                    + component.getClientId(facesContext)
                    + " is not an EditableValueHolder");
        }
        Map paramValuesMap = facesContext.getExternalContext()
                .getRequestParameterValuesMap();
        String clientId = component.getClientId(facesContext);
        if (paramValuesMap.containsKey(clientId)) {
            String[] reqValues = (String[]) paramValuesMap.get(clientId);
            ((EditableValueHolder) component).setSubmittedValue(reqValues);
        } else {
            //request parameter not found, nothing to decode - set submitted value to empty
            //if the component has not been disabled
            if(!isDisabledOrReadOnly(component))
            {
                ((EditableValueHolder) component).setSubmittedValue( RendererUtils.EMPTY_STRING );
                // Necessary for combo box / list with no selected item
            }
        }
    }

    /**
     * X-CHECKED: tlddoc h:selectManyListbox
     *
     * @param facesContext
     * @param component
     */
    public static void decodeUISelectOne(FacesContext facesContext,
            UIComponent component) {
        if (!(component instanceof EditableValueHolder)) {
            throw new IllegalArgumentException("Component "
                    + component.getClientId(facesContext)
                    + " is not an EditableValueHolder");
        }
        Map paramMap = facesContext.getExternalContext()
                .getRequestParameterMap();
        String clientId = component.getClientId(facesContext);
        if (paramMap.containsKey(clientId)) {
            //request parameter found, set submitted value
            ((EditableValueHolder) component).setSubmittedValue(paramMap
                    .get(clientId));
        } else {
            //request parameter not found, nothing to decode - set submitted value to empty
            //if the component has not been disabled

            if(!isDisabledOrReadOnly(component))
            {
                ((EditableValueHolder) component).setSubmittedValue( RendererUtils.EMPTY_STRING );
                // Necessary for list with no selected item
            }
        }
    }

    /*
     * public static void renderCheckbox(FacesContext facesContext, UIComponent
     * uiComponent, String value, String label, boolean checked) throws
     * IOException { String clientId = uiComponent.getClientId(facesContext);
     *
     * ResponseWriter writer = facesContext.getResponseWriter();
     *
     * writer.startElement(HTML.INPUT_ELEM, uiComponent);
     * writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX, null);
     * writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
     * writer.writeAttribute(HTML.ID_ATTR, clientId, null);
     *
     * if (checked) { writer.writeAttribute(HTML.CHECKED_ATTR,
     * HTML.CHECKED_ATTR, null); }
     *
     * if ((value != null) && (value.length() > 0)) {
     * writer.writeAttribute(HTML.VALUE_ATTR, value, null); }
     *
     * renderHTMLAttributes(writer, uiComponent,
     * HTML.INPUT_PASSTHROUGH_ATTRIBUTES); renderDisabledOnUserRole(writer,
     * uiComponent, facesContext);
     *
     * if ((label != null) && (label.length() > 0)) {
     * writer.write(HTML.NBSP_ENTITY); writer.writeText(label, null); }
     *
     * writer.endElement(HTML.INPUT_ELEM); }
     */

    public static void renderListbox(FacesContext facesContext,
            UISelectOne selectOne, boolean disabled, int size)
            throws IOException {
        internalRenderSelect(facesContext, selectOne, disabled, size, false);
    }

    public static void renderListbox(FacesContext facesContext,
            UISelectMany selectMany, boolean disabled, int size)
            throws IOException {
        internalRenderSelect(facesContext, selectMany, disabled, size, true);
    }

    public static void renderMenu(FacesContext facesContext,
            UISelectOne selectOne, boolean disabled) throws IOException {
        internalRenderSelect(facesContext, selectOne, disabled, 1, false);
    }

    public static void renderMenu(FacesContext facesContext,
            UISelectMany selectMany, boolean disabled) throws IOException {
        internalRenderSelect(facesContext, selectMany, disabled, 1, true);
    }

    private static void internalRenderSelect(FacesContext facesContext,
            UIComponent uiComponent, boolean disabled, int size,
            boolean selectMany) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SELECT_ELEM, uiComponent);
        HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
        writer.writeAttribute(HTML.NAME_ATTR, uiComponent
                .getClientId(facesContext), null);

        List selectItemList;
        Converter converter;
        if (selectMany) {
            writer.writeAttribute(HTML.MULTIPLE_ATTR, "true", null);
            selectItemList = RendererUtils
                    .getSelectItemList((UISelectMany) uiComponent);
            converter = findUISelectManyConverterFailsafe(facesContext, uiComponent);
        } else {
            selectItemList = RendererUtils
                    .getSelectItemList((UISelectOne) uiComponent);
            converter = findUIOutputConverterFailSafe(facesContext, uiComponent);
        }

        if (size == 0) {
            //No size given (Listbox) --> size is number of select items
            writer.writeAttribute(HTML.SIZE_ATTR, Integer
                    .toString(selectItemList.size()), null);
        } else {
            writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(size), null);
        }
        renderHTMLAttributes(writer, uiComponent,
                HTML.SELECT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (disabled) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        Set lookupSet = getSubmittedOrSelectedValuesAsSet(selectMany, uiComponent, facesContext, converter);

        renderSelectOptions(facesContext, uiComponent, converter, lookupSet,
                selectItemList);
        // bug #970747: force separate end tag
        writer.writeText("", null);
        writer.endElement(HTML.SELECT_ELEM);
    }

    public static Set getSubmittedOrSelectedValuesAsSet(boolean selectMany, UIComponent uiComponent, FacesContext facesContext, Converter converter) {
        Set lookupSet;

        if (selectMany) {
            UISelectMany uiSelectMany = (UISelectMany) uiComponent;
            lookupSet = RendererUtils.getSubmittedValuesAsSet(facesContext, uiComponent, converter, uiSelectMany);
            if (lookupSet == null)
            {
                lookupSet = RendererUtils.getSelectedValuesAsSet(facesContext, uiComponent, converter, uiSelectMany);
            }
        } else {
            UISelectOne uiSelectOne = (UISelectOne) uiComponent;
            Object lookup = uiSelectOne.getSubmittedValue();
            if (lookup == null)
            {
                lookup = uiSelectOne.getValue();
            }
            String lookupString = RendererUtils.getConvertedStringValue(facesContext, uiComponent, converter, lookup);
            lookupSet = Collections.singleton(lookupString);
        }
        return lookupSet;
    }

    public static Converter findUISelectManyConverterFailsafe(FacesContext facesContext, UIComponent uiComponent) {
        Converter converter;
        try {
            converter = RendererUtils.findUISelectManyConverter(
                    facesContext, (UISelectMany) uiComponent);
        } catch (FacesException e) {
            log.error("Error finding Converter for component with id "
                    + uiComponent.getClientId(facesContext));
            converter = null;
        }
        return converter;
    }

    public static Converter findUIOutputConverterFailSafe(FacesContext facesContext, UIComponent uiComponent) {
        Converter converter;
        try {
            converter = RendererUtils.findUIOutputConverter(facesContext,
                    (UISelectOne) uiComponent);
        } catch (FacesException e) {
            log.error("Error finding Converter for component with id "
                    + uiComponent.getClientId(facesContext));
            converter = null;
        }
        return converter;
    }

    /**
     * Renders the select options for a <code>UIComponent</code> that is
     * rendered as an HTML select element.
     *
     * @param context
     *            the current <code>FacesContext</code>.
     * @param component
     *            the <code>UIComponent</code> whose options need to be
     *            rendered.
     * @param converter
     *            <code>component</code>'s converter
     * @param lookupSet
     *            the <code>Set</code> to use to look up selected options
     * @param selectItemList
     *            the <code>List</code> of <code>SelectItem</code> s to be
     *            rendered as HTML option elements.
     * @throws IOException
     */
    private static void renderSelectOptions(FacesContext context,
                                            UIComponent component, Converter converter, Set lookupSet,
                                            List selectItemList) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        for (Iterator it = selectItemList.iterator(); it.hasNext();) {
            SelectItem selectItem = (SelectItem) it.next();

            if (selectItem instanceof SelectItemGroup) {
                writer.startElement(HTML.OPTGROUP_ELEM, null);
                writer.writeAttribute(HTML.LABEL_ATTR, selectItem.getLabel(),
                        null);
                SelectItem[] selectItems = ((SelectItemGroup) selectItem)
                        .getSelectItems();
                renderSelectOptions(context, component, converter, lookupSet,
                        Arrays.asList(selectItems));
                writer.endElement(HTML.OPTGROUP_ELEM);
            } else {
                String itemStrValue = RendererUtils.getConvertedStringValue(context, component,
                        converter, selectItem);

                writer.write("\t\t");
                writer.startElement(HTML.OPTION_ELEM, null);
                if (itemStrValue != null) {
                    writer.writeAttribute(HTML.VALUE_ATTR, itemStrValue, null);
                }

                if (lookupSet.contains(itemStrValue)) {  //TODO/FIX: we always compare the String vales, better fill lookupSet with Strings only when useSubmittedValue==true, else use the real item value Objects
                    writer.writeAttribute(HTML.SELECTED_ATTR,
                            HTML.SELECTED_ATTR, null);
                }

                if (selectItem.isDisabled()) {
                    writer.writeAttribute(HTML.DISABLED_ATTR,
                            HTML.DISABLED_ATTR, null);
                }

                writer.writeText(selectItem.getLabel(), null);

                writer.endElement(HTML.OPTION_ELEM);
            }
        }
    }

    /*
     * public static void renderRadio(FacesContext facesContext, UIInput
     * uiComponent, String value, String label, boolean checked) throws
     * IOException { String clientId = uiComponent.getClientId(facesContext);
     *
     * ResponseWriter writer = facesContext.getResponseWriter();
     *
     * writer.startElement(HTML.INPUT_ELEM, uiComponent);
     * writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_RADIO, null);
     * writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
     * writer.writeAttribute(HTML.ID_ATTR, clientId, null);
     *
     * if (checked) { writer.writeAttribute(HTML.CHECKED_ATTR,
     * HTML.CHECKED_ATTR, null); }
     *
     * if ((value != null) && (value.length() > 0)) {
     * writer.writeAttribute(HTML.VALUE_ATTR, value, null); }
     *
     * renderHTMLAttributes(writer, uiComponent,
     * HTML.INPUT_PASSTHROUGH_ATTRIBUTES); renderDisabledOnUserRole(writer,
     * uiComponent, facesContext);
     *
     * if ((label != null) && (label.length() > 0)) {
     * writer.write(HTML.NBSP_ENTITY); writer.writeText(label, null); }
     *
     * writer.endElement(HTML.INPUT_ELEM); }
     */

    public static void writePrettyLineSeparator(FacesContext facesContext)
            throws IOException {
        if (MyfacesConfig.getCurrentInstance(facesContext.getExternalContext())
                .isPrettyHtml()) {
            facesContext.getResponseWriter().write(LINE_SEPARATOR);
        }
    }

    public static void writePrettyIndent(FacesContext facesContext)
            throws IOException {
        if (MyfacesConfig.getCurrentInstance(facesContext.getExternalContext())
                .isPrettyHtml()) {
            facesContext.getResponseWriter().write('\t');
        }
    }

    /**
     * @return true, if the attribute was written
     * @throws java.io.IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
            String componentProperty, String attrName, Object value)
            throws IOException {
        if (!RendererUtils.isDefaultAttributeValue(value)) {
            // render JSF "styleClass" attribute as "class"
            String htmlAttrName = attrName.equals(HTML.STYLE_CLASS_ATTR) ? HTML.CLASS_ATTR
                    : attrName;
            writer.writeAttribute(htmlAttrName, value, componentProperty);
            return true;
        }

        return false;
    }

    /**
     * @return true, if the attribute was written
     * @throws java.io.IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
            UIComponent component, String componentProperty, String htmlAttrName)
            throws IOException {
        Object value = component.getAttributes().get(componentProperty);
        return renderHTMLAttribute(writer, componentProperty, htmlAttrName,
                value);
    }

    /**
     * @return true, if an attribute was written
     * @throws java.io.IOException
     */
    public static boolean renderHTMLAttributes(ResponseWriter writer,
            UIComponent component, String[] attributes) throws IOException {
        boolean somethingDone = false;
        for (int i = 0, len = attributes.length; i < len; i++) {
            String attrName = attributes[i];
            if (renderHTMLAttribute(writer, component, attrName, attrName)) {
                somethingDone = true;
            }
        }
        return somethingDone;
    }

    public static boolean renderHTMLAttributeWithOptionalStartElement(
            ResponseWriter writer, UIComponent component, String elementName,
            String attrName, Object value, boolean startElementWritten)
            throws IOException {
        if (!RendererUtils.isDefaultAttributeValue(value)) {
            if (!startElementWritten) {
                writer.startElement(elementName, component);
                startElementWritten = true;
            }
            renderHTMLAttribute(writer, attrName, attrName, value);
        }
        return startElementWritten;
    }

    public static boolean renderHTMLAttributesWithOptionalStartElement(
            ResponseWriter writer, UIComponent component, String elementName,
            String[] attributes) throws IOException {
        boolean startElementWritten = false;
        for (int i = 0, len = attributes.length; i < len; i++) {
            String attrName = attributes[i];
            Object value = component.getAttributes().get(attrName);
            if (!RendererUtils.isDefaultAttributeValue(value)) {
                if (!startElementWritten) {
                    writer.startElement(elementName, component);
                    startElementWritten = true;
                }
                renderHTMLAttribute(writer, attrName, attrName, value);
            }
        }
        return startElementWritten;
    }

    public static boolean renderOptionalEndElement(ResponseWriter writer,
            UIComponent component, String elementName, String[] attributes)
            throws IOException {
        boolean endElementNeeded = false;
        for (int i = 0, len = attributes.length; i < len; i++) {
            String attrName = attributes[i];
            Object value = component.getAttributes().get(attrName);
            if (!RendererUtils.isDefaultAttributeValue(value)) {
                endElementNeeded = true;
                break;
            }
        }
        if (endElementNeeded) {
            writer.endElement(elementName);
            return true;
        }

        return false;
    }

    public static void writeIdIfNecessary(ResponseWriter writer, UIComponent component,
                                          FacesContext facesContext)
        throws IOException
    {
        if(component.getId()!=null && !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
        {
            writer.writeAttribute(HTML.ID_ATTR, component.getClientId(facesContext),null);
        }
    }

    public static void renderDisplayValueOnlyForSelects(FacesContext facesContext, UIComponent
            uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        List selectItemList;
        Converter converter;
        if (uiComponent instanceof UISelectMany) {
            selectItemList = RendererUtils
                    .getSelectItemList((UISelectMany) uiComponent);
            converter = findUISelectManyConverterFailsafe(facesContext, uiComponent);
        } else {
            selectItemList = RendererUtils
                    .getSelectItemList((UISelectOne) uiComponent);
            converter = findUIOutputConverterFailSafe(facesContext, uiComponent);
        }

        writer.startElement(HTML.UL_ELEM, uiComponent);
        writeIdIfNecessary(writer, uiComponent, facesContext);
        writer.writeAttribute(HTML.NAME_ATTR, uiComponent
                .getClientId(facesContext), null);

        renderDisplayValueOnlyAttributes(uiComponent, writer);


        Set lookupSet = getSubmittedOrSelectedValuesAsSet(
                uiComponent instanceof UISelectMany,
                uiComponent, facesContext, converter);

        renderSelectOptionsAsText(facesContext, uiComponent, converter, lookupSet,
                selectItemList);

        // bug #970747: force separate end tag
        writer.writeText("", null);
        writer.endElement(HTML.UL_ELEM);

    }

    public static void renderDisplayValueOnlyAttributes(UIComponent uiComponent, ResponseWriter writer) throws IOException {
        if(!(uiComponent instanceof DisplayValueOnlyCapable))
        {
            log.error("Wrong type of uiComponent. needs DisplayValueOnlyCapable.");
            renderHTMLAttributes(writer, uiComponent,
                    HTML.COMMON_PASSTROUGH_ATTRIBUTES);

            return;
        }

        DisplayValueOnlyCapable displayOnlyCapable = (DisplayValueOnlyCapable)
            uiComponent;


        if(getDisplayValueOnlyStyle(uiComponent) != null || getDisplayValueOnlyStyleClass(uiComponent)!=null)
        {
            if(getDisplayValueOnlyStyle(uiComponent) != null )
            {
                writer.writeAttribute(HTML.STYLE_ATTR, getDisplayValueOnlyStyle(uiComponent), null);
            }
            else if(uiComponent.getAttributes().get("style")!=null)
            {
                writer.writeAttribute(HTML.STYLE_ATTR, uiComponent.getAttributes().get("style"), null);
            }
            
            if(getDisplayValueOnlyStyleClass(uiComponent) != null )
            {
                writer.writeAttribute(HTML.STYLE_CLASS_ATTR, getDisplayValueOnlyStyleClass(uiComponent), null);
            }
            else if(uiComponent.getAttributes().get("styleClass")!=null)
            {
                writer.writeAttribute(HTML.STYLE_CLASS_ATTR, uiComponent.getAttributes().get("styleClass"), null);
            }

            renderHTMLAttributes(writer, uiComponent,
                    HTML.COMMON_PASSTROUGH_ATTRIBUTES_WITHOUT_STYLE);
        }
        else
        {
            renderHTMLAttributes(writer, uiComponent,
                    HTML.COMMON_PASSTROUGH_ATTRIBUTES);
        }
    }

    private static void renderSelectOptionsAsText(FacesContext context,
                                            UIComponent component, Converter converter, Set lookupSet,
                                            List selectItemList) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        for (Iterator it = selectItemList.iterator(); it.hasNext();) {
            SelectItem selectItem = (SelectItem) it.next();

            if (selectItem instanceof SelectItemGroup) {
                SelectItem[] selectItems = ((SelectItemGroup) selectItem)
                        .getSelectItems();
                renderSelectOptionsAsText(context, component, converter, lookupSet,
                        Arrays.asList(selectItems));
            } else {
                String itemStrValue = RendererUtils.getConvertedStringValue(context, component,
                        converter, selectItem);

                if (lookupSet.contains(itemStrValue)) {  //TODO/FIX: we always compare the String vales, better fill lookupSet with Strings only when useSubmittedValue==true, else use the real item value Objects

                    writer.startElement(HTML.LI_ELEM, null);
                    writer.writeText(selectItem.getLabel(), null);
                    writer.endElement(HTML.LI_ELEM);
                }
            }
        }
    }

    public static String getDisplayValueOnlyStyleClass(UIComponent component) {

        if(component instanceof DisplayValueOnlyCapable)
        {
            if(((DisplayValueOnlyCapable) component).getDisplayValueOnlyStyleClass()!=null)
                return ((DisplayValueOnlyCapable) component).getDisplayValueOnlyStyleClass();

            UIComponent parent=component;

            while((parent = parent.getParent())!=null)
            {
                if(parent instanceof DisplayValueOnlyCapable)
                {
                    return ((DisplayValueOnlyCapable) parent).getDisplayValueOnlyStyleClass();
                }
            }
        }

        return null;
    }

    public static String getDisplayValueOnlyStyle(UIComponent component) {

        if(component instanceof DisplayValueOnlyCapable)
        {
            if(((DisplayValueOnlyCapable) component).getDisplayValueOnlyStyle()!=null)
                return ((DisplayValueOnlyCapable) component).getDisplayValueOnlyStyle();

            UIComponent parent=component;

            while((parent = parent.getParent())!=null)
            {
                if(parent instanceof DisplayValueOnlyCapable)
                {
                    return ((DisplayValueOnlyCapable) parent).getDisplayValueOnlyStyle();
                }
            }
        }

        return null;
    }

    public static boolean isDisplayValueOnly(UIComponent component) {

        if(component instanceof DisplayValueOnlyCapable)
        {
            
            UIComponent parent=component;

            while((parent = parent.getParent())!=null)
            {
                if(parent instanceof DisplayValueOnlyCapable)
                {
                    return ((DisplayValueOnlyCapable) parent).isDisplayValueOnly();
                }
            }
        }

        return (component instanceof DisplayValueOnlyCapable) &&
                        ((DisplayValueOnlyCapable) component).isDisplayValueOnly();
    }

    public static void renderDisplayValueOnly(FacesContext facesContext, UIInput input) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.SPAN_ELEM, input);

        writeIdIfNecessary(writer, input, facesContext);

        renderDisplayValueOnlyAttributes(input, writer);

        String strValue = RendererUtils.getStringValue(facesContext, input);
        writer.writeText(strValue, JSFAttr.VALUE_ATTR);

        writer.endElement(HTML.SPAN_ELEM);
    }

    public static class LinkParameter {
        private String _name;

        private Object _value;

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }

        public Object getValue() {
            return _value;
        }

        public void setValue(Object value) {
            _value = value;
        }

    }

    public static void renderHiddenCommandFormParams(ResponseWriter writer,
            Set dummyFormParams) throws IOException {
        for (Iterator it = dummyFormParams.iterator(); it.hasNext();) {
            writer.startElement(HTML.INPUT_ELEM, null);
            writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
            writer.writeAttribute(HTML.NAME_ATTR, it.next(), null);
            writer.endElement(HTML.INPUT_ELEM);
        }
    }

    /**
     * Render the javascript function that is called on a click on a commandLink
     * to clear the hidden inputs. This is necessary because on a browser back,
     * each hidden input still has it's old value (browser cache!) and therefore
     * a new submit would cause the according action once more!
     *
     * @param writer
     * @param formName
     * @param dummyFormParams
     * @param formTarget
     * @throws IOException
     */
    public static void renderClearHiddenCommandFormParamsFunction(
            ResponseWriter writer, String formName, Set dummyFormParams,
            String formTarget) throws IOException {
        //render the clear hidden inputs javascript function
        String functionName = getClearHiddenCommandFormParamsFunctionName(formName);
        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
        writer.write("\n<!--");
        writer.write("\nfunction ");
        writer.write(functionName);
        writer.write("() {");
        if (dummyFormParams != null) {
            writer.write("\n  var f = document.forms['");
            writer.write(formName);
            writer.write("'];");
            for (Iterator it = dummyFormParams.iterator(); it.hasNext();) {
                writer.write("\n  f.elements['");
                writer.write((String) it.next());
                writer.write("'].value=null;");
            }
        }
        // clear form target
        writer.write("\n  f.target=");
        if (formTarget == null || formTarget.length() == 0) {
            //Normally one would think that setting target to null has the
            //desired effect, but once again IE is different...
            //Setting target to null causes IE to open a new window!
            writer.write("'';");
        } else {
            writer.write("'");
            writer.write(formTarget);
            writer.write("';");
        }
        writer.write("\n}");

        //Just to be sure we call this clear method on each load.
        //Otherwise in the case, that someone submits a form by pressing Enter
        //within a text input, the hidden inputs won't be cleared!
        writer.write("\n");
        writer.write(functionName);
        writer.write("();");

        writer.write("\n//-->\n");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * Prefixes the given String with "clear_" and removes special characters
     *
     * @param formName
     * @return
     */
    public static String getClearHiddenCommandFormParamsFunctionName(
            String formName) {
        return "clear_"
                + JavascriptUtils.getValidJavascriptName(formName, false);
    }

    public static String getFormName(UIComponent component, FacesContext context) {
        //Find form
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIForm)) {
            parent = parent.getParent();
        }

        if (parent != null) {
            //link is nested inside a form
            return ((UIForm) parent).getClientId(context);
        }
        //not nested in form, we must add a dummy form at the end of the
        // document
        return DummyFormUtils.DUMMY_FORM_NAME;
    }

    public static String getHiddenCommandLinkFieldName(String formName) {
        return formName + NamingContainer.SEPARATOR_CHAR
                + HIDDEN_COMMANDLINK_FIELD_NAME;
    }

}