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
