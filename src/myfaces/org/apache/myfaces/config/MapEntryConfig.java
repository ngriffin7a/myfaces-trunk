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

import net.sourceforge.myfaces.util.FacesUtils;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class MapEntryConfig
{
    //~ Instance fields ----------------------------------------------------------------------------

    Object       _key;
    Object       _value;
    ValueBinding _valueBinding;
    private int  _type;

    //~ Methods ------------------------------------------------------------------------------------

    public void setKey(String key)
    {
        if ((key == null) || (key.length() == 0))
        {
            throw new NullPointerException("map-entry key must not be null");
        }

        _key = key.intern();
    }

    public Object getKey()
    {
        return _key;
    }

    public void setNullValue(String dymmy)
    {
        _type = ManagedPropertyConfig.TYPE_NULL;
    }

    public int getType()
    {
        return _type;
    }

    public void setValue(String value)
    {
        if (FacesUtils.isValueBinding(value))
        {
            // TODO: can we prebuild VB?
            _type     = ManagedPropertyConfig.TYPE_VALUE_BINDING;
            value     = value.trim();
        }
        else
        {
            _type = ManagedPropertyConfig.TYPE_OBJECT;
        }
        _value = value.intern();
    }

    public Object getValue(FacesContext facesContext, Class toClass)
    {
        Object retval = null;

        switch (_type)
        {
            case ManagedPropertyConfig.TYPE_NULL:
                return null;

            case ManagedPropertyConfig.TYPE_VALUE_BINDING:
                if (_valueBinding == null)
                {
                    // potential concurrency, but VB cache will mitigate that
                    _valueBinding = FacesUtils.createValueBinding(facesContext, _value.toString());
                }
                retval = _valueBinding.getValue(facesContext);
                break;

            case ManagedPropertyConfig.TYPE_OBJECT:
                retval = _value;
                break;
        }

        // TODO: convert to toClass
        return retval;
    }

    public void keyToClass(Class keyClass)
    {
        // TODO
    }

    public void updateBean(FacesContext facesContext, Map bean, Class valueClass)
    {
        bean.put(_key, getValue(facesContext, valueClass));
    }
}
