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
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.*;

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
}
