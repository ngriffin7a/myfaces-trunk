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
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.servlet.jsp.el.ELException;

import net.sourceforge.myfaces.el.ValueBindingImpl.NotVariableReferenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MethodBindingImpl extends MethodBinding implements StateHolder
{
    static final Log log = LogFactory.getLog(MethodBindingImpl.class);
    
    //~ Instance fields ----------------------------------------------------------------------------

    ValueBindingImpl _valueBinding;
    Class[]          _argClasses;

    //~ Constructors -------------------------------------------------------------------------------

    public MethodBindingImpl(Application application, String reference, Class[] argClasses)
    {
        // Note: using ValueBindingImpl, istead of creating a common subclass,
        //       to share single Expression cache
        _valueBinding = new ValueBindingImpl(application, reference);
        _argClasses = argClasses;
    }

    //~ Methods ------------------------------------------------------------------------------------

    public String getExpressionString()
    {
        return _valueBinding._expressionString;
    }

    public Class getType(FacesContext facesContext)
    {
        try
        {
            Object[] baseAndProperty = resolveToBaseAndProperty(facesContext);
            Object base              = baseAndProperty[0];
            Object property          = baseAndProperty[1];
            
            return base.getClass().getMethod(property.toString(), _argClasses)
                .getReturnType();
        }
        catch (ReferenceSyntaxException e) {
            throw e;
        }
        catch (IndexOutOfBoundsException e) 
        {
            // ArrayIndexOutOfBoundsException also here
            throw new PropertyNotFoundException("Expression: " + getExpressionString(), e);
        }
        catch (Exception e)
        {
            log.error("Cannot get type for expression " + getExpressionString(), e);
            throw new EvaluationException("Expression: " + getExpressionString(), e);
        }
    }

    public Object invoke(FacesContext facesContext, Object[] args)
            throws EvaluationException, MethodNotFoundException
    {
        try
        {
            Object[] baseAndProperty = resolveToBaseAndProperty(facesContext);
            Object base              = baseAndProperty[0];
            Object property          = baseAndProperty[1];
            
            return base.getClass().getMethod(property.toString(), _argClasses)
                .invoke(base, args);
        }
        catch (ReferenceSyntaxException e) {
            throw e;
        }
        catch (IndexOutOfBoundsException e) 
        {
            // ArrayIndexOutOfBoundsException also here
            throw new PropertyNotFoundException("Expression: " + getExpressionString(), e);
        }
        catch (Exception e)
        {
            log.error("Cannot get type for expression " + getExpressionString(), e);
            throw new EvaluationException("Expression: " + getExpressionString(), e);
        }
    }

    protected Object[] resolveToBaseAndProperty(FacesContext facesContext) throws ELException 
    {
        if (facesContext == null)
        {
            throw new NullPointerException("facesContext");
        }
        
        try
        {
            Object base = _valueBinding.resolveToBaseAndProperty(facesContext);
        
            if (!(base instanceof Object[]))
            {
                String errorMessage = "Expression not a valid method binding: " 
                    + getExpressionString(); 
                log.error(errorMessage);
                throw new ReferenceSyntaxException(errorMessage);
            }
            
            return (Object[]) base;
        }
        catch (NotVariableReferenceException e)
        {
            throw new ReferenceSyntaxException("Expression: " + getExpressionString(), e);
        }
    }
    
    //~ StateHolder implementation ------------------------------------------------------------------------------------
    
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
