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

package net.sourceforge.myfaces.confignew;

import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class TestPropertyResolver extends PropertyResolver
{

    private PropertyResolver delegate;


    public TestPropertyResolver()
    {
    }


    public TestPropertyResolver(PropertyResolver delegate)
    {
        this.delegate = delegate;
    }


    // METHODS
    public Class getType(Object base, int index) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getType(base, index);
    }


    public Class getType(Object base, Object property) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getType(base, property);
    }


    public Object getValue(Object base, int index) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getValue(base, index);
    }


    public Object getValue(Object base, Object property) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getValue(base, property);
    }


    public boolean isReadOnly(Object base, int index) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.isReadOnly(base, index);
    }


    public boolean isReadOnly(Object base, Object property) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.isReadOnly(base, property);
    }


    public void setValue(Object base, int index, Object value) throws EvaluationException, PropertyNotFoundException
    {
        delegate.setValue(base, index, value);
    }


    public void setValue(Object base, Object property, Object value) throws EvaluationException, PropertyNotFoundException
    {
        delegate.setValue(base, property, value);
    }


    public PropertyResolver getDelegate()
    {
        return delegate;
    }
}
