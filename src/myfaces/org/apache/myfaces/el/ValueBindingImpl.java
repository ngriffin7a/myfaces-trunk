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

import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;

/**
 * JSF 1.0 PRD2, 5.2.3
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValueBindingImpl
    extends ValueBinding
{
    private String _reference;
    private VariableResolver _variableResolver;
    private PropertyResolver _propertyResolver;

    public ValueBindingImpl(String reference,
                            VariableResolver variableResolver,
                            PropertyResolver propertyResolver)
    {
        _reference = BeanUtils.stripBracketsFromModelReference(reference);
        _variableResolver = variableResolver;
        _propertyResolver = propertyResolver;
    }


    public Class getType(FacesContext facesContext) throws PropertyNotFoundException
    {
        int i = _reference.indexOf('.');
        if (i == -1)
        {
            return _variableResolver.resolveVariable(facesContext, _reference).getClass();
        }
        else
        {
            String objName = _reference.substring(0, i);
            String propName = _reference.substring(i + 1);
            Object obj = _variableResolver.resolveVariable(facesContext, objName);
            return _propertyResolver.getType(obj, propName);
        }
    }

    public Object getValue(FacesContext facesContext) throws PropertyNotFoundException
    {
        int i = _reference.indexOf('.');
        if (i == -1)
        {
            return _variableResolver.resolveVariable(facesContext, _reference);
        }
        else
        {
            String objName = _reference.substring(0, i);
            String propName = _reference.substring(i + 1);
            Object obj = _variableResolver.resolveVariable(facesContext, objName);
            return _propertyResolver.getValue(obj, propName);
        }
    }

    public boolean isReadOnly(FacesContext facesContext) throws PropertyNotFoundException
    {
        int i = _reference.indexOf('.');
        if (i == -1)
        {
            return true;
        }
        else
        {
            String objName = _reference.substring(0, i);
            String propName = _reference.substring(i + 1);
            Object obj = _variableResolver.resolveVariable(facesContext, objName);
            return _propertyResolver.isReadOnly(obj, propName);
        }
    }

    public void setValue(FacesContext facesContext, Object v) throws PropertyNotFoundException
    {
        int i = _reference.indexOf('.');
        if (i == -1)
        {
            throw new UnsupportedOperationException("Reference '" + _reference + "' cannot be set.");
        }
        else
        {
            String objName = _reference.substring(0, i);
            String propName = _reference.substring(i + 1);
            Object obj = _variableResolver.resolveVariable(facesContext, objName);
            _propertyResolver.setValue(obj, propName, v);
        }
    }
}
