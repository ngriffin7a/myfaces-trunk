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

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.application.MessageFactory;
import net.sourceforge.myfaces.convert.MyFacesConverterException;
import net.sourceforge.myfaces.convert.impl.StringArrayConverter;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlRendererUtils
{
    private static final Log log = LogFactory.getLog(HtmlRendererUtils.class);

    //TODO: Define in Bundle
    private static final String DEFAULT_CONVERTER_EXCEPTION_MSG_ID
            = "net.sourceforge.myfaces.convert.Converter.EXCEPTION";


    private HtmlRendererUtils() {} //no instance allowed

    /**
     * @param facesContext
     * @param input
     * @param defaultConverter      Converter to be used by default or null for no conversion (=StringConverter)
     * @param setValueOnAbsentParam set value although param is absent?
     * @param reqParamAbsentValue   value to be used if parameter is absent,
     *                              e.g. Boolean.FALSE for UISelectBoolean
     */
    public static void decodeInput(FacesContext facesContext,
                                   UIInput input,
                                   Converter defaultConverter,
                                   boolean setValueOnAbsentParam,
                                   Object reqParamAbsentValue)
    {

        Map paramValuesMap = facesContext.getExternalContext().getRequestParameterValuesMap();
        String clientId  = input.getClientId(facesContext);
        if (!paramValuesMap.containsKey(clientId))
        {
            if (setValueOnAbsentParam)
            {
                Object previous = input.getValue();
                input.setValue(reqParamAbsentValue);
                input.setPrevious(previous);
            }
        }
        else
        {
            Object previous = input.getValue();

            String[] newValues = (String[])paramValuesMap.get(clientId);
            Converter converter = RendererUtils.findValueConverter(facesContext, input);
            if (converter == null)
            {
                converter = defaultConverter;
            }

            if (converter != null)
            {
                //Converter found
                if (converter instanceof StringArrayConverter)
                {
                    //Expected type is StringArray --> no conversion necessary
                    input.setValue(newValues);
                    input.setValid(true);
                }
                else
                {
                    String strValue = StringArrayConverter.getAsString(newValues, false);
                    try
                    {
                        Object objValue = converter.getAsObject(facesContext,
                                                                input,
                                                                strValue);
                        input.setValue(objValue);
                        input.setValid(true);
                    }
                    catch (ConverterException e)
                    {
                        if (log.isInfoEnabled()) log.info("Converter exception", e);
                        if (e instanceof MyFacesConverterException)
                        {
                            facesContext.addMessage(clientId,
                                                    ((MyFacesConverterException)e).getFacesMessage());
                            ((MyFacesConverterException)e).release();
                        }
                        else
                        {
                            MessageFactory mf = MyFacesFactoryFinder.getMessageFactory(facesContext.getExternalContext());
                            facesContext.addMessage(clientId,
                                                    mf.getMessage(facesContext, DEFAULT_CONVERTER_EXCEPTION_MSG_ID));
                        }
                        input.setValue(strValue);
                        input.setValid(false);
                    }
                }
            }
            else
            {
                //No converter --> we assume String value is the default type
                String strValue = StringArrayConverter.getAsString(newValues, false);
                input.setValue(strValue);
                input.setValid(true);
            }

            input.setPrevious(previous);
        }
    }

    public static void drawCheckbox(
        FacesContext facesContext, UIComponent uiComponent, String value, String label,
        boolean checked)
    throws IOException
    {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR,HTML.INPUT_TYPE_CHECKBOX,null);
        writer.writeAttribute(HTML.NAME_ATTR,clientId,null);
        writer.writeAttribute(HTML.ID_ATTR,clientId,null);

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR,HTML.INPUT_CHECKED_VALUE,null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR,value,null);
        }

        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(writer, uiComponent, facesContext);

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.write(label);
        }
    }

}
