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
package javax.faces.component;

import java.io.Serializable;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _ComponentFacetMap
        implements Map, Serializable
{
    private UIComponent _component;
    private Map _map = new HashMap();

    _ComponentFacetMap(UIComponent component)
    {
        _component = component;
    }

    public int size()
    {
        return _map.size();
    }

    public void clear()
    {
        _map.clear();
    }

    public boolean isEmpty()
    {
        return _map.isEmpty();
    }

    public boolean containsKey(Object key)
    {
        checkKey(key);
        return _map.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        checkValue(value);
        return false;
    }

    public Collection values()
    {
        return _map.values();
    }

    public void putAll(Map t)
    {
        for (Iterator it = t.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Entry)it.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    public Set entrySet()
    {
        return _map.entrySet();
    }

    public Set keySet()
    {
        return _map.keySet();
    }

    public Object get(Object key)
    {
        checkKey(key);
        return _map.get(key);
    }

    public Object remove(Object key)
    {
        checkKey(key);
        UIComponent facet = (UIComponent)_map.remove(key);
        if (facet != null) facet.setParent(null);
        return facet;
    }

    public Object put(Object key, Object value)
    {
        checkKey(key);
        checkValue(value);
        setNewParent((String)key, (UIComponent)value);
        return _map.put(key, value);
    }


    private void setNewParent(String facetName, UIComponent facet)
    {
        UIComponent oldParent = facet.getParent();
        if (oldParent != null)
        {
            oldParent.getFacets().remove(facetName);
        }
        facet.setParent(_component);
    }

    private void checkKey(Object key)
    {
        if (key == null) throw new NullPointerException("key");
        if (!(key instanceof String)) throw new ClassCastException("key is not a String");
    }

    private void checkValue(Object value)
    {
        if (value == null) throw new NullPointerException("value");
        if (!(value instanceof UIComponent)) throw new ClassCastException("value is not a UIComponent");
    }

}
