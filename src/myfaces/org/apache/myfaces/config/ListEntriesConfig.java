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
package net.sourceforge.myfaces.config;

import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.webapp.UIComponentTag;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ListEntriesConfig
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Class _valueClass = null;
    private List  _values = null;
    private boolean _containsValueBindings = false;

    //~ Methods ------------------------------------------------------------------------------------

    public void setValueClass(String valueClass)
    {
        _valueClass = ClassUtils.classForName(valueClass);  //no need to support primitive types
    }

    public Class getValueClass()
    {
        return _valueClass;
    }

    public void addNullValue(String dummy)
    {
        addValue(null);
    }

    public void addValue(String value)
    {
        if (_values == null)
        {
            _values = new ArrayList();
        }

        if (value != null && UIComponentTag.isValueReference(value))
        {
            _values.add(new ValueBindingExpression(value));
            _containsValueBindings = true;
        }
        else
        {
            if (_valueClass != null)
            {
                _values.add(ConfigUtils.convertToType(value, _valueClass));
            }
            else
            {
                _values.add(value);
            }
        }
    }

    public List getValues()
    {
        return _values;
    }

    public boolean isContainsValueBindings()
    {
        return _containsValueBindings;
    }
}
