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
package org.apache.myfaces.taglib.core;

import java.util.Locale;
import java.util.TimeZone;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.ConverterTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/11/22 16:13:08  bdudney
 * added code to make sure the converterId is set in WebLogic8.1sp3.
 *
 * Revision 1.6  2004/10/13 11:51:00  matze
 * renamed packages to org.apache
 *
 * Revision 1.5  2004/07/01 22:05:03  mwessendorf
 * ASF switch
 *
 * Revision 1.4  2004/06/27 19:26:18  mwessendorf
 * added default value, needed by jsf-spec
 *
 * Revision 1.3  2004/04/16 15:35:59  manolito
 * Log
 *
 */
public class ConvertDateTimeTag
        extends ConverterTag
{
    private String _dateStyle = "default"; // the default value as required by the spec (default in this case)
    private String _locale = null;
    private String _pattern = null;
    private String _timeStyle = "default"; // the default value as required by the spec (default in this case)
    private String _timeZone = null;
    private String _type = null;

    public ConvertDateTimeTag()
    {
      super();
        //setConverterId(DateTimeConverter.CONVERTER_ID);
    }

    public void setDateStyle(String dateStyle)
    {
        _dateStyle = dateStyle;
    }

    public void setLocale(String locale)
    {
        _locale = locale;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    public void setTimeStyle(String timeStyle)
    {
        _timeStyle = timeStyle;
    }

    public void setTimeZone(String timeZone)
    {
        _timeZone = timeZone;
    }

    public void setType(String type)
    {
        _type = type;
    }

    public void setPageContext(PageContext context) 
    {
        super.setPageContext(context);
        setConverterId(DateTimeConverter.CONVERTER_ID);
    }

    protected Converter createConverter() throws JspException
    {
        DateTimeConverter converter = (DateTimeConverter)super.createConverter();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        setConverterDateStyle(facesContext, converter, _dateStyle);
        setConverterLocale(facesContext, converter, _locale);
        setConverterPattern(facesContext, converter, _pattern);
        setConverterTimeStyle(facesContext, converter, _timeStyle);
        setConverterTimeZone(facesContext, converter, _timeZone);
        setConverterType(facesContext, converter, _type);

        return converter;
    }
    
    
    private static void setConverterDateStyle(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setDateStyle((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setDateStyle(value);
        }
    }

    private static void setConverterLocale(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setLocale((Locale)vb.getValue(facesContext));
        }
        else
        {
            throw new IllegalArgumentException("Attribute locale of type String not supported");
        }
    }

    private static void setConverterPattern(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setPattern((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setPattern(value);
        }
    }

    private static void setConverterTimeStyle(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setTimeStyle((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setTimeStyle(value);
        }
    }

    private static void setConverterTimeZone(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setTimeZone((TimeZone)vb.getValue(facesContext));
        }
        else
        {
            converter.setTimeZone(TimeZone.getTimeZone(value));
        }
    }

    private static void setConverterType(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setType((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setType(value);
        }
    }

}
