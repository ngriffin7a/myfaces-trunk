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
import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;
import org.apache.myfaces.renderkit.html.HtmlMenuRendererBase;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.FacesException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.*;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlMenuRenderer
        extends HtmlMenuRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlMenuRenderer.class);
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

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        if(component instanceof HtmlSelectOneMenu)
        {
            internalRenderSelect(facesContext, component, isDisabled(facesContext, component), 1, false);
        }
        else
        {
            super.encodeEnd(facesContext, component);
        }
    }

    private static void internalRenderSelect(FacesContext facesContext,
            UIComponent uiComponent, boolean disabled, int size,
            boolean selectMany) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SELECT_ELEM, uiComponent);
        HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
        writer.writeAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext), null);

        List selectItemList;
        Converter converter;
        if (selectMany) {
            writer.writeAttribute(HTML.MULTIPLE_ATTR, "true", null);
            selectItemList = RendererUtils.getSelectItemList((UISelectMany) uiComponent);
            try {
                converter = RendererUtils.findUISelectManyConverter(
                        facesContext, (UISelectMany) uiComponent);
            } catch (FacesException e) {
//                log.error("Error finding Converter for component with id "
//                        + uiComponent.getClientId(facesContext));
                converter = null;
            }
        } else {
            selectItemList = RendererUtils
                    .getSelectItemList((UISelectOne) uiComponent);
            try {
                converter = RendererUtils.findUIOutputConverter(facesContext,
                        (UISelectOne) uiComponent);
            } catch (FacesException e) {
//                log.error("Error finding Converter for component with id "
//                        + uiComponent.getClientId(facesContext));
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
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                HTML.SELECT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (disabled) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        Set lookupSet;
        boolean useSubmittedValue;
        if (selectMany) {
            UISelectMany uiSelectMany = (UISelectMany) uiComponent;
            lookupSet = RendererUtils.getSubmittedValuesAsSet(facesContext, uiComponent, converter, uiSelectMany);
            if (lookupSet == null)
            {
                useSubmittedValue = false;
                lookupSet = RendererUtils.getSelectedValuesAsSet(facesContext, uiComponent, converter, uiSelectMany);
            }
            else
            {
                useSubmittedValue = true;
            }
        } else {
            UISelectOne uiSelectOne = (UISelectOne) uiComponent;
            Object lookup = uiSelectOne.getSubmittedValue();
            if (lookup == null)
            {
                useSubmittedValue = false;
                lookup = uiSelectOne.getValue();
            }
            else
            {
                useSubmittedValue = true;
            }
            String lookupString = RendererUtils.getConvertedStringValue(facesContext, uiComponent, converter, lookup);
            lookupSet = Collections.singleton(lookupString);
        }

        if(uiComponent instanceof HtmlSelectOneMenu)
        {
            if(((HtmlSelectOneMenu)uiComponent).isDisplayValueOnly())
                renderSelectOptions(facesContext, (HtmlSelectOneMenu)uiComponent, converter, lookupSet,
                                    useSubmittedValue, selectItemList);
            else
                renderSelectOptionsValueOnly(facesContext, (HtmlSelectOneMenu)uiComponent,
                                             converter, lookupSet,
                                             useSubmittedValue, selectItemList);
        }
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
     * @param menu
     *            the <code>menu</code> whose options need to be
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
            HtmlSelectOneMenu menu, Converter converter, Set lookupSet,
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
                renderSelectOptions(context, menu, converter, lookupSet,
                        useSubmittedValue, Arrays.asList(selectItems));
                writer.endElement(HTML.OPTGROUP_ELEM);
            } else {
                String itemStrValue = RendererUtils.getConvertedStringValue(context, menu,
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

    private static void renderSelectOptionsValueOnly(FacesContext context,
            HtmlSelectOneMenu menu, Converter converter, Set lookupSet,
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
                renderSelectOptions(context, menu, converter, lookupSet,
                        useSubmittedValue, Arrays.asList(selectItems));
                writer.endElement(HTML.OPTGROUP_ELEM);
            } else {
                String itemStrValue = RendererUtils.getConvertedStringValue(context, menu,
                        converter, selectItem);

                writer.write("\t\t");
                writer.startElement(HTML.SPAN_ELEM, null);
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

                writer.endElement(HTML.SPAN_ELEM);
            }
        }
    }
}
