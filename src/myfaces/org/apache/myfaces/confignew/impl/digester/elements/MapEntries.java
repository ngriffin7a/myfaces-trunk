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
import java.util.Collection;
import java.util.Iterator;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class MapEntries implements net.sourceforge.myfaces.confignew.element.MapEntries
{

    private String keyClass;
    private String valueClass;
    private List entries = new ArrayList();


    public String getKeyClass()
    {
        return keyClass;
    }


    public void setKeyClass(String keyClass)
    {
        this.keyClass = keyClass;
    }


    public String getValueClass()
    {
        return valueClass;
    }


    public void setValueClass(String valueClass)
    {
        this.valueClass = valueClass;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }


    public Iterator getMapEntries()
    {
        return entries.iterator();
    }


    public static class Entry implements net.sourceforge.myfaces.confignew.element.MapEntry {
       String key;
        boolean nullValue = false;
        String value;


        public String getKey()
        {
            return key;
        }


        public void setKey(String key)
        {
            this.key = key;
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
    }
}
