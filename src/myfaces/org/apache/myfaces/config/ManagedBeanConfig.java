/**
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
import java.util.List;

/**
 * Implements the configureation class for managed beans defined with &lt;managed-bean&gt;
 * in faces-config.xml
 *  
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ManagedBeanConfig
    implements Config
{
    private String _managedBeanName;
    private String _managedBeanClass;
    private String _managedBeanScope;
    private List _managedPropertyConfigList;

    public String getManagedBeanName()
    {
        return _managedBeanName;
    }

    public void setManagedBeanName(String managedBeanName)
    {
        _managedBeanName = managedBeanName;
    }

    public String getManagedBeanClass()
    {
        return _managedBeanClass;
    }

    public void setManagedBeanClass(String managedBeanClass)
    {
        _managedBeanClass = managedBeanClass;
    }

    public String getManagedBeanScope()
    {
        return _managedBeanScope;
    }

    public void setManagedBeanScope(String managedBeanScope)
    {
        _managedBeanScope = managedBeanScope;
    }



    public void addManagedPropertyConfig(ManagedPropertyConfig propertyConfig)
    {
        if (_managedPropertyConfigList == null)
        {
            _managedPropertyConfigList = new ArrayList();
        }
		_managedPropertyConfigList.add(propertyConfig);
    }

    public List getManagedPropertyConfigList() 
    {
        return _managedPropertyConfigList != null
                ? _managedPropertyConfigList
                : Collections.EMPTY_LIST;
    }
    
    public void setDescription(String s) 
    {
        // ignore, we do not care
    }

}
