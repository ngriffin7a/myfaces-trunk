/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.el;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import net.sourceforge.myfaces.config.RuntimeConfig;
import net.sourceforge.myfaces.config.element.ManagedBean;
import net.sourceforge.myfaces.util.BiLevelCacheMap;

import org.apache.commons.el.ArraySuffix;
import org.apache.commons.el.Coercions;
import org.apache.commons.el.ComplexValue;
import org.apache.commons.el.ConditionalExpression;
import org.apache.commons.el.Expression;
import org.apache.commons.el.ExpressionString;
import org.apache.commons.el.NamedValue;
import org.apache.commons.el.PropertySuffix;
import org.apache.commons.el.ValueSuffix;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * 
 * $Log$
 * Revision 1.52  2004/10/01 11:54:29  dave0000
 * add detailed error messages for "base is null"
 *
 * Revision 1.51  2004/09/28 19:11:49  dave0000
 * uppercase static final prop
 * remove redundant code
 *
 * Revision 1.50  2004/09/28 18:29:48  dave0000
 * fix for bug 1034332: ValueBinding.getExpressionString() not implemented
 *
 * Revision 1.49  2004/09/20 14:35:48  dave0000
 * bug 1030875:
 * getType() should return null if type cannot be determined
 * isReadOnly() should return false if read-only cannot be determined
 *
 * Revision 1.48  2004/07/27 06:28:34  dave0000
 * fix issue with getType of literal expressions (and other improvements)
 *
 * Revision 1.47  2004/07/07 08:34:58  mwessendorf
 * removed unused import-statements
 *
 * Revision 1.46  2004/07/07 00:25:07  o_rossmueller
 * tidy up config/confignew package (moved confignew classes to package config)
 *
 * Revision 1.45  2004/07/06 23:46:01  o_rossmueller
 * tidy up config/confignew package
 *
 * Revision 1.44  2004/07/01 22:05:12  mwessendorf
 * ASF switch
 *
 * Revision 1.43  2004/06/28 22:12:13  o_rossmueller
 * fix #978654: do not coerce null
 *
 * Revision 1.42  2004/06/16 23:02:24  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.41.2.1  2004/06/16 02:07:23  o_rossmueller
 * get navigation rules from RuntimeConfig
 * refactored all remaining usages of MyFacesFactoryFinder to use RuntimeConfig
 *
 * Revision 1.41  2004/05/31 02:28:37  dave0000
 * Fix for: [955111] RestoreState of outputText with mixed ValueBinding attribute
 *
 * Revision 1.40  2004/05/11 04:24:10  dave0000
 * Bug 943166: add value coercion to ManagedBeanConfigurator
 *
 * Revision 1.39  2004/05/10 05:30:14  dave0000
 * Fix issue with setting Managed Bean to a wrong scope
 *
 * Revision 1.38  2004/04/26 05:54:59  dave0000
 * Add coercion to ValueBinding (and related changes)
 *
 * Revision 1.37  2004/04/08 05:16:45  dave0000
 * change to always use JSF PropertyResolver (was using JSP PR sometimes)
 *
 * Revision 1.36  2004/04/07 09:46:38  tinytoony
 * changed exception handling to show root cause
 *
 * Revision 1.35  2004/04/07 03:21:19  dave0000
 * fix issue with trim()ing of expression strings
 * prepare for PropertyResolver integration
 *
 * Revision 1.34  2004/04/07 01:52:55  dave0000
 * fix set "root" variable - was setting in the wrong scope if new var
 *
 * Revision 1.33  2004/04/07 01:40:13  dave0000
 * fix set "root" variable bug
 *
 * Revision 1.32  2004/03/30 07:40:08  dave0000
 * implement mixed string-reference expressions
 *
 */
public class ValueBindingImpl extends ValueBinding implements StateHolder
{
    //~ Static fields/initializers --------------------------------------------

    static final Log log = LogFactory.getLog(ValueBindingImpl.class);
    
    /**
     * To implement function support, subclass and use a static 
     * initialization block to assign your own function mapper
     */
    protected static FunctionMapper s_functionMapper = new FunctionMapper()
        {
            public Method resolveFunction(String prefix, String localName)
            {
                throw new ReferenceSyntaxException(
                    "Functions not supported in expressions. Function: " 
                    + prefix + ":" + localName);
            }
        };
    
