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

import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.util.NullIterator;

import javax.faces.render.Renderer;
import java.util.*;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class RendererConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Class  _rendererClass              = null;

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private Map    _attributeConfigMap         = null;
    private Set    _supportedComponentClassSet = null;
    private Set    _supportedComponentTypeSet  = null;
    private String _rendererType               = null;

    //~ Methods ------------------------------------------------------------------------------------

    public AttributeConfig getAttributeConfig(String attributeName)
    {
        return (AttributeConfig) getAttributeConfigMap().get(attributeName);
    }

    public Iterator getAttributeNames()
    {
        return (_attributeConfigMap == null) ? NullIterator.instance()
                                             : _attributeConfigMap.keySet().iterator();
    }

    public void setDescription(String description)
    {
// ignore        
//        _description = description;
    }

    public void setDisplayName(String displayName)
    {
// ignore        
//        _displayName = displayName;
    }

    public void setIconConfig(IconConfig iconConfig)
    {
// ignore        
//        _iconConfig = iconConfig;
    }

    public void setRendererClass(String rendererClass)
    {
        _rendererClass = ClassUtils.classForName(rendererClass);
    }

    public Class getRendererClass()
    {
        return _rendererClass;
    }

    public void setRendererType(String rendererType)
    {
        _rendererType = rendererType.intern();
    }

    public String getRendererType()
    {
        return _rendererType;
    }

    public Iterator getSupportedComponentClassNames()
    {
        if (_supportedComponentClassSet == null)
        {
            return NullIterator.instance();
        }

        final Iterator it = _supportedComponentClassSet.iterator();
        return new Iterator()
            {
                public boolean hasNext()
                {
                    return it.hasNext();
                }

                public Object next()
                {
                    return ((SupportedComponentClassConfig) it.next()).getComponentClass();
                }

                public void remove()
                {
                    it.remove();
                }
            };
    }

    public Iterator getSupportedComponentTypes()
    {
        if (_supportedComponentTypeSet == null)
        {
            return NullIterator.instance();
        }

        final Iterator it = _supportedComponentTypeSet.iterator();
        return new Iterator()
            {
                public boolean hasNext()
                {
                    return it.hasNext();
                }

                public Object next()
                {
                    return ((SupportedComponentTypeConfig) it.next()).getComponentType();
                }

                public void remove()
                {
                    it.remove();
                }
            };
    }

    public void addAttributeConfig(AttributeConfig attributeConfig)
    {
        getAttributeConfigMap().put(attributeConfig.getAttributeName(), attributeConfig);
    }

    public void addSupportedComponentClass(SupportedComponentClassConfig componentClassName)
    {
        if (_supportedComponentClassSet == null)
        {
            _supportedComponentClassSet = new HashSet();
        }
        _supportedComponentClassSet.add(componentClassName);
    }

    public void addSupportedComponentClass(Class clazz)
    {
        if (_supportedComponentClassSet == null)
        {
            _supportedComponentClassSet = new HashSet();
        }
        _supportedComponentClassSet.add(new SupportedComponentClassConfig(clazz));
    }

    public void addSupportedComponentType(SupportedComponentTypeConfig componentType)
    {
        if (_supportedComponentTypeSet == null)
        {
            _supportedComponentTypeSet = new HashSet();
        }
        _supportedComponentTypeSet.add(componentType);
    }

    public Renderer newRenderer()
    {
        return (Renderer) ClassUtils.newInstance(getRendererClass());
    }

    public boolean supportsComponentClass(Class componentClass)
    {
        for (Iterator it = getSupportedComponentClassNames(); it.hasNext();)
        {
            Class c = (Class) it.next();
            if (c.isAssignableFrom(componentClass))
            {
                return true;
            }
        }
        return false;
    }

    public boolean supportsComponentType(String componentType)
    {
        for (Iterator it = getSupportedComponentTypes(); it.hasNext();)
        {
            String type = (String) it.next();
            if (type.equals(componentType))
            {
                return true;
            }
        }
        return false;
    }

    private Map getAttributeConfigMap()
    {
        if (_attributeConfigMap == null)
        {
            _attributeConfigMap = new HashMap();
        }
        return _attributeConfigMap;
    }
}
