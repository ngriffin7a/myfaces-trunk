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
package net.sourceforge.myfaces.config;

import javax.faces.convert.Converter;

import net.sourceforge.myfaces.util.NullIterator;

import java.util.*;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ConverterConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Class  _converterForClass; // must stay String because of Application.addConverter()
    private List   _propertyConfigList; // List to maintain ordering
    private Map    _attributeConfigMap;
    private String _converterClass;
    private String _converterId;

    //~ Methods ------------------------------------------------------------------------------------

    public Iterator getAttributeNames()
    {
        return (_attributeConfigMap == null) ? NullIterator.instance()
                                             : _attributeConfigMap.keySet().iterator();
    }

    public void setConverterClass(String converterClass)
    {
        _converterClass = converterClass.intern();
    }

    public String getConverterClass()
    {
        return _converterClass;
    }

    public void setConverterForClass(String converterForClass)
    {
        _converterForClass = ConfigUtil.classForName(converterForClass);
    }

    public Class getConverterForClass()
    {
        return _converterForClass;
    }

    public void setConverterId(String converterId)
    {
        _converterId = converterId.intern();
    }

    public String getConverterId()
    {
        return _converterId;
    }

    public List getProperties()
    {
        return _propertyConfigList;
    }

    public void addAttributeConfig(AttributeConfig attributeConfig)
    {
        if (_attributeConfigMap == null)
        {
            _attributeConfigMap = new HashMap();
        }
        _attributeConfigMap.put(attributeConfig.getAttributeName(), attributeConfig);
    }

    public void addPropertyConfig(PropertyConfig propertyConfig)
    {
        if (_propertyConfigList == null)
        {
            _propertyConfigList = new ArrayList();
        }
        _propertyConfigList.add(propertyConfig);
    }

    public Converter newConverter()
    {
        return (Converter) ConfigUtil.newInstance(_converterClass);
    }
}
