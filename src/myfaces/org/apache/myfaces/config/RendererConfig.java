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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.render.Renderer;
import java.util.*;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class RendererConfig implements Config
{
    private static final Log log = LogFactory.getLog(RendererConfig.class);

    //~ Instance fields ----------------------------------------------------------------------------

    private Class  _rendererClass              = null;

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private Map    _attributeConfigMap         = null;
    private Set    _supportedComponentClassSet = null;
    private Set    _supportedComponentTypeSet  = null;
    private String _componentFamily            = null;
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

    public void setComponentFamily(String componentFamily)
    {
        _componentFamily = componentFamily.intern();
    }

    public String getComponentFamily()
    {
        return _componentFamily;
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

    public Renderer newRenderer(ExternalContext externalContext)
    {
        try
        {
            return (Renderer) ClassUtils.newInstance(getRendererClass());
        }
        catch (Exception e)
        {
            log.error("Renderer of class " + getRendererClass() + " could not be instantiated", e);
            throw new FacesException(e);
        }
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