    private static final BiLevelCacheMap s_expressionCache =
        new BiLevelCacheMap(90)
        {
            protected Object newInstance(Object key)
            {
                return ELParserHelper.parseExpression((String) key);
            }
        };

    //~ Instance fields -------------------------------------------------------

    protected Application _application;
    protected String      _expressionString;
    protected Object      _expression;
    
    /**
     * RuntimeConfig is instantiated once per servlet and never changes--we can
     * safely cache it
     */
    private RuntimeConfig   _runtimeConfig;

    //~ Constructors ----------------------------------------------------------

    public ValueBindingImpl(Application application, String expression)
    {
        if (application == null)
        {
            throw new NullPointerException("application");
        }
        
        // Do not trim(), we support mixed string-bindings
        if ((expression == null) || (expression.length() == 0))
        {
            throw new ReferenceSyntaxException("Expression: empty or null");
        }
        _application = application;
        _expressionString  = expression;

        _expression = s_expressionCache.get(expression);
    }

    //~ Methods ---------------------------------------------------------------

    public String getExpressionString()
    {
        return _expressionString;
    }
    
    public boolean isReadOnly(FacesContext facesContext)
    {
        try
        {
            Object base_ = resolveToBaseAndProperty(facesContext);
            if (base_ instanceof String)
            {
                return VariableResolverImpl.s_standardImplicitObjects
                    .containsKey(base_);
            }
            
            Object[] baseAndProperty = (Object[]) base_;
            Object base      = baseAndProperty[0];
            Object property  = baseAndProperty[1];
            
            Integer index = ELParserHelper.toIndex(base, property);
            return (index == null)
                ? _application.getPropertyResolver().isReadOnly(base, property)
                : _application.getPropertyResolver()
                    .isReadOnly(base, index.intValue());
        }
        catch (NotVariableReferenceException e)
        {
            // if it is not a variable reference (e.g., a constant literal), 
            // we cannot write to it but can read it
            return true;
        }
        catch (Exception e)
        {
            // Cannot determine read-only, return false (is this what the spec requires?)
            return false;
        }
    }

    public Class getType(FacesContext facesContext)
    {
        try
        {
            Object base_ = resolveToBaseAndProperty(facesContext);
            if (base_ instanceof String)
            {
                String name = (String) base_;
                
                // Check if it is a ManagedBean
                // WARNING: must do this check first to avoid instantiating
                //          the MB in resolveVariable()
                ManagedBean mbConfig =
                    getRuntimeConfig(facesContext).getManagedBean(name);
                if (mbConfig != null)
                {
                    // Note: if MB Class is not set, will return 
                    //       <code>null</code>, which is a valid return value
                    return mbConfig.getManagedBeanClass();
                }

                Object val = _application.getVariableResolver()
                    .resolveVariable(facesContext, name);
                
                // Note: if there is no ManagedBean or variable with this name
                //       in any scope,then we will create a new one and thus
                //       any Object is allowed.
                return (val != null) ? val.getClass() : Object.class;
            }
            else
            {
                Object[] baseAndProperty = (Object[]) base_;
                Object base      = baseAndProperty[0];
                Object property  = baseAndProperty[1];
                
                Integer index = ELParserHelper.toIndex(base, property);
                return (index == null)
                    ? _application.getPropertyResolver().getType(base, property)
                    : _application.getPropertyResolver()
                        .getType(base, index.intValue());
            }
        }
        catch (NotVariableReferenceException e)
        {
            // It is not a value reference, then it probably is an expression
            // that evaluates to a literal. Get the value and then it's class
            // Note: we could hadle this case in a more performance efficient manner--
            //       but this case is so rare, that for months no-one detected
            //       the error before this code was added.
            try 
            {
                return getValue(facesContext).getClass();
            }
            catch (Exception e1)
            {
                // Cannot determine type, return null per JSF spec
                return null;
            }
        }
        catch (Exception e)
        {
            // Cannot determine type, return null per JSF spec
            return null;
        }
    }

