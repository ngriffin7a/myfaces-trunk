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
package net.sourceforge.myfaces.config;

import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.util.NullIterator;

import javax.faces.convert.Converter;
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
        _converterForClass = ClassUtils.javaTypeToClass(converterForClass);
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
        return (Converter) ClassUtils.newInstance(_converterClass);
    }
}
