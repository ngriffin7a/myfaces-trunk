/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.config;

import javax.faces.convert.Converter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterConfig
    implements Config
{
    private String _converterId;
    private String _converterForClass;
    private String _converterClass;
    private Map _attributeConfigMap;
    private Map _propertyConfigMap;

    public String getConverterId()
    {
        return _converterId;
    }

    public void setConverterId(String converterId)
    {
        _converterId = converterId;
    }

    public String getConverterForClass()
    {
        return _converterForClass;
    }

    public void setConverterForClass(String converterForClass)
    {
        _converterForClass = converterForClass;
    }

    public String getConverterClass()
    {
        return _converterClass;
    }

    public void setConverterClass(String converterClass)
    {
        _converterClass = converterClass;
    }


    public void addAttributeConfig(AttributeConfig attributeConfig)
    {
        if (_attributeConfigMap == null)
        {
            _attributeConfigMap = new HashMap();
        }
        _attributeConfigMap.put(attributeConfig.getAttributeName(), attributeConfig);
    }

    public Iterator getAttributeNames()
    {
        return _attributeConfigMap == null
                ? Collections.EMPTY_SET.iterator()
                : _attributeConfigMap.keySet().iterator();
    }


    public void addPropertyConfig(PropertyConfig propertyConfig)
    {
        if (_propertyConfigMap == null)
        {
            _propertyConfigMap = new HashMap();
        }
        _propertyConfigMap.put(propertyConfig.getPropertyName(), propertyConfig);
    }

    public Iterator getPropertyNames()
    {
        return _propertyConfigMap == null
                ? Collections.EMPTY_SET.iterator()
                : _propertyConfigMap.keySet().iterator();
    }


    public Converter newConverter()
    {
        return (Converter)ConfigUtil.newInstance(_converterClass);
    }
}