    public void setValue(FacesContext facesContext, Object newValue)
            throws PropertyNotFoundException
    {
        try
        {
            Object base_ = resolveToBaseAndProperty(facesContext);
            if (base_ instanceof String)
            {
                String name = (String) base_;
                if (VariableResolverImpl.s_standardImplicitObjects
                    .containsKey(name))
                {
                    String errorMessage = 
                        "Cannot set value of implicit object '" 
                        + name + "' for expression '" + _expressionString + "'"; 
                    log.error(errorMessage);
                    throw new ReferenceSyntaxException(errorMessage);
                }

                // Note: will be coerced later
                setValueInScope(facesContext, name, newValue);
            }
            else
            {
                Object[] baseAndProperty = (Object[]) base_;
                Object base      = baseAndProperty[0];
                Object property  = baseAndProperty[1];
                PropertyResolver propertyResolver = 
                    _application.getPropertyResolver();

                Integer index = ELParserHelper.toIndex(base, property);
                if (index == null)
                {
                    Class clazz = propertyResolver.getType(base, property);
                    propertyResolver.setValue(
                        base, property, coerce(newValue, clazz));
                }
                else
                {
                    int indexVal = index.intValue();
                    Class clazz = propertyResolver.getType(base, indexVal);
                    propertyResolver.setValue(
                        base, indexVal, coerce(newValue, clazz));
                }
            }
        }
        catch (IndexOutOfBoundsException e) 
        {
            // ArrayIndexOutOfBoundsException also here
            throw new PropertyNotFoundException(
                "Expression: '" + _expressionString + "'", e);
        }
        catch (Exception e)
        {
            if (newValue == null)
            {
                log.error("Cannot set value for expression '" 
                    + _expressionString + "' to null.", e);
            }
            else
            {
                log.error("Cannot set value for expression '" 
                    + _expressionString + "' to a new value of type " 
                    + newValue.getClass().getName(), e);
            }
            throw new EvaluationException(
                "Expression: '" + _expressionString + "'", e);
        }
    }
    
    private void setValueInScope(
        FacesContext facesContext, String name, Object newValue)
    throws ELException
    {
        ExternalContext externalContext = facesContext.getExternalContext();
         
        // Request context
        Map scopeMap = externalContext.getRequestMap();
        Object obj = scopeMap.get(name);
        if (obj != null)
        {
            scopeMap.put(name, coerce(newValue, obj.getClass()));
            return;
        }

        // Session context
        scopeMap = externalContext.getSessionMap();  
        obj = scopeMap.get(name);
        if (obj != null)
        {
            scopeMap.put(name, coerce(newValue, obj.getClass()));
            return;
        }

        // Application context
        scopeMap = externalContext.getApplicationMap();
        obj = scopeMap.get(name);
        if (obj != null)
        {
            scopeMap.put(name, coerce(newValue, obj.getClass()));
            return;
        }
        
        // Check for ManagedBean
        ManagedBean mbConfig =
            getRuntimeConfig(facesContext).getManagedBean(name);
        if (mbConfig != null)
        {
            String scopeName = mbConfig.getManagedBeanScope();
            
            // find the scope handler object
            // Note: this does not handle user-extended _scope values
            Scope scope = 
                (Scope) VariableResolverImpl.s_standardScopes.get(scopeName);
            if (scope != null)
            {
                scope.put(externalContext, name, 
                    coerce(newValue, mbConfig.getManagedBeanClass()));
                return;
            }
            
            log.error("Managed bean '" + name + "' has illegal scope: "
                + scopeName);
            externalContext.getRequestMap().put(name, 
                coerce(newValue, mbConfig.getManagedBeanClass()));
            return;
        }
        
        // unknown target class, put newValue into request scope without coercion 
        externalContext.getRequestMap().put(name, newValue);
    }

