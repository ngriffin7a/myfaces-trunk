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

import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.render.Renderer;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RendererConfig
    implements Config
{
    private String _rendererType = null;
    private String _rendererClass = null;
    private Map _attributeConfigMap = null;
    private Set _componentTypeSet = null;
    private Set _componentClassSet = null;

    public String getRendererType()
    {
        return _rendererType;
    }

    public void setRendererType(String rendererType)
    {
        _rendererType = rendererType;
    }

    public String getRendererClass()
    {
        return _rendererClass;
    }

    public void setRendererClass(String rendererClass)
    {
        _rendererClass = rendererClass;
    }


    public void addAttributeConfig(AttributeConfig attributeConfig)
    {
        getAttributeConfigMap().put(attributeConfig.getAttributeName(),
                                    attributeConfig);
    }

    public AttributeConfig getAttributeConfig(String attributeName)
    {
        return (AttributeConfig)getAttributeConfigMap().get(attributeName);
    }

    public Iterator getAttributeNames()
    {
        return _attributeConfigMap == null
                ? Collections.EMPTY_SET.iterator()
                : _attributeConfigMap.keySet().iterator();
    }

    private Map getAttributeConfigMap()
    {
        if (_attributeConfigMap == null)
        {
            _attributeConfigMap = new HashMap();
        }
        return _attributeConfigMap;
    }


    public void addComponentType(String componentType)
    {
        if (_componentTypeSet == null)
        {
            _componentTypeSet = new HashSet();
        }
        _componentTypeSet.add(componentType);
    }

    public Iterator getComponentTypes()
    {
        return _componentTypeSet == null
                ? Collections.EMPTY_SET.iterator()
                : _componentTypeSet.iterator();
    }

    public boolean supportsComponentType(String componentType)
    {
        return _componentTypeSet.contains(componentType);
    }



    public void addComponentClass(String componentClassName)
    {
        try
        {
            if (_componentClassSet == null)
            {
                _componentClassSet = new HashSet();
            }
            _componentClassSet.add(Class.forName(componentClassName));
        }
        catch (ClassNotFoundException e)
        {
            LogUtil.getLogger().severe("Error in faces-config.xml - Class not found: " + componentClassName);
        }
    }

    public Iterator getComponentClassNames()
    {
        if (_componentClassSet == null)
        {
            return Collections.EMPTY_SET.iterator();
        }
        final Iterator it = _componentClassSet.iterator();
        return new Iterator() {
            public boolean hasNext()
            {
                return it.hasNext();
            }

            public Object next()
            {
                return ((Class)it.next()).getName();
            }

            public void remove()
            {
                it.remove();
            }
        };
    }

    Iterator getComponentClasses()
    {
        return _componentClassSet == null
                ? Collections.EMPTY_SET.iterator()
                : _componentClassSet.iterator();
    }

    public boolean supportsComponentClass(Class componentClass)
    {
        if (_componentClassSet == null)
        {
            return false;
        }

        if (_componentClassSet.contains(componentClass))
        {
            return true;
        }

        //Is it a derived class?
        for (Iterator it = _componentClassSet.iterator(); it.hasNext(); )
        {
            Class c = (Class)it.next();
            if (c.isAssignableFrom(componentClass))
            {
                return true;
            }
        }
        return false;
    }



    public Renderer newRenderer()
    {
        return (Renderer)ConfigUtil.newInstance(getRendererClass());
    }

}
