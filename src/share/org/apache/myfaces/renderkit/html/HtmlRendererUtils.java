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

import org.apache.myfaces.config.MyfacesConfig;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.renderkit.html.util.JavascriptUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.*;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$ $Log:
 *          HtmlRendererUtils.java,v $ Revision 1.20 2004/10/13 11:51:01 matze
 *          renamed packages to org.apache
 * 
 * Revision 1.19 2004/09/08 09:32:03 manolito MyfacesConfig moved to config
 * package
 * 
 * Revision 1.18 2004/08/09 11:47:09 manolito CSS style support also for non OL
 * or UL layout
 * 
 * Revision 1.17 2004/07/18 22:45:11 o_rossmueller fix #992668: convert values
 * to string for 'selected' comparision
 * 
 * Revision 1.16 2004/07/09 02:26:19 dave0000 cleanup
 * 
 * Revision 1.15 2004/07/01 22:00:57 mwessendorf ASF switch
 * 
 * Revision 1.14 2004/06/21 23:23:37 o_rossmueller fix #976411: removed </input>
 * tag for selectMultiCheckbox fix #972165: also check for readonly and also
 * check for selectMultiCheckbox
 * 
 * Revision 1.13 2004/06/17 00:35:50 o_rossmueller fix #972165: do not reset
 * disabled html checkboxes (the browser does not send a form value for disabled
 * checkboxes even if value=true)
 * 
 * Revision 1.12 2004/06/16 23:51:15 o_rossmueller fix #970747: force separate
 * end tag for empty select list
 * 
 * Revision 1.11 2004/06/03 12:57:03 o_rossmueller modified link renderer to use
 * one hidden field for all links according to 1.1 renderkit docs added
 * onclick=clear_XXX to button
 * 
 * Revision 1.10 2004/05/29 10:19:54 mwessendorf made the class FINAL, because
 * has only one private const
 * 
 * Revision 1.9 2004/05/18 14:31:39 manolito user role support completely moved
 * to components source tree
 * 
 * Revision 1.8 2004/05/03 11:34:27 manolito bug #945118 (Checkbox session
 * state) fixed
 * 
 * Revision 1.7 2004/04/30 09:11:38 manolito no message
 * 
 * Revision 1.6 2004/04/29 19:34:38 o_rossmueller javascript for 'target'
 * attribute handling
 * 
 * Revision 1.5 2004/04/29 14:25:23 manolito javascript function name bugfix
 * 
 * Revision 1.4 2004/04/27 10:32:24 manolito clear hidden inputs javascript
 * function
 * 
 * Revision 1.3 2004/04/06 15:34:12 manolito decode methods must not set
 * submitted value to null
 * 
 * Revision 1.2 2004/04/01 12:43:18 manolito html nesting bug fixed
 * 
 * Revision 1.1 2004/03/29 14:57:00 manolito refactoring for implementation and
 * non-standard component split
 * 
 * Revision 1.17 2004/03/26 13:39:14 manolito added javascript 'return false' to
 * onClick attribute in render link method
 *  
 */
public final class HtmlRendererUtils {
    private static final Log log = LogFactory.getLog(HtmlRendererUtils.class);

