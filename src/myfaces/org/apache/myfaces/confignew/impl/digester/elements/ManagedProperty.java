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
package net.sourceforge.myfaces.confignew.impl.digester.elements;

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
