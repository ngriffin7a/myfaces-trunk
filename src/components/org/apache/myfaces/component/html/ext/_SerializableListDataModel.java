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
package net.sourceforge.myfaces.component.html.ext;

import javax.faces.component.StateHolder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * %Log$
 */
class _SerializableListDataModel
        extends _SerializableDataModel
{
    //private static final Log log = LogFactory.getLog(_SerializableDataModel.class);

    public _SerializableListDataModel(int first, int rows, List list)
    {
        _first = first;
        _rows = rows;
        _rowCount = list.size();
        if (_rows <= 0)
        {
            _rows = _rowCount - first;
        }

        if (_rows == _rowCount)
        {
            //whole list must be saved
            if (list instanceof Serializable || list instanceof StateHolder)
            {
                _list = list;
            }
            else
            {
                //copy list
                _list = new ArrayList(list);
            }
        }
        else
        {
            int size = _rows > 0 && _rows < _rowCount ? _rows : _rowCount;
            _list = new ArrayList(size);
            if (size > _rowCount - _first)
            {
                size = _rowCount - _first;
            }
            for (int i = 0; i < size; i++)
            {
                _list.add(list.get(_first + i));
            }
        }
    }
}
