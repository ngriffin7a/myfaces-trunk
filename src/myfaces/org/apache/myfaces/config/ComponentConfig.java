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

import java.util.*;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ComponentConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private List   _propertyConfigList; // List to maintain ordering
    private Map    _attributeConfigMap;
    private String _componentClass; // must stay String because of Application.addComponent()
    private String _componentType;

    //~ Methods ------------------------------------------------------------------------------------

    public Map getAttributeMap()
    {
        return (_attributeConfigMap == null) ? Collections.EMPTY_MAP : _attributeConfigMap;
    }

    public void setComponentClass(String componentClass)
    {
        _componentClass = componentClass.intern();
    }

    public String getComponentClass()
    {
        return _componentClass;
    }

    public void setComponentType(String componentType)
    {
        _componentType = componentType.intern();
    }

    public String getComponentType()
    {
        return _componentType;
    }

    public List getPropertyConfigList()
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

    public void addPropertyConfig(PropertyConfig property)
    {
        if (_propertyConfigList == null)
        {
            _propertyConfigList = new ArrayList();
        }
        _propertyConfigList.add(property);
    }
}
