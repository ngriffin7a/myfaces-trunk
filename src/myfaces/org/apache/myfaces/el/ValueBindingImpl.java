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

import net.sourceforge.myfaces.util.BiLevelCacheMap;
import org.apache.commons.el.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ValueBindingImpl extends ValueBinding implements StateHolder
{
    //~ Static fields/initializers -----------------------------------------------------------------

    static final Log log = LogFactory.getLog(ValueBindingImpl.class);
    private static final Logger s_logger = new Logger(System.out);    
    
    private static final FunctionMapper s_functionMapper = new FunctionMapper()
    {
        public Method resolveFunction(String prefix, String localName)
        {
            throw new ReferenceSyntaxException(
                "Functions not supported in expressions. Function: " 
                + prefix + ":" + localName);
        }
    };
    
    static final ExpressionEvaluatorImpl s_expressionEvaluator = 
        new ExpressionEvaluatorImpl();

    /**
     * Expressions are already cached by JSP EL implementation, 
     * we cache again to avoid string replacement from #{} to ${} on each request
     * Overhead is small, only one extra HashMap
     */
    private static final BiLevelCacheMap s_expressions =
        new BiLevelCacheMap(256, 128, 100)
        {
            protected Object newInstance(Object key)
            {
                try {
                    return s_expressionEvaluator.parseExpressionString(
                        ("${" + stripBracketsFromModelReference((String) key) + '}'));
                }
                catch (ELException e)
                {
                    if (log.isDebugEnabled()) {
                        log.debug("Invalid expression: " + key, e);
                    }
                    throw new ReferenceSyntaxException("Expression: " + key 
                        + " -> " + e.getMessage(), e);
                }
            }
        };

    //~ Instance fields ----------------------------------------------------------------------------

    protected Application _application;
    protected String      _expressionString;
    protected Expression  _expression;

    //~ Constructors -------------------------------------------------------------------------------

    public ValueBindingImpl(Application application, String expression)
    {
        if (application == null)
        {
            throw new NullPointerException("application");
        }
        if ((expression == null) || ((expression = expression.trim()).length() == 0))
        {
            throw new ReferenceSyntaxException("Reference: empty or null");
        }
        _application = application;
        _expressionString  = expression;
        
        Object parsedExpression = s_expressions.get(expression);
        if (!(parsedExpression instanceof Expression))
        {
            throw new IllegalStateException("Parsed Expression of unexpected type " 
                + parsedExpression.getClass().getName()); 
        }
        
        _expression    = (Expression) parsedExpression;
    }

    //~ Methods ------------------------------------------------------------------------------------

    public boolean isReadOnly(FacesContext facesContext)
            throws PropertyNotFoundException
    {
        try
        {
            Object base_ = resolveToBaseAndProperty(facesContext);
            if (!(base_ instanceof Object[]))
            {
                return VariableResolverImpl.s_standardImplicitObjects
                    .containsKey(base_.toString());
            }
            
            Object[] baseAndProperty = (Object[]) base_;
            Object base      = baseAndProperty[0];
            Object property  = baseAndProperty[1];
            
            Integer index = toIndex(base, property);
            return (index == null)
                ? _application.getPropertyResolver().isReadOnly(base, property)
                : _application.getPropertyResolver().isReadOnly(base, index.intValue());
        }
        catch (NotVariableReferenceException e)
        {
            // if it is not a variable reference (e.g., a constant literal), 
            // we cannot write to it but can read it
            return true;
        }
        catch (Exception e)
        {
            log.error("Cannot determine readonly for expression " + _expressionString, e);
            throw new EvaluationException("Expression: " + _expressionString, e);
        }
    }

    public Class getType(FacesContext facesContext)
            throws PropertyNotFoundException
    {
        try
        {
            Object base_ = resolveToBaseAndProperty(facesContext);
            if (!(base_ instanceof Object[]))
            {
                Object val = _application.getVariableResolver()
                    .resolveVariable(facesContext, base_.toString());
                
                // Note: if val==null, then there is no variable with this name
                //       in any scope. Therefore, we will create a new one, so 
                //       any Object is allowed.
                return (val == null) ? Object.class : val.getClass();
            }
            
            Object[] baseAndProperty = (Object[]) base_;
            Object base      = baseAndProperty[0];
            Object property  = baseAndProperty[1];
            
            Integer index = toIndex(base, property);
            return (index == null)
                ? _application.getPropertyResolver().getType(base, property)
                : _application.getPropertyResolver().getType(base, index.intValue());
        }
        catch (Exception e)
        {
            log.error("Cannot get type for expression " + _expressionString, e);
            throw new EvaluationException("Expression: " + _expressionString, e);
        }
    }

    public void setValue(FacesContext facesContext, Object newValue)
            throws PropertyNotFoundException
    {
        try
        {
            Object base_ = resolveToBaseAndProperty(facesContext);
            if (!(base_ instanceof Object[]))
            {
                String name = base_.toString();
                if (VariableResolverImpl.s_standardImplicitObjects.containsKey(name))
                {
                    String errorMessage = "Cannot set value of implicit object '" 
                        + name + "' for expression " + _expressionString; 
                    log.error(errorMessage);
                    throw new ReferenceSyntaxException(errorMessage);
                }

                setValueInScope(facesContext, name, newValue);
            }
            
            Object[] baseAndProperty = (Object[]) base_;
            Object base      = baseAndProperty[0];
            Object property  = baseAndProperty[1];

            Integer index = toIndex(base, property);
            if (index == null)
            {
                _application.getPropertyResolver().setValue(base, property, newValue);
            }
            else
            {
                _application.getPropertyResolver().setValue(base, index.intValue(), newValue);
            }
        }
        catch (Exception e)
        {
            if (newValue == null)
            {
                log.error("Cannot set value for expression " 
                    + _expressionString + " to null.", e);
            }
            else
            {
                log.error("Cannot set value for expression " 
                    + _expressionString + " to a new value of type " 
                    + newValue.getClass().getName(), e);
            }
            throw new EvaluationException("Expression: " + _expressionString, e);
        }
    }
    
    private void setValueInScope(FacesContext facesContext, String name, Object newValue)
    {
        ExternalContext externalContext = facesContext.getExternalContext();
         
        Object obj = null;
        Map scopeMap = null;
        
      findObject: {
            // Request context
            scopeMap = externalContext.getRequestMap();
            obj = scopeMap.get(name);
            if (obj == null)
            {
                break findObject;
            }
    
            // Session context (try to get without creating a new session)
            Object session = externalContext.getSession(false);
            if (session != null)
            {
                scopeMap = externalContext.getSessionMap();  
                obj = scopeMap.get(name);
                if (obj != null)
                {
                    break findObject;
                }
            }
    
            // Application context
            scopeMap = externalContext.getApplicationMap();
            obj = scopeMap.get(name);
            if (obj != null)
            {
                break findObject;
            }
        }
        
        if (scopeMap == null)
        {
            scopeMap = externalContext.getRequestMap();
        }
        
        scopeMap.put(name, newValue);
    }

    public Object getValue(FacesContext facesContext)
    throws PropertyNotFoundException
    {
        try
        {
            return _expression.evaluate (
                new ELVariableResolver(facesContext),
                s_functionMapper, s_logger);
        }
        catch (Exception e)
        {
            log.error("Cannot get value for expression " + _expressionString, e);
            throw new EvaluationException("Expression: " + _expressionString, e);
        }
    }
    
    protected Object resolveToBaseAndProperty(FacesContext facesContext) 
        throws ELException, NotVariableReferenceException 
    {
        if (facesContext == null)
        {
            throw new NullPointerException("facesContext");
        }
        
        VariableResolver variableResolver = new ELVariableResolver(facesContext);
        Expression expression = _expression;
        
        while (expression instanceof ConditionalExpression)
        {
            ConditionalExpression conditionalExpression = ((ConditionalExpression) expression);
            // first, evaluate the condition (and coerce the result to a boolean value)
            boolean condition =
              Coercions.coerceToBoolean(
                  conditionalExpression.getCondition().evaluate(
                      variableResolver, s_functionMapper, s_logger), s_logger)
                  .booleanValue();

            // then, use this boolean to branch appropriately
            expression = condition ? conditionalExpression.getTrueBranch()
                : conditionalExpression.getFalseBranch();
        }

        if (expression instanceof NamedValue)
        {
            return ((NamedValue) expression).getName();
        }
        
        if (!(expression instanceof ComplexValue)) {
            // all other cases are not variable references
            throw new NotVariableReferenceException (
                "Parsed Expression of unsupported type for this operation. Expression class: " 
                + _expression.getClass().getName() + ". Expression: " + _expressionString);
        }
        
        ComplexValue complexValue = (ComplexValue) expression;
        
        // resolve the prefix
        Object base = complexValue.getPrefix()
            .evaluate(variableResolver, s_functionMapper, s_logger);

        // Resolve and apply the suffixes
        List suffixes = complexValue.getSuffixes();
        int max = (suffixes == null) ? -1 : suffixes.size() - 1;
        for (int i = 0; i < max; i++) 
        {
            ValueSuffix suffix = (ValueSuffix) suffixes.get(i);
            base = suffix.evaluate(base, variableResolver, s_functionMapper, s_logger);
        }
        
        // Resolve the last suffix
        Object index = null;
        if (max >= 0)
        {
            ArraySuffix arraySuffix = (ArraySuffix) suffixes.get(max);
            Expression arraySuffixIndex = arraySuffix.getIndex();
            
            if (arraySuffixIndex != null)
            {
                index = arraySuffixIndex.evaluate(
                        variableResolver, s_functionMapper, s_logger);
            }
            else
            {
                index = ((PropertySuffix) arraySuffix).getName();
            }
        }
        
        return (index == null) ? base : new Object[] {base, index};
    }

    /**
     * Coerces <code>index</code> to Integer for array types, or returns 
     * <code>null</code> for non-array types.
     * 
     * @param base Object for the base
     * @param index Object for the index
     * @return Integer a valid Integer index, or null if not an array type
     * 
     * @throws ELException if exception occurs trying to coerce to Integer
     * @throws EvaluationException if base is array type but cannot convert index to Integer
     */
    protected Integer toIndex(Object base, Object index) throws ELException, EvaluationException
    {
        if ((base instanceof List) || (base.getClass().isArray()))
        {
            return coerceToIntegerWrapper(base, index);
        }
        if (base instanceof UIComponent)
        {
            try
            {
                return coerceToIntegerWrapper(base, index);
            }
            catch (Throwable t)
            {
                // treat as simple property
                return null;
            }
        }

        // If not an array type
        return null;
    }
    
    private Integer coerceToIntegerWrapper(Object base, Object index)
        throws EvaluationException, ELException
    {
        Integer integer = Coercions.coerceToInteger(index, s_logger);
        if (integer != null)
        {
            return integer;
        }
        throw new EvaluationException("Cannot convert index to int for base " 
            + base.getClass().getName() + " and index " + index);
    }

    /**
     * Strip "#{" and "}" from a modelReference, if any
     *
     * @param expressionString the model reference expression
     *
     * @return the model reference, with "#{" and "}" removed
     */
    static String stripBracketsFromModelReference(String expressionString)
    {
        if (expressionString.startsWith("#{") && expressionString.endsWith("}"))
        {
            return expressionString.substring(2, expressionString.length() - 1);
        }
        else
        {
            throw new ReferenceSyntaxException(
                "Reference: " + expressionString + ". Reference must be enclosed in #{ }");
        }
    }

    //~ StateHolder implementation ----------------------------------------------------------------------------
    
    private boolean _transient = false;

    /**
     * Empty constructor, so that new instances can be created when restoring state.
     */
    public ValueBindingImpl()
    {
        _application = null;
        _expressionString = null;
        _expression = null;
    }

    public Object saveState(FacesContext facesContext)
    {
        return _expressionString;
    }

    public void restoreState(FacesContext facesContext, Object obj)
    {
        _application = facesContext.getApplication();
        _expressionString  = (String) obj;
        _expression = (Expression) s_expressions.get(_expressionString);
    }

    public boolean isTransient()
    {
        return _transient;
    }

    public void setTransient(boolean flag)
    {
        _transient = flag;
    }
    
    //~ Internal classes ----------------------------------------------------------------------------

    public static class ELVariableResolver implements VariableResolver {
        private final FacesContext _facesContext;
        
        public ELVariableResolver(FacesContext facesContext) 
        {
            _facesContext = facesContext;
        }
        
        public Object resolveVariable(String pName)
            throws ELException
        {
            return _facesContext.getApplication().getVariableResolver()
                .resolveVariable(_facesContext, pName);
        }
    }
    
    public static final class NotVariableReferenceException extends ReferenceSyntaxException
    {
        public NotVariableReferenceException(String message)
        {
            super(message);
        }
    }
}
