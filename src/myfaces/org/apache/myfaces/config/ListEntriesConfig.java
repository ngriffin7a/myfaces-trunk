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
import net.sourceforge.myfaces.util.FacesUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.ValueBinding;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ListEntriesConfig
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final String NULL        = "null";

    //~ Instance fields ----------------------------------------------------------------------------

    private Class _valueClass;
    private List  _valueList; // List to maintain ordering (is it needed here?)

    //~ Methods ------------------------------------------------------------------------------------

    public void setNullValue(String dymmy)
    {
        _valueList.add(NULL);
    }

    public void setValue(String value)
    {
        Object actualValue;
        if (FacesUtils.isValueBinding(value))
        {
            // TODO: can we prebuild VB?
            actualValue = value.trim().intern();
        }
        else
        {
            actualValue = value.intern();
        }
        _valueList.add(actualValue);
    }

    public void setValueClass(String valueClass)
    {
        _valueClass = ClassUtils.javaTypeToClass(valueClass);
    }

    public Class getValueClass()
    {
        return _valueClass;
    }

    public void updateBean(FacesContext facesContext, List list)
    {
        for (int i = 0, len = _valueList.size(); i < len; i++)
        {
            Object value = _valueList.get(i);
            if (value instanceof ValueBinding)
            {
                value = ((ValueBinding) value).getValue(facesContext);
            }

            // TODO: convert to value class
            list.add(value);
        }
    }

    public void updateBean(
        FacesContext facesContext, Object bean, String propName, Class propClass)
    {
        boolean isNew;
        List    list;
        try
        {
            list      = (List) PropertyResolverImpl.getProperty(bean, propName);
            isNew     = false;
        }
        catch (Exception e)
        {
            try
            {
                list =
                    (propClass != null) ? (List) propClass.newInstance()
                                        : new ArrayList(_valueList.size());
            }
            catch (Exception e1)
            {
                throw new EvaluationException("Unable to instantiate: " + propClass, e1);
            }
            isNew = true;
        }

        updateBean(facesContext, list);

        if (isNew)
        {
            PropertyResolverImpl.setProperty(bean, propName, list);
        }
    }
}