    //private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String LINE_SEPARATOR = System.getProperty(
            "line.separator", "\r\n");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

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
            //request parameter not found, nothing to decode
            // we must not reset the submitted value of the component
            // because there could be a submittedValue from former submits!
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
            //request parameter not found,
            // we must not reset the submitted value of the component
            // if there is a submittedValue from a former submit!
            if (((EditableValueHolder) component).getSubmittedValue() == null) {
                // bug #972165: for disabled components the browser does not
                // send a form value, so we have to
                // check this to avoid modification of disabled component
                if (component instanceof HtmlSelectBooleanCheckbox) {
                    if (!(((HtmlSelectBooleanCheckbox) component).isDisabled() || ((HtmlSelectBooleanCheckbox) component)
                            .isReadonly())) {
                        ((EditableValueHolder) component)
                                .setSubmittedValue(Boolean.FALSE);
                    }
                } else {
                    ((EditableValueHolder) component)
                            .setSubmittedValue(Boolean.FALSE);
                }
            }
        }
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
            //request parameter not found,
            // we must not reset the submitted value of the component
            // if there is a submittedValue from a former submit!
            if (((EditableValueHolder) component).getSubmittedValue() == null) {
                // bug #972165: for disabled components the browser does not
                // send a form value, so we have to
                // check this to avoid modification of disabled component
                if (component instanceof HtmlSelectManyCheckbox) {
                    if (!(((HtmlSelectManyCheckbox) component).isDisabled() || ((HtmlSelectManyCheckbox) component)
                            .isReadonly())) {
                        ((EditableValueHolder) component)
                                .setSubmittedValue(EMPTY_STRING_ARRAY);
                    }
                } else {

                    ((EditableValueHolder) component)
                            .setSubmittedValue(EMPTY_STRING_ARRAY);
                }
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
            //request parameter not found, nothing to decode
            // we must not reset the submitted value of the component
            // because there could be a submittedValue from former submits!
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
        writer.writeAttribute(HTML.NAME_ATTR, uiComponent
                .getClientId(facesContext), null);

        List selectItemList;
        Converter converter;
        if (selectMany) {
            writer.writeAttribute(HTML.MULTIPLE_ATTR, "true", null);
            selectItemList = RendererUtils
                    .getSelectItemList((UISelectMany) uiComponent);
            try {
                converter = RendererUtils.findUISelectManyConverter(
                        facesContext, (UISelectMany) uiComponent);
            } catch (FacesException e) {
                log.error("Error finding Converter for component with id "
                        + uiComponent.getClientId(facesContext));
                converter = null;
            }
        } else {
            selectItemList = RendererUtils
                    .getSelectItemList((UISelectOne) uiComponent);
            try {
                converter = RendererUtils.findUIOutputConverter(facesContext,
                        (UISelectOne) uiComponent);
            } catch (FacesException e) {
                log.error("Error finding Converter for component with id "
                        + uiComponent.getClientId(facesContext));
                converter = null;
            }
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

        Set lookupSet;
        boolean useSubmittedValue;
        if (selectMany) {
            UISelectMany uiSelectMany = (UISelectMany) uiComponent;
            lookupSet = RendererUtils.getSubmittedValuesAsSet(uiSelectMany);
            useSubmittedValue = lookupSet != null;

            if (!useSubmittedValue) {
                lookupSet = RendererUtils.getSelectedValuesAsSet(uiSelectMany);
            }
        } else {
            UISelectOne uiSelectOne = (UISelectOne) uiComponent;
            Object lookup = uiSelectOne.getSubmittedValue();
            useSubmittedValue = lookup != null;

            if (!useSubmittedValue) {
                lookup = uiSelectOne.getValue();
            }

            lookupSet = Collections.singleton(lookup);
        }

        renderSelectOptions(facesContext, uiComponent, converter, lookupSet,
                useSubmittedValue, selectItemList);
        // bug #970747: force separate end tag
        writer.writeText("", null);
        writer.endElement(HTML.SELECT_ELEM);
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
     * @param useSubmittedValue
     *            whether we are using the submittedValue
     * @param selectItemList
     *            the <code>List</code> of <code>SelectItem</code> s to be
     *            rendered as HTML option elements.
     * @throws IOException
     */
    private static void renderSelectOptions(FacesContext context,
            UIComponent component, Converter converter, Set lookupSet,
            boolean useSubmittedValue, List selectItemList) throws IOException {
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
                        useSubmittedValue, Arrays.asList(selectItems));
                writer.endElement(HTML.OPTGROUP_ELEM);
            } else {
                Object itemValue = selectItem.getValue();
                String itemStrValue = getItemStringValue(context, component,
                        converter, selectItem);

                writer.write("\t\t");
                writer.startElement(HTML.OPTION_ELEM, null);
                if (itemStrValue != null) {
                    writer.writeAttribute(HTML.VALUE_ATTR, itemStrValue, null);
                }

                if ((useSubmittedValue && lookupSet.contains(itemStrValue))
                        || (!useSubmittedValue && lookupSet.contains(itemValue))) {
                    writer.writeAttribute(HTML.SELECTED_ATTR,
                            HTML.SELECTED_ATTR, null);
                }

                writer.writeText(selectItem.getLabel(), null);

                if (selectItem.isDisabled()) {
                    writer.writeAttribute(HTML.DISABLED_ATTR,
                            HTML.DISABLED_ATTR, null);
                }

                writer.endElement(HTML.OPTION_ELEM);
            }
        }
    }

    private static String getItemStringValue(FacesContext context,
            UIComponent component, Converter converter, SelectItem selectItem) {
        Object itemValue = selectItem.getValue();
        if (converter == null) {
            if (itemValue == null) {
                return "";
            } else if (itemValue instanceof String) {
                return (String) itemValue;
            } else {
                throw new IllegalArgumentException(
                        "Item value of SelectItem with label "
                                + selectItem.getLabel()
                                + " is no String and parent component "
                                + component.getClientId(context)
                                + " does not have a Converter");
            }
        } else {
            return converter.getAsString(context, component, itemValue);
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
        } else {
            return false;
        }
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
        } else {
            return false;
        }
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
        } else {
            //not nested in form, we must add a dummy form at the end of the
            // document
            return DummyFormUtils.DUMMY_FORM_NAME;
        }
    }

    public static String getHiddenCommandLinkFieldName(String formName) {
        return formName + NamingContainer.SEPARATOR_CHAR
                + HIDDEN_COMMANDLINK_FIELD_NAME;
    }

}