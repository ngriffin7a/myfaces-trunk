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

import net.sourceforge.myfaces.el.PropertyResolverImpl;

import javax.faces.context.FacesContext;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ManagedPropertyConfig extends PropertyConfig
{
    //~ Static fields/initializers -----------------------------------------------------------------

    static final int          TYPE_NOT_SET       = 0;
    static final int          TYPE_NULL          = 1;
    static final int          TYPE_OBJECT        = 2;
    static final int          TYPE_VALUE_BINDING = 3;
    static final int          TYPE_MAP           = 4;
    static final int          TYPE_LIST          = 5;

    //~ Instance fields ----------------------------------------------------------------------------

    private ListEntriesConfig _listEntriesConfig;
    private MapEntriesConfig  _mapEntriesConfig;
    private Object            _value;
    private ValuesConfig      _valuesConfig;
    private int               _type;

    //~ Methods ------------------------------------------------------------------------------------

    public void setListEntriesConfig(ListEntriesConfig listEntriesConfig)
    {
        _listEntriesConfig = listEntriesConfig;
    }

    public ListEntriesConfig getListEntriesConfig()
    {
        return _listEntriesConfig;
    }

    public void setMapEntriesConfig(MapEntriesConfig mapEntriesConfig)
    {
        _mapEntriesConfig = mapEntriesConfig;
    }

    public MapEntriesConfig getMapEntriesConfig()
    {
        return _mapEntriesConfig;
    }

    public void setNullValue(String dummy)
    {
        _type = TYPE_NULL;
    }

    public void setValue(String value)
    {
        //TODO: convert to property type
        _value = value.intern();
    }

    public void setValuesConfig(ValuesConfig valuesConfig)
    {
        _valuesConfig = valuesConfig;
    }

    public ValuesConfig getValuesConfig()
    {
        return _valuesConfig;
    }

    public void updateBean(FacesContext facesContext, Object bean)
    {
        switch (_type)
        {
            case TYPE_NULL:
                PropertyResolverImpl.setProperty(bean, getPropertyName(), null);
                return;

            case TYPE_OBJECT:

                // TODO: convert to property class
                PropertyResolverImpl.setProperty(bean, getPropertyName(), _value);
                return;

            case TYPE_VALUE_BINDING:

                // TODO: convert to property class
                PropertyResolverImpl.setProperty(bean, getPropertyName(), _value);
                return;

            case TYPE_MAP:
                _mapEntriesConfig.updateBean(
                    facesContext, bean, getPropertyName(), getPropertyClass());
                return;

            case TYPE_LIST:
                _listEntriesConfig.updateBean(
                    facesContext, bean, getPropertyName(), getPropertyClass());
                return;

            default:
                throw new IllegalStateException();
        }
    }
}
