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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
