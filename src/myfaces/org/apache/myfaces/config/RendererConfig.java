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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private static final Log log = LogFactory.getLog(RendererConfig.class);

    private String _rendererType = null;
    private String _rendererClass = null;
    private Map _attributeConfigMap = null;
    private Set _supportedComponentTypeSet = null;
    private Set _supportedComponentClassSet = null;

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


    public void addSupportedComponentType(SupportedComponentTypeConfig componentType)
    {
        if (_supportedComponentTypeSet == null)
        {
            _supportedComponentTypeSet = new HashSet();
        }
        _supportedComponentTypeSet.add(componentType);
    }

    public Iterator getSupportedComponentTypes()
    {
        if (_supportedComponentTypeSet == null)
        {
            return Collections.EMPTY_SET.iterator();
        }
        final Iterator it = _supportedComponentTypeSet.iterator();
        return new Iterator() {
            public boolean hasNext()
            {
                return it.hasNext();
            }

            public Object next()
            {
                return ((SupportedComponentTypeConfig)it.next()).getComponentType();
            }

            public void remove()
            {
                it.remove();
            }
        };

    }

    public boolean supportsComponentType(String componentType)
    {
        for (Iterator it = getSupportedComponentTypes(); it.hasNext(); )
        {
            String type = (String)it.next();
            if ( type.equals(componentType))
            {
                return true;
            }
        }
        return false;
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

    public Iterator getSupportedComponentClassNames()
    {
        if (_supportedComponentClassSet == null)
        {
            return Collections.EMPTY_SET.iterator();
        }
        final Iterator it = _supportedComponentClassSet.iterator();
        return new Iterator() {
            public boolean hasNext()
            {
                return it.hasNext();
            }

            public Object next()
            {
                return ((SupportedComponentClassConfig)it.next()).getComponentClass();
            }

            public void remove()
            {
                it.remove();
            }
        };
    }

    public boolean supportsComponentClass(Class componentClass)
    {
        for (Iterator it = getSupportedComponentClassNames(); it.hasNext(); )
        {
            Class c = (Class)it.next();
            if ( c.isAssignableFrom(componentClass))
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
