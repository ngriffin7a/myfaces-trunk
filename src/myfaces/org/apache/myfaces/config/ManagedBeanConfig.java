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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final int TYPE_NO_INIT = 0;
    public static final int TYPE_PROPERTIES = 1;
    public static final int TYPE_MAP = 2;
    public static final int TYPE_LIST = 3;

    //~ Instance fields ----------------------------------------------------------------------------

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private List _propertyConfigList = null;
    private MapEntriesConfig _mapEntriesConfig = null;
    private ListEntriesConfig _listEntriesConfig = null;
    private Class  _managedBeanClass;
    private String _managedBeanName = null;
    private String _managedBeanScope = null;
    private int _type = TYPE_NO_INIT;

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

    public void addManagedPropertyConfig(ManagedPropertyConfig propertyConfig)
    {
        if (_propertyConfigList == null)
        {
            _propertyConfigList = new ArrayList();
        }
        _propertyConfigList.add(propertyConfig);
        _type = TYPE_PROPERTIES;
    }

    public List getManagedPropertyConfigList()
    {
        return (_propertyConfigList != null) ? _propertyConfigList : Collections.EMPTY_LIST;
    }

    public void setMapEntriesConfig(MapEntriesConfig mapEntriesConfig)
    {
        _mapEntriesConfig = mapEntriesConfig;
        _type = TYPE_MAP;
    }

    public MapEntriesConfig getMapEntriesConfig()
    {
        return _mapEntriesConfig;
    }

    public void setListEntriesConfig(ListEntriesConfig listEntriesConfig)
    {
        _listEntriesConfig = listEntriesConfig;
        _type = TYPE_LIST;
    }

    public ListEntriesConfig getListEntriesConfig()
    {
        return _listEntriesConfig;
    }

    public int getType()
    {
        return _type;
    }


}
