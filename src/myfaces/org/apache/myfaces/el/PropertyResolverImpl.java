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
package net.sourceforge.myfaces.el;

import net.sourceforge.myfaces.util.bean.BeanUtils;

import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import java.beans.PropertyDescriptor;

/**
 * JSF 1.0 PRD2, 5.2.2
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class PropertyResolverImpl
    extends PropertyResolver
{
    public Class getType(Object obj, String s) throws PropertyNotFoundException
    {
        if (obj == null)
        {
            throw new NullPointerException("Cannot get type of property '" + s + "' for null bean.");
        }

        try
        {
            return BeanUtils.getBeanPropertyType(obj, s);
        }
        catch (IllegalArgumentException e)
        {
            throw new PropertyNotFoundException(e);
        }
    }

    public Object getValue(Object obj, String s) throws PropertyNotFoundException
    {
        if (obj == null)
        {
            throw new NullPointerException("Cannot get value of property '" + s + "' for null bean.");
        }

        try
        {
            return BeanUtils.getBeanPropertyValue(obj, s);
        }
        catch (IllegalArgumentException e)
        {
            throw new PropertyNotFoundException(e);
        }
    }

    public boolean isReadOnly(Object obj, String s) throws PropertyNotFoundException
    {
        if (obj == null)
        {
            throw new NullPointerException("Cannot determine readonly of property '" + s + "' for null bean.");
        }

        PropertyDescriptor pd = BeanUtils.findBeanPropertyDescriptor(obj, s);
        if (pd == null)
        {
            throw new PropertyNotFoundException("Object " + obj + " does not have a property '" + s + "'.");
        }
        return pd.getWriteMethod() == null;
    }

    public void setValue(Object obj, String s, Object v) throws PropertyNotFoundException
    {
        try
        {
            BeanUtils.setBeanPropertyValue(obj, s, v);
        }
        catch (IllegalArgumentException e)
        {
            throw new PropertyNotFoundException(e);
        }
    }




    public Class getType(Object obj, int i) throws PropertyNotFoundException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Object getValue(Object obj, int i) throws PropertyNotFoundException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isReadOnly(Object obj, int i) throws PropertyNotFoundException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setValue(Object obj, int i, Object v) throws PropertyNotFoundException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
