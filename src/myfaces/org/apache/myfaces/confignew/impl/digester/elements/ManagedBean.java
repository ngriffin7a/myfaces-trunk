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

package net.sourceforge.myfaces.confignew.impl.digester.elements;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class ManagedBean implements net.sourceforge.myfaces.confignew.element.ManagedBean
{

    private String name;
    private String beanClass;
    private String scope;
    private List property = new ArrayList();
    private MapEntries mapEntries;
    private ListEntries listEntries;


    public int getInitMode()
    {
        if (mapEntries != null) {
            return INIT_MODE_MAP;
        }
        if (listEntries != null) {
            return INIT_MODE_LIST;
        }
        if (! property.isEmpty()) {
            return INIT_MODE_PROPERTIES;
        }
        return INIT_MODE_NO_INIT;
    }



    public net.sourceforge.myfaces.confignew.element.MapEntries getMapEntries()
    {
        return mapEntries;
    }


    public void setMapEntries(MapEntries mapEntries)
    {
        this.mapEntries = mapEntries;
    }


    public net.sourceforge.myfaces.confignew.element.ListEntries getListEntries()
    {
        return listEntries;
    }


    public void setListEntries(ListEntries listEntries)
    {
        this.listEntries = listEntries;
    }


    public String getManagedBeanName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getManagedBeanClass()
    {
        return beanClass;
    }


    public void setBeanClass(String beanClass)
    {
        this.beanClass = beanClass;
    }


    public String getManagedBeanScope()
    {
        return scope;
    }


    public void setScope(String scope)
    {
        this.scope = scope;
    }


    public void addProperty(ManagedProperty value)
    {
        property.add(value);
    }


    public Iterator getManagedProperties()
    {
        return property.iterator();
    }
}
