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

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentConfig
    implements Config
{
    private String _componentType;
    private String _componentClass;
    private Map _attributes;
    private Map _properties;

    public String getComponentType()
    {
        return _componentType;
    }

    public void setComponentType(String componentType)
    {
        _componentType = componentType;
    }

    public String getComponentClass()
    {
        return _componentClass;
    }

    public void setComponentClass(String componentClass)
    {
        _componentClass = componentClass;
    }

    public void addAttribute(AttributeConfig attribute)
    {
        if (_attributes == null)
        {
            _attributes = new HashMap();
        }
        _attributes.put(attribute.getAttributeName(), attribute);
    }

    public Map getAttributes()
    {
        return _attributes == null ? Collections.EMPTY_MAP : _attributes;
    }

    public void addProperty(PropertyConfig property)
    {
        if (_properties == null)
        {
            _properties = new HashMap();
        }
        _properties.put(property.getPropertyName(), property);
    }

    public Map getProperties()
    {
        return _properties == null ? Collections.EMPTY_MAP : _properties;
    }

}
