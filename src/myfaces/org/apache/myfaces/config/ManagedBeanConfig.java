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

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Implements the configureation class for managed beans defined with &lt;managed-bean&gt;
 * in faces-config.xml
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ManagedBeanConfig implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Class  _managedBeanClass;

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private List   _propertyConfigList = null;
    private String _managedBeanName = null;
    private String _managedBeanScope = null;

    //~ Methods ------------------------------------------------------------------------------------

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

    public void setManagedBeanClass(String managedBeanClass)
    {
        _managedBeanClass = ClassUtils.classForName(managedBeanClass);
    }

    public Class getManagedBeanClass()
    {
        return _managedBeanClass;
    }

    public void setManagedBeanName(String managedBeanName)
    {
        _managedBeanName = managedBeanName.intern();
    }

    public String getManagedBeanName()
    {
        return _managedBeanName;
    }

    public void setManagedBeanScope(String managedBeanScope)
    {
        _managedBeanScope = managedBeanScope.intern();
    }

    public String getManagedBeanScope()
    {
        return _managedBeanScope;
    }

    public List getPropertyConfigList()
    {
        return (_propertyConfigList != null) ? _propertyConfigList : Collections.EMPTY_LIST;
    }

    public void addListEntriesConfig(ListEntriesConfig listEntriesConfig)
    {
        addToList(listEntriesConfig);
    }

    public void addManagedPropertyConfig(ManagedPropertyConfig propertyConfig)
    {
        addToList(propertyConfig);
    }

    public void addMapEntriesConfig(MapEntriesConfig mapEntriesConfig)
    {
        addToList(mapEntriesConfig);
    }

    public Object createBean(FacesContext facesContext)
    {
        Object bean;
        try
        {
            bean = _managedBeanClass.newInstance();
        }
        catch (Exception e)
        {
            throw new EvaluationException("Unable to instantiate: " + _managedBeanClass, e);
        }

        if (_propertyConfigList != null)
        {
            for (int i = 0, len = _propertyConfigList.size(); i < len; i++)
            {
                Object propConfig = _propertyConfigList.get(i);
                if (propConfig instanceof ManagedPropertyConfig)
                {
                    ((ManagedPropertyConfig) propConfig).updateBean(facesContext, bean);
                }
                else if (propConfig instanceof MapEntriesConfig)
                {
                    ((MapEntriesConfig) propConfig).updateBean(facesContext, (Map) bean);
                }
                else if (propConfig instanceof ListEntriesConfig)
                {
                    ((ListEntriesConfig) propConfig).updateBean(facesContext, (List) bean);
                }
            }
        }

        return bean;
    }

    private void addToList(Object o)
    {
        if (_propertyConfigList == null)
        {
            _propertyConfigList = new ArrayList();
        }
        _propertyConfigList.add(o);
    }
}
