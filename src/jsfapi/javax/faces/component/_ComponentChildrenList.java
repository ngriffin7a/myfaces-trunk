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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _ComponentChildrenList
        extends AbstractList
        implements Serializable
{
    private UIComponent _component;
    private List _list = new ArrayList();

    _ComponentChildrenList(UIComponent component)
    {
        _component = component;
    }

    public Object get(int index)
    {
        return _list.get(index);
    }

    public int size()
    {
        return _list.size();
    }

    public Object set(int index, Object value)
    {
        checkValue(value);
        setNewParent((UIComponent)value);
        return _list.set(index, value);
    }

    public boolean add(Object value)
    {
        checkValue(value);
        setNewParent((UIComponent)value);
        return _list.add(value);
    }

    public Object remove(int index)
    {
        UIComponent child = (UIComponent) _list.remove(index);
        if (child != null) child.setParent(null);
        return child;
    }


    private void setNewParent(UIComponent child)
    {
        UIComponent oldParent = child.getParent();
        if (oldParent != null)
        {
            oldParent.getChildren().remove(child);
        }
        child.setParent(_component);
    }

    private void checkValue(Object value)
    {
        if (value == null) throw new NullPointerException("value");
        if (!(value instanceof UIComponent)) throw new ClassCastException("value is not a UIComponent");
    }

}
