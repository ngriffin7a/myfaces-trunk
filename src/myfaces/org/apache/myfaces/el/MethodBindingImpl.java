/*
 * MyFaces - the free JSF implementation Copyright (C) 2003, 2004 The MyFaces
 * Team (http://myfaces.sourceforge.net)
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.el;

import net.sourceforge.myfaces.el.ValueBindingImpl.NotVariableReferenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.*;
import javax.faces.event.AbortProcessingException;
import javax.faces.validator.ValidatorException;
import javax.servlet.jsp.el.ELException;
import java.lang.reflect.InvocationTargetException;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.13  2004/05/11 04:24:10  dave0000
 * Bug 943166: add value coercion to ManagedBeanConfigurator
 *
 * Revision 1.12  2004/04/16 15:13:31  manolito
 * validator attribute support and MethodBinding invoke exception handling fixed
 *
 */
public class MethodBindingImpl extends MethodBinding
    implements StateHolder
{
    static final Log log = LogFactory.getLog(MethodBindingImpl.class);

    //~ Instance fields -------------------------------------------------------

    ValueBindingImpl _valueBinding;
    Class[]          _argClasses;

    //~ Constructors ----------------------------------------------------------

    public MethodBindingImpl(Application application, String reference,
        Class[] argClasses)
    {
        // Note: using ValueBindingImpl, istead of creating a common subclass,
        //       to share single Expression cache
        // Note: we can trim() reference, since string-binding mixed
        //       expressions are not allowed for MethodBindings
        _valueBinding = new ValueBindingImpl(application, reference.trim());
        _argClasses = argClasses;
    }

    //~ Methods ---------------------------------------------------------------

    public String getExpressionString()
    {
        return _valueBinding._expressionString;
    }

    public Class getType(FacesContext facesContext)
    {
        try
        {
            Object[] baseAndProperty = resolveToBaseAndProperty(facesContext);
            Object base = baseAndProperty[0];
            Object property = baseAndProperty[1];

            return base.getClass().getMethod(property.toString(), _argClasses)
                .getReturnType();
        }
        catch (ReferenceSyntaxException e)
        {
            throw e;
        }
        catch (IndexOutOfBoundsException e)
        {
            // ArrayIndexOutOfBoundsException also here
            throw new PropertyNotFoundException("Expression: "
                + getExpressionString(), e);
        }
        catch (Exception e)
        {
            log.error(
                "Cannot get type for expression " + getExpressionString(), e);
            throw new EvaluationException("Expression: "
                + getExpressionString(), e);
        }
    }

    public Object invoke(FacesContext facesContext, Object[] args)
        throws EvaluationException, MethodNotFoundException
    {
        try
        {
            Object[] baseAndProperty = resolveToBaseAndProperty(facesContext);
            Object base = baseAndProperty[0];
            Object property = baseAndProperty[1];

            return base.getClass().getMethod(property.toString(), _argClasses)
                .invoke(base, args);
        }
        catch (ReferenceSyntaxException e)
        {
            throw e;
        }
        catch (IndexOutOfBoundsException e)
        {
            // ArrayIndexOutOfBoundsException also here
            throw new PropertyNotFoundException("Expression: "
                + getExpressionString(), e);
        }
        catch (InvocationTargetException e)
        {
            Throwable cause = e.getCause();
            if (cause != null)
            {
                if (cause instanceof ValidatorException ||
                    cause instanceof AbortProcessingException)
                {
                    throw new EvaluationException(cause);
                }
                else
                {
                    log.error("Exception while invoking expression "
                        + getExpressionString(), cause);
                    throw new EvaluationException("Expression: "
                        + getExpressionString(), cause);
                }
            }
            else
            {
                log.error("Exception while invoking expression "
                    + getExpressionString(), e);
                throw new EvaluationException("Expression: "
                    + getExpressionString(), e);
            }
        }
        catch (Exception e)
        {
            log.error("Exception while invoking expression "
                + getExpressionString(), e);
            throw new EvaluationException("Expression: "
                + getExpressionString(), e);
        }
    }

    protected Object[] resolveToBaseAndProperty(FacesContext facesContext)
        throws ELException
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
            throw new ReferenceSyntaxException("Expression: "
                + getExpressionString(), e);
        }
    }
    
    public String toString()
    {
        return _valueBinding.toString();
    }

    //~ StateHolder implementation --------------------------------------------

    private boolean _transient = false;

    /**
     * Empty constructor, so that new instances can be created when restoring
     * state.
     */
    public MethodBindingImpl()
    {
        _valueBinding = null;
        _argClasses = null;
    }

    public Object saveState(FacesContext facescontext)
    {
        return new Object[] { _valueBinding.saveState(facescontext),
            _argClasses};
    }

    public void restoreState(FacesContext facescontext, Object obj)
    {
        Object[] ar = (Object[]) obj;
        _valueBinding = new ValueBindingImpl();
        _valueBinding.restoreState(facescontext, ar[0]);
        _argClasses = (Class[]) ar[1];
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