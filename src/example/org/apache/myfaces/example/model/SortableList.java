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



/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class SortableList
{
    private String _sort;
    private final static String DESCENDING = "-DESC";

    protected abstract boolean isColumnAscendingByDefault(String column);

    protected abstract void sort(String column, boolean ascending);

    public void sort(String sortCommand)
    {
        String sortColumn = getSortColumn();

        if (sortCommand == null)
        {
            throw new IllegalArgumentException("Argument sortCommand may not be null.");
        }

        // case 1: first click on a sort column
        if (_sort == null)
        {
            // column Ascending by default -> sort descending
            if (isColumnAscendingByDefault(sortCommand))
            {
                _sort = sortCommand + DESCENDING;
                sort(sortCommand, false);
            }
            else
            {
                _sort = sortCommand;
                sort(sortCommand, true);
            }
        }
        // case 2: actual sort equals new sortCommand -> sort descending
        else if (_sort.equals(sortCommand))
        {
            _sort = sortColumn + DESCENDING;
            sort(sortCommand, false);
        }
        // case 3: actual sort equals new sortCommand -> sort ascending
        else if (sortColumn.equals(sortCommand))
        {
            _sort = sortColumn;
            sort(sortCommand, true);
        }
        // case 4: sort another column
        else
        {
            _sort = sortCommand;
            sort(sortCommand, true);
        }
    }

    public String getSort()
    {
        return _sort;
    }

    public void setSort(String sort)
    {
        _sort = sort;
    }

    private String getSortColumn()
    {
        if (_sort == null)
        {
            return null;
        }

        if (_sort.endsWith(DESCENDING))
        {
            return _sort.substring(0, _sort.length() - DESCENDING.length());
        }
        else
        {
            return _sort;
        }
    }
}
