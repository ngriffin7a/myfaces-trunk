/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.example.model;

import java.util.*;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SimpleCarList
    extends SortableList
{
    private List _cars;

    public SimpleCarList()
    {
        super("type");

        _cars = new ArrayList();
        _cars.add(new SimpleCar("car A", "red"));
        _cars.add(new SimpleCar("car B", "blue"));
        _cars.add(new SimpleCar("car C", "green"));
        _cars.add(new SimpleCar("car D", "yellow"));
        _cars.add(new SimpleCar("car E", "orange"));
    }

    public Iterator getCars()
    {
        sort(getSort(), isAscending());
        return _cars.iterator();
    }

    protected boolean isDefaultAscending(String sortColumn)
    {
        return true;
    }

    protected void sort(final String column, final boolean ascending)
    {
        Comparator comparator = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                SimpleCar c1 = (SimpleCar)o1;
                SimpleCar c2 = (SimpleCar)o2;
                if (column == null)
                {
                    return 0;
                }
                if (column.equals("type"))
                {
                    return ascending ? c1.getType().compareTo(c2.getType()) : c2.getType().compareTo(c1.getType());
                }
                else if (column.equals("color"))
                {
                    return ascending ? c1.getColor().compareTo(c2.getColor()) : c2.getColor().compareTo(c1.getColor());
                }
                else return 0;
            }
        };
        Collections.sort(_cars, comparator);
    }
}