    public Object getValue(FacesContext facesContext)
    throws PropertyNotFoundException
    {
        try
        {
            return _expression instanceof Expression 
                ? ((Expression) _expression).evaluate(
                    new ELVariableResolver(facesContext),
                    s_functionMapper, ELParserHelper.LOGGER)
                : ((ExpressionString) _expression).evaluate(
                    new ELVariableResolver(facesContext),
                    s_functionMapper, ELParserHelper.LOGGER);
        }
        catch (IndexOutOfBoundsException e) 
        {
            // ArrayIndexOutOfBoundsException also here
            throw new PropertyNotFoundException(
                "Expression: '" + _expressionString + "'", e);
        }
        catch (Exception e)
        {
            log.error("Cannot get value for expression '" + _expressionString 
                + "'", e);

            if (e instanceof ELException)
            {
                log.error("Root cause for exception : ", 
                    ((ELException) e).getRootCause());
            }

            throw new EvaluationException(
                "Expression: '" + _expressionString + "'", e);
        }
    }
    
    protected Object resolveToBaseAndProperty(FacesContext facesContext) 
        throws ELException, NotVariableReferenceException 
    {
        if (facesContext == null)
        {
            throw new NullPointerException("facesContext");
        }
        
        VariableResolver variableResolver = 
            new ELVariableResolver(facesContext);
        Object expression = _expression;
        
        while (expression instanceof ConditionalExpression)
        {
            ConditionalExpression conditionalExpression = 
                ((ConditionalExpression) expression);
            // first, evaluate the condition (and coerce the result to a
            // boolean value)
            boolean condition =
              Coercions.coerceToBoolean(
                  conditionalExpression.getCondition().evaluate(
                      variableResolver, s_functionMapper, 
                      ELParserHelper.LOGGER), 
                      ELParserHelper.LOGGER)
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
            throw new NotVariableReferenceException(
                "Parsed Expression of unsupported type for this operation. Expression class: "
                    + _expression.getClass().getName() + ". Expression: '"
                    + _expressionString + "'");
        }
        
        ComplexValue complexValue = (ComplexValue) expression;
        
        // resolve the prefix
        Object base = complexValue.getPrefix()
            .evaluate(variableResolver, s_functionMapper, 
                ELParserHelper.LOGGER);
        if (base == null)
        {
            throw new PropertyNotFoundException("Base is null: " 
                + complexValue.getPrefix().getExpressionString());
        }

        // Resolve and apply the suffixes
        List suffixes = complexValue.getSuffixes();
        int max = (suffixes == null) ? -1 : suffixes.size() - 1;
        for (int i = 0; i < max; i++) 
        {
            ValueSuffix suffix = (ValueSuffix) suffixes.get(i);
            base = suffix.evaluate(base, variableResolver, s_functionMapper,
                ELParserHelper.LOGGER);
            if (base == null)
            {
                throw new PropertyNotFoundException("Base is null: " 
                    + suffix.getExpressionString());
            }
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
                        variableResolver, s_functionMapper, 
                        ELParserHelper.LOGGER);
                if (index == null)
                {
                    throw new PropertyNotFoundException("Index is null: " 
                        + arraySuffixIndex.getExpressionString());
                }
                
            }
            else
            {
                index = ((PropertySuffix) arraySuffix).getName();
            }
        }
        
        return new Object[] {base, index};
    }

    private Object coerce(Object value, Class clazz) throws ELException
    {
        return (value == null) ? null
            : (clazz == null) ? value : 
                Coercions.coerce(value, clazz, ELParserHelper.LOGGER);
    }
    
    protected RuntimeConfig getRuntimeConfig(FacesContext facesContext)
    {
        if (_runtimeConfig == null)
        {
            _runtimeConfig = RuntimeConfig.getCurrentInstance(facesContext.getExternalContext());
        }
        return _runtimeConfig;
    }
    
    public String toString()
    {
        return _expressionString;
    }

    //~ State Holder ------------------------------------------------------

    private boolean _transient = false;

    /**
     * Empty constructor, so that new instances can be created when restoring 
     * state.
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
        _expression = s_expressionCache.get(_expressionString);
    }

    public boolean isTransient()
    {
        return _transient;
    }

    public void setTransient(boolean flag)
    {
        _transient = flag;
    }
    
    //~ Internal classes ------------------------------------------------------

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
    
    public static final class NotVariableReferenceException 
        extends ReferenceSyntaxException
    {
        public NotVariableReferenceException(String message)
        {
            super(message);
        }
    }
}
