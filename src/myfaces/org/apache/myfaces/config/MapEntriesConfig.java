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
import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.util.HashMapUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class MapEntriesConfig
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Class _keyClass;
    private Class _valueClass;
    private List  _mapEntryConfigList; // List to maintain ordering (is it needed here?)

    //~ Methods ------------------------------------------------------------------------------------

    public void setKeyClass(String keyClass)
    {
        _keyClass = ClassUtils.javaTypeToClass(keyClass);
        for (int i = 0, len = _mapEntryConfigList.size(); i < len; i++)
        {
            ((MapEntryConfig) _mapEntryConfigList.get(i)).keyToClass(_keyClass);
        }
    }

    public Class getKeyClass()
    {
        return _keyClass;
    }

    public List getMapEntryConfigList()
    {
        return _mapEntryConfigList;
    }

    public void setValueClass(Class valueClass)
    {
        _valueClass = valueClass;
    }

    public Class getValueClass()
    {
        return _valueClass;
    }

    public void addMapEntryConfig(MapEntryConfig mapEntryConfig)
    {
        if (_keyClass != null)
        {
            mapEntryConfig.keyToClass(_keyClass);
        }
        _mapEntryConfigList.add(mapEntryConfig);
    }

    public void updateBean(FacesContext facesContext, Map map)
    {
        for (int i = 0, len = _mapEntryConfigList.size(); i < len; i++)
        {
            ((MapEntryConfig) _mapEntryConfigList.get(i)).updateBean(
                facesContext, map, _valueClass);
        }
    }

    public void updateBean(
        FacesContext facesContext, Object bean, String propName, Class propertyClass)
    {
        boolean isNew;
        Map     map;
        try
        {
            map       = (Map) PropertyResolverImpl.getProperty(bean, propName);
            isNew     = false;
        }
        catch (Exception e)
        {
            try
            {
                map = (propertyClass != null) ? (Map) propertyClass.newInstance()
                                              : new HashMap(
                        HashMapUtils.calcCapacity(_mapEntryConfigList.size()));
            }
            catch (Exception e1)
            {
                throw new EvaluationException("Unable to instantiate: " + propertyClass, e1);
            }
            isNew = true;
        }

        updateBean(facesContext, map);

        if (isNew)
        {
            PropertyResolverImpl.setProperty(bean, propName, map);
        }
    }
}
