/*
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

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.convert.impl.StringArrayConverter;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormResponseWriter;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.*;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.net.URLEncoder;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlRendererUtils
{
    private static final Log log = LogFactory.getLog(HtmlRendererUtils.class);

    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\r\n");

    private HtmlRendererUtils() {} //no instance allowed

    /**
     * @param facesContext
     * @param input
     */
    public static void decodeUIInput(FacesContext facesContext,
                                     UIInput input)
    {
        Map paramValuesMap = facesContext.getExternalContext().getRequestParameterValuesMap();
        String clientId  = input.getClientId(facesContext);
        if (paramValuesMap.containsKey(clientId))
        {
            String[] reqValues = (String[])paramValuesMap.get(clientId);
            input.setSubmittedValue(StringArrayConverter.getAsString(reqValues, false));
            //input.setValid(true);
        }
        else
        {
            //request parameter not found, nothing to decode
            input.setSubmittedValue(null);
        }
    }


    public static void decodeUISelectBoolean(FacesContext facesContext,
                                             UISelectBoolean selectBoolean,
                                             boolean setFalseOnAbsentParam,
                                             String externalTrueValue)
    {
        Map paramValuesMap = facesContext.getExternalContext().getRequestParameterValuesMap();
        String clientId  = selectBoolean.getClientId(facesContext);
        if (!paramValuesMap.containsKey(clientId))
        {
            //request parameter not found
            if (setFalseOnAbsentParam)
            {
                selectBoolean.setSelected(false);  //no request param means false
            }
            return;
        }

        String[] reqValues = (String[])paramValuesMap.get(clientId);
        if (reqValues.length > 0 && reqValues[0].equals(externalTrueValue))
        {
            selectBoolean.setSelected(true);
        }
        else
        {
            selectBoolean.setSelected(false);
        }
    }


    public static void decodeUISelectMany(FacesContext facesContext,
                                          UISelectMany selectMany)
    {
        Map paramValuesMap = facesContext.getExternalContext().getRequestParameterValuesMap();
        String clientId  = selectMany.getClientId(facesContext);
        if (paramValuesMap.containsKey(clientId))
        {
            String[] reqValues = (String[])paramValuesMap.get(clientId);
            selectMany.setSubmittedValue(reqValues);
        }
        else
        {
            selectMany.setSubmittedValue(null);
            return;
        }
    }


    public static void renderCheckbox(FacesContext facesContext,
                                      UIComponent uiComponent,
                                      String value,
                                      String label,
                                      boolean checked)
            throws IOException
    {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.INPUT_CHECKED_VALUE, null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(writer, uiComponent, facesContext);

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }

        writer.endElement(HTML.INPUT_ELEM);
    }


    public static void renderListbox(FacesContext facesContext,
                                     UISelectOne selectOne,
                                     int size) throws IOException
    {
        internalRenderSelect(facesContext, selectOne, size, false);
    }

    public static void renderListbox(FacesContext facesContext,
                                     UISelectMany selectMany,
                                     int size) throws IOException
    {
        internalRenderSelect(facesContext, selectMany, size, true);
    }
    
    public static void renderMenu(FacesContext facesContext,
                                  UISelectOne selectOne) throws IOException
    {
        internalRenderSelect(facesContext, selectOne, 1, false);
    }

    public static void renderMenu(FacesContext facesContext,
                                  UISelectMany selectMany) throws IOException
    {
        internalRenderSelect(facesContext, selectMany, 1, true);
    }

    private static void internalRenderSelect(FacesContext facesContext,
                                             UIComponent uiComponent,
                                             int size,
                                             boolean selectMany)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SELECT_ELEM, uiComponent);
        writer.writeAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext), null);

        List selectItemList;
        Converter converter;
        if (selectMany)
        {
            writer.writeAttribute(HTML.MULTIPLE_ATTR, HTML.MULTIPLE_ATTR, null);
            selectItemList = RendererUtils.getSelectItemList((UISelectMany)uiComponent);
            try
            {
                converter = RendererUtils.findUISelectManyConverter(facesContext,
                                                                    (UISelectMany)uiComponent);
            }
            catch (FacesException e)
            {
                log.error("Error finding Converter for component with id " + uiComponent.getClientId(facesContext));
                converter = null;
            }
        }
        else
        {
            selectItemList = RendererUtils.getSelectItemList((UISelectOne)uiComponent);
            try
            {
                converter = RendererUtils.findUIOutputConverter(facesContext,
                                                                (UISelectOne)uiComponent);
            }
            catch (FacesException e)
            {
                log.error("Error finding Converter for component with id " + uiComponent.getClientId(facesContext));
                converter = null;
            }
        }

        if (size == 0)
        {
            //No size given (Listbox) --> size is number of select items
            writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(selectItemList.size()), null);
        }
        else
        {
            writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(size), null);
        }
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.SELECT_PASSTHROUGH_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(writer, uiComponent, facesContext);

        Set lookupSet;
        if (selectMany)
        {
            lookupSet = RendererUtils.getSelectedValuesAsSet((UISelectMany)uiComponent);
        }
        else
        {
            lookupSet = Collections.singleton(((UISelectOne)uiComponent).getValue());
        }

        for (Iterator it = selectItemList.iterator(); it.hasNext(); )
        {
            SelectItem selectItem = (SelectItem)it.next();
            Object itemValue = selectItem.getValue();
            String itemStrValue;
            if (converter == null)
            {
                itemStrValue = itemValue.toString();
            }
            else
            {
                itemStrValue = converter.getAsString(facesContext, uiComponent, itemValue);
            }

            writer.write("\t\t");
            writer.startElement(HTML.OPTION_ELEM, uiComponent);
            writer.writeAttribute(HTML.VALUE_ATTR, itemStrValue, null);

            if (lookupSet.contains(itemValue))
            {
                writer.writeAttribute(HTML.INPUT_SELECTED_VALUE, HTML.INPUT_SELECTED_VALUE, null);
            }

            writer.writeText(selectItem.getLabel(), null);

            writer.endElement(HTML.OPTION_ELEM);
        }

        writer.endElement(HTML.SELECT_ELEM);
    }


    public static void renderRadio(FacesContext facesContext,
                                   UIInput uiComponent,
                                   String value,
                                   String label,
                                   boolean checked)
            throws IOException
    {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_RADIO, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.INPUT_CHECKED_VALUE, null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(writer, uiComponent, facesContext);

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }

        writer.endElement(HTML.INPUT_ELEM);
    }


    public static void writePrettyLineSeparator(FacesContext facesContext)
            throws IOException
    {
        if (MyFacesConfig.isPrettyHtml(facesContext.getExternalContext()))
        {
            facesContext.getResponseWriter().write(LINE_SEPARATOR);
        }
    }

    public static void writePrettyIndent(FacesContext facesContext)
            throws IOException
    {
        if (MyFacesConfig.isPrettyHtml(facesContext.getExternalContext()))
        {
            facesContext.getResponseWriter().write('\t');
        }
    }

    public static void renderCommandLinkStart(
            FacesContext facesContext, UIComponent component,
            String clientId,
            Object value,
            String styleClass,
            LinkParameter[] additionalParams)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (RendererUtils.isEnabledOnUserRole(facesContext, component))
        {
            if (MyFacesConfig.isAllowJavascript(facesContext.getExternalContext()))
            {
                renderJavaScriptAnchorStart(facesContext, writer, component, clientId, additionalParams);
            }
            else
            {
                renderNonJavaScriptAnchorStart(facesContext, writer, component, clientId, additionalParams);
            }

            HTMLUtil.renderHTMLAttributes(writer, component,
                                          HTML.ANCHOR_PASSTHROUGH_ATTRIBUTES_WITHOUT_STYLE_CLASS);
            HTMLUtil.renderHTMLAttribute(writer, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR,
                                         styleClass);
        }

        //MyFaces extension: Render text of given by value
        if(value != null)
        {
            writer.writeText(value.toString(), JSFAttr.VALUE_ATTR);
        }
    }

    private static void renderJavaScriptAnchorStart(FacesContext facesContext,
                                             ResponseWriter writer,
                                             UIComponent component,
                                             String clientId,
                                             LinkParameter[] additionalParams)
        throws IOException
    {
        //Find form
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIForm))
        {
            parent = parent.getParent();
        }

        boolean insideForm;
        String formName;
        DummyFormResponseWriter dummyFormResponseWriter;
        if (parent != null)
        {
            //link is nested inside a form
            UIForm form = (UIForm)parent;
            formName = form.getClientId(facesContext);
            insideForm = true;
            dummyFormResponseWriter = null;
        }
        else
        {
            //not nested in form, we must add a dummy form at the end of the document
            formName = DummyFormUtils.DUMMY_FORM_NAME;
            insideForm = false;
            dummyFormResponseWriter = DummyFormUtils.getDummyFormResponseWriter(facesContext);
            dummyFormResponseWriter.setWriteDummyForm(true);
        }

        StringBuffer onClick = new StringBuffer();

        String commandOnclick;
        if (component instanceof HtmlCommandLink)
        {
            commandOnclick = ((HtmlCommandLink)component).getOnclick();
        }
        else
        {
            commandOnclick = (String)component.getAttributes().get(HTML.ONCLICK_ATTR);
        }
        if (commandOnclick != null)
        {
            onClick.append(commandOnclick);
            onClick.append(';');
        }

        String jsForm = "document.forms['" + formName + "']";

        //add id parameter for decode
        onClick.append(jsForm);
        onClick.append(".elements['").append(clientId).append("']");
        onClick.append(".value='").append(clientId).append("';");
        if (insideForm)
        {
            renderHiddenParam(writer, clientId);
            //TODO: We must not render duplicate hidden params!
        }
        else
        {
            dummyFormResponseWriter.addDummyFormParameter(clientId);
        }

        //add child parameters
        for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                Object value = ((UIParameter)child).getValue();

                renderLinkParameter(writer, dummyFormResponseWriter, name, value, onClick, jsForm, insideForm);
            }
        }

        //add additional parameters

        if(additionalParams != null)
        {
            for (int i = 0; i < additionalParams.length; i++)
            {
                LinkParameter additionalParam = additionalParams[i];

                renderLinkParameter(writer, dummyFormResponseWriter,
                        additionalParam.getName(), additionalParam.getValue(), onClick,
                        jsForm, insideForm);
            }
        }

        //submit
        onClick.append(jsForm);
        onClick.append(".submit();");

        writer.startElement(HTML.ANCHOR_ELEM, component);
        writer.writeURIAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, onClick.toString(), null);
    }

    private static void renderNonJavaScriptAnchorStart(FacesContext facesContext,
                                                ResponseWriter writer,
                                                UIComponent component,
                                                String clientId,
                                                LinkParameter[] additionalParams)
        throws IOException
    {
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        String path = viewHandler.getActionURL(facesContext, viewId);

        StringBuffer hrefBuf = new StringBuffer(path);

        //add clientId parameter for decode

        if (path.indexOf('?') == -1)
        {
            hrefBuf.append('?');
        }
        else
        {
            hrefBuf.append('&');
        }
        hrefBuf.append(clientId);
        hrefBuf.append('=');
        hrefBuf.append(clientId);

        if (component.getChildCount() > 0 || additionalParams != null)
        {
            addChildParametersToHref(component, hrefBuf,
                                     false, //not the first url parameter
                                     writer.getCharacterEncoding(),
                                     additionalParams);
        }

        String href = hrefBuf.toString();
        writer.startElement(HTML.ANCHOR_ELEM, component);
        writer.writeURIAttribute(HTML.HREF_ATTR, href, null);
    }

    private static void addChildParametersToHref(UIComponent linkComponent,
                                          StringBuffer hrefBuf,
                                          boolean firstParameter,
                                          String charEncoding, LinkParameter[] additionalParams)
            throws IOException
    {
        for (Iterator it = linkComponent.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                Object value = ((UIParameter)child).getValue();

                addParameterToHref(name, value, hrefBuf, firstParameter, charEncoding);
            }
        }

        if(additionalParams!=null)
        {
            for (int i = 0; i < additionalParams.length; i++)
            {
                LinkParameter additionalParam = additionalParams[i];

                addParameterToHref(additionalParam.getName(), additionalParam.getValue(), hrefBuf, firstParameter, charEncoding);
            }
        }
    }

    public static void renderOutputLinkStart(FacesContext facesContext, UIOutput output,
                                      LinkParameter[] additionalParams)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (!RendererUtils.isEnabledOnUserRole(facesContext, output))
        {
            RendererUtils.renderChildren(facesContext, output);
            return;
        }

        //calculate href
        String href = RendererUtils.getStringValue(facesContext, output);
        if (output.getChildCount() > 0 || additionalParams != null)
        {
            StringBuffer hrefBuf = new StringBuffer(href);
            addChildParametersToHref(output, hrefBuf,
                                     (href.indexOf('?') == -1), //first url parameter?
                                     writer.getCharacterEncoding(), additionalParams);
            href = hrefBuf.toString();
        }
        href = facesContext.getExternalContext().encodeResourceURL(href);    //TODO: or encodeActionURL ?

        //write anchor
        writer.startElement(HTML.ANCHOR_ELEM, output);
        writer.writeURIAttribute(HTML.HREF_ATTR, href, null);
        HTMLUtil.renderHTMLAttributes(writer, output, HTML.ANCHOR_PASSTHROUGH_ATTRIBUTES);
        writer.flush();
    }

    private static void renderLinkParameter(ResponseWriter writer, DummyFormResponseWriter dummyFormResponseWriter, String name, Object value, StringBuffer onClick, String jsForm, boolean insideForm)
            throws IOException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
        }
        onClick.append(jsForm);
        onClick.append(".elements['").append(name).append("']");
        //UIParameter is no ValueHolder, so no conversion possible
        String strParamValue = value != null ? value.toString() : ""; //TODO: Use Converter?
        onClick.append(".value='").append(strParamValue).append("';");

        if (insideForm)
        {
            renderHiddenParam(writer, name);
        }
        else
        {
            dummyFormResponseWriter.addDummyFormParameter(name);
        }
    }


    private static void addParameterToHref(String name, Object value, StringBuffer hrefBuf, boolean firstParameter, String charEncoding)
            throws UnsupportedEncodingException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
        }

        hrefBuf.append(firstParameter ? '?' : '&');
        hrefBuf.append(URLEncoder.encode(name, charEncoding));
        hrefBuf.append('=');
        if (value != null)
        {
            //UIParameter is no ConvertibleValueHolder, so no conversion possible
            hrefBuf.append(URLEncoder.encode(value.toString(), charEncoding));
        }
    }

    private static void renderHiddenParam(ResponseWriter writer, String paramName)
        throws IOException
    {
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, paramName, null);
        writer.endElement(HTML.INPUT_ELEM);
    }

    public static void renderLinkEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        if (RendererUtils.isEnabledOnUserRole(facesContext, component))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.endElement(HTML.ANCHOR_ELEM);
        }
    }

    public static class LinkParameter
    {
        private String _name;
        private Object _value;

        public String getName()
        {
            return _name;
        }

        public void setName(String name)
        {
            _name = name;
        }

        public Object getValue()
        {
            return _value;
        }

        public void setValue(Object value)
        {
            _value = value;
        }

    }
}
