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

import java.util.ArrayList;
import java.util.List;
import javax.faces.el.ValueBinding;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class ManagedProperty implements net.sourceforge.myfaces.confignew.element.ManagedProperty
{

    private String propertyName;
    private String propertyClass;
    private boolean nullValue = false;
    private String value;
    private MapEntries mapEntries;
    private ListEntries listEntries;
    private ValueBinding valueBinding;


    public int getType()
    {
        if (mapEntries != null)
        {
            return TYPE_MAP;
        }
        if (listEntries != null)
        {
            return TYPE_LIST;
        }
        if (nullValue)
        {
            return TYPE_NULL;
        }
        return TYPE_VALUE;
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


    public String getPropertyName()
    {
        return propertyName;
    }


    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }


    public String getPropertyClass()
    {
        return propertyClass;
    }


    public void setPropertyClass(String propertyClass)
    {
        this.propertyClass = propertyClass;
    }


    public boolean isNullValue()
    {
        return nullValue;
    }


    public void setNullValue()
    {
        this.nullValue = true;
    }


    public String getValue()
    {
        return value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public ValueBinding getValueBinding()
    {
        return valueBinding;
    }


    public void setValueBinding(ValueBinding valueBinding)
    {
        this.valueBinding = valueBinding;
    }

}
