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
package net.sourceforge.myfaces.el;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ValueBinding;
import java.lang.reflect.Method;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MethodBindingImpl
    extends MethodBinding
    implements StateHolder
{
    //~ Instance fields ----------------------------------------------------------------------------

    ValueBindingImpl _valueBinding;
    Class[]          _argClasses;

    //~ Constructors -------------------------------------------------------------------------------

    public MethodBindingImpl(Application application, String reference, Class[] argClasses)
    {
        // NOTE: getting ValueBindingImpl through application will utilize the caching
        //
        // ValueBindingImp will check application == null and ref == null
        _valueBinding = (ValueBindingImpl) application.createValueBinding(reference);
        if (_valueBinding._parsedReference.length < 2)
        {
            throw new MethodNotFoundException(
                "Reference: " + _valueBinding._reference
                + ". MethodBinding reference cannot be just #{var}");
        }

        _argClasses = argClasses;
    }

    //~ Methods ------------------------------------------------------------------------------------

    public String getExpressionString()
    {
        return _valueBinding._reference;
    }

    public Class getType(FacesContext context)
    {
        if (context == null)
        {
            throw new NullPointerException("facesContext");
        }

        Object base = _valueBinding.resolve(context);
        if (base == null)
        {
            throw new MethodNotFoundException(
                "Reference: " + _valueBinding._reference + ", base: null");
        }

        Method method =
            getMethod(
                context, base,
                _valueBinding._parsedReference[_valueBinding._parsedReference.length - 1],
                _argClasses);
        return method.getReturnType();
    }

    public Object invoke(FacesContext context, Object[] args)
            throws EvaluationException, MethodNotFoundException
    {
        if (context == null)
        {
            throw new NullPointerException("facesContext");
        }

        Object base = _valueBinding.resolve(context);
        if (base == null)
        {
            throw new MethodNotFoundException(
                "Reference: " + _valueBinding._reference + ", base: null");
        }

        Method method =
            getMethod(
                context, base,
                _valueBinding._parsedReference[_valueBinding._parsedReference.length - 1],
                _argClasses);

        try
        {
            return method.invoke(base, args);
        }
        catch (MethodNotFoundException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new EvaluationException("Error invoking method binding: " + _valueBinding._reference, e);
        }
    }

    protected Method getMethod(FacesContext facesContext,
                               Object base,
                               Object name,
                               Class[] argClasses)
        throws MethodNotFoundException
    {
        if (name instanceof ValueBinding)
        {
            name = ((ValueBinding) name).getValue(facesContext);
        }
        if (name == null)
        {
            throw new MethodNotFoundException(
                "Reference: " + _valueBinding._reference + ", base: " + base.getClass()
                + ", method name resolved to null from: " + ((ValueBindingImpl) name)._reference);
        }

        try
        {
            return base.getClass().getMethod(_valueBinding.coerceToString(name), argClasses);
        }
        catch (NoSuchMethodException e)
        {
            throw new MethodNotFoundException(
                "Reference: " + _valueBinding._reference + ", base: " + base.getClass()
                + ", method: " + name.toString(), e);
        }
    }


    //~ StateHolder support ----------------------------------------------------------------------------

    private boolean _transient = false;

    /**
     * Empty constructor, so that new instances can be created when restoring state.
     */
    public MethodBindingImpl()
    {
        _valueBinding = null;
        _argClasses = null;
    }

    public Object saveState(FacesContext facescontext)
    {
        return new Object[] {_valueBinding.saveState(facescontext), _argClasses};
    }

    public void restoreState(FacesContext facescontext, Object obj)
    {
        Object[] ar = (Object[])obj;
        _valueBinding = new ValueBindingImpl();
        _valueBinding.restoreState(facescontext, ar[0]);
        _argClasses = (Class[])ar[1];
    }

    public boolean isTransient()
    {
        return _transient;
    }

    public void setTransient(boolean flag)
    {
        _transient = flag;
    }

}
