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

import net.sourceforge.myfaces.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ValueBindingImpl
    extends ValueBinding
    implements StateHolder
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Log    log                 = LogFactory.getLog(ValueBindingImpl.class);

    // Cache of commonly used Integer instances
    static final Integer        ZERO                = new Integer(0);
    static final Integer        ONE                 = new Integer(1);
    static final int            INTEGER_CACHE_LOWER = 0; // array/List indexes start from 0
    static final int            INTEGER_CACHE_UPPER = 100; // unlikely for user to use constant indexes above 100
    static final Integer[]      INTEGER_CACHE       = createIntegerCache();

    //~ Instance fields ----------------------------------------------------------------------------

    protected Application _application;
    protected String      _reference;
    protected Object[]    _parsedReference;

    //~ Constructors -------------------------------------------------------------------------------

    public ValueBindingImpl(Application application, String reference)
    {
        if (application == null)
        {
            throw new NullPointerException("application");
        }
        if ((reference == null) || ((reference = reference.trim()).length() == 0))
        {
            throw new ReferenceSyntaxException("Reference: empty or null");
        }
        _application         = application;
        _reference           = reference.intern();
        _parsedReference     = parse(stripBracketsFromModelReference(_reference).intern());
    }

    //~ Methods ------------------------------------------------------------------------------------

    public boolean isReadOnly(FacesContext facesContext)
            throws PropertyNotFoundException
    {
        try
        {
            Object base = resolve(facesContext);
            if (base == null)
            {
                throw new PropertyNotFoundException("Reference: " + _reference + ". Null base bean");
            }

            int maxIndex = _parsedReference.length - 1;
            if (maxIndex > 0)
            {
                return isPropertyReadOnly(facesContext, base, _parsedReference[maxIndex]);
            }

            return true;
        }
        catch (RuntimeException e)
        {
            log.error("Exception determining readonly for value reference " + _reference, e);
            throw e;
        }
    }

    public Class getType(FacesContext facesContext)
            throws PropertyNotFoundException
    {
        try
        {
            Object base = resolve(facesContext);
            if (base == null)
            {
                throw new PropertyNotFoundException("Reference: " + _reference + ". Null base bean");
            }

            int maxIndex = _parsedReference.length - 1;
            if (maxIndex > 0)
            {
                return getPropertyType(facesContext, base, _parsedReference[maxIndex]);
            }

            return base.getClass();
        }
        catch (RuntimeException e)
        {
            log.error("Exception determining type of value reference " + _reference, e);
            throw e;
        }
    }

    public void setValue(FacesContext facesContext, Object newValue)
            throws PropertyNotFoundException
    {
        try
        {
            int maxIndex = _parsedReference.length - 1;
            if (maxIndex > 0)
            {
                Object base = resolve(facesContext);
                if (base == null)
                {
                    throw new PropertyNotFoundException(
                        "Reference: " + _reference + ". Null base bean");
                }
                setPropertyValue(facesContext, base, _parsedReference[maxIndex], newValue);
            }
            else
            {
                facesContext.getExternalContext().getRequestMap().put(
                    _parsedReference[0], newValue);
            }
        }
        catch (RuntimeException e)
        {
            if (newValue == null)
            {
                log.error("Exception setting value of reference " + _reference + " to null", e);
            }
            else
            {
                log.error("Exception setting value of reference " + _reference + " to a new value of type " +
                          newValue.getClass().getName(), e);

            }
            throw e;
        }
    }

    public Object getValue(FacesContext facesContext)
            throws PropertyNotFoundException
    {
        try
        {
            Object base = resolve(facesContext);
            if (base == null)
            {
                return null;
            }

            int maxIndex = _parsedReference.length - 1;
            if (maxIndex > 0)
            {
                base = getPropertyValue(facesContext, base, _parsedReference[maxIndex]);
            }

            return base;
        }
        catch (RuntimeException e)
        {
            log.error("Exception getting value of reference " + _reference, e);
            throw e;
        }
    }


    private static Integer integer(int i)
    {
        if ((i >= INTEGER_CACHE_LOWER) && (i <= INTEGER_CACHE_UPPER))
        {
            return INTEGER_CACHE[i - INTEGER_CACHE_LOWER];
        }
        return new Integer(i);
    }

    protected boolean isPropertyReadOnly(FacesContext facesContext, Object base, Object name)
    {
        name = coerceProperty(facesContext, base, name);

        return (name instanceof String)
        ? _application.getPropertyResolver().isReadOnly(base, name)
        : _application.getPropertyResolver().isReadOnly(base, ((Integer) name).intValue());
    }

    private Class getPropertyType(FacesContext facesContext, Object base, Object name)
    {
        name = coerceProperty(facesContext, base, name);

        return (name instanceof String)
        ? _application.getPropertyResolver().getType(base, name)
        : _application.getPropertyResolver().getType(base, ((Integer) name).intValue());
    }

    private void setPropertyValue(FacesContext facesContext,
                                  Object base,
                                  Object name,
                                  Object newValue)
    {
        name = coerceProperty(facesContext, base, name);
        if (name instanceof String)
        {
            _application.getPropertyResolver().setValue(base, name, newValue);
        }
        else
        {
            _application.getPropertyResolver().setValue(
                base, ((Integer) name).intValue(), newValue);
        }
    }

    /**
     * Gets the value of property <code>name</code> from object
     * <code>base</code>. Calls the appropriate
     * <code>PropertyResolver.getValue</code> method based on the type (int or
     * String) of <code>name</code>
     *
     * @param facesContext facesContext to resolve against
     * @param base the bean whose property to get
     * @param name the name or index of the property
     *
     * @return the value of requested property
     */
    private Object getPropertyValue(FacesContext facesContext, Object base, Object name)
    {
        name = coerceProperty(facesContext, base, name);
        if (name == null)
        {
            return null;
        }

        return (name instanceof Integer)
            ? _application.getPropertyResolver().getValue(base, ((Integer) name).intValue())
            : _application.getPropertyResolver().getValue(base, name);
    }

    /**
     * Get the name of a simple (not nested) property (as String) or index of array
     * or <code>List</code> (as Integer) based on the <code>base</code> type
     *
     * <p>
     * We need this function, because <code>PropertyResolver</code> does not
     * provide <code>getValue</code> function where the property is of type
     * <code>Object</code>. Therefore, we must decide which
     * <code>getValue</code> function to call. We also need to get the value
     * of <code>name</code>, when it is a <code>ValueBinding</code> expression
     * </p>
     *
     * @param facesContext the current context to resolve against
     * @param base the bean, whose property will be accessed
     * @param name the property/index to access, will be coerced to String or Integer
     *        as needed
     *
     * @return the name or index of the property
     */
    private Object coerceProperty(FacesContext facesContext, Object base, Object name)
    {
// FIXME: only bean guaranteed non-null
//        Both guaranteed by caller not to be null
//        if ((base == null) || (name == null))
//        {
//            return null; // (see JSF 1.0, PRD2, 5.1.2.1)
//        }
//
        if (name instanceof ValueBinding)
        {
            name = ((ValueBinding) name).getValue(facesContext);
        }
        if ((base.getClass().isArray()) || (base instanceof List))
        {
            // ReferenceSyntaxException would be thrown by coerceToInteger(), if needed
            return coerceToInteger(name);
        }
        if (base instanceof UIComponent)
        {
            try
            {
                return coerceToInteger(name);
            }
            catch (Throwable t)
            {
                return coerceToString(name);
            }
        }

        // If none of the special bean types (or bean is Map)
        return coerceToString(name);
    }

    /**
     * Coerces the supplied object to String based on coercion rules defined for JSP EL
     *
     * <p>
     * Note: null object coerced to empty string--per JSF
     * </p>
     *
     * @param obj the object to coerce
     *
     * @return String the String value of <code>obj</code>
     *
     * @throws ReferenceSyntaxException on eny error during coercion
     */
    protected String coerceToString(Object obj)
    {
        if (obj == null)
        {
            return "";
        }

        try
        {
            return obj.toString();
        }
        catch (Throwable t)
        {
            throw new ReferenceSyntaxException(
                "Reference: " + _reference + ". Unable to coerce " + obj.getClass() + " to String",
                t);
        }
    }

    protected Object resolve(FacesContext facesContext)
    {
        Object base =
            _application.getVariableResolver().resolveVariable(
                facesContext, (String) _parsedReference[0]);

        return resolve(facesContext, base, 1);
    }

    private Object resolve(FacesContext facesContext, Object base, int start)
    {
        for (int i = start, max = _parsedReference.length - 1; i < max; i++)
        {
            base = getPropertyValue(facesContext, base, _parsedReference[i]);
            if (base == null)
            {
                return null;
            }
        }

        return base;
    }

    private static Integer[] createIntegerCache()
    {
        Integer[] integerCache = new Integer[INTEGER_CACHE_UPPER - INTEGER_CACHE_LOWER + 1];

        for (int i = 0, val = INTEGER_CACHE_LOWER; val <= INTEGER_CACHE_UPPER; i++, val++)
        {
            integerCache[i] = new Integer(val);
        }

        integerCache[0]     = ZERO;
        integerCache[1]     = ONE;

        return integerCache;
    }

    private boolean isEscaped(String str, int i)
    {
        int escapeCharCount = 0;
        while ((--i >= 0) && (str.charAt(i) == '\\'))
        {
            escapeCharCount++;
        }

        return (escapeCharCount % 2) != 0;
    }

    /**
     * Coerces the supplied object to Integer based on coercion rules defined for JSP EL
     *
     * <p>
     * Note: null object or empty string are coerced to 0 (zero)--per JSF
     * </p>
     *
     * @param obj the object to coerce
     *
     * @return int the int value of <code>obj</code>
     *
     * @throws ReferenceSyntaxException on eny error during coercion
     */
    private Integer coerceToInteger(Object obj)
    {
        if (obj == null)
        {
            return ZERO;
        }
        if (obj instanceof String)
        {
            String s = (String) obj;
            if (s.length() == 0)
            {
                return ZERO; // empty strings are considered to be 0
            }

            try
            {
                return integer(Integer.parseInt(s));
            }
            catch (NumberFormatException e)
            {
                throw new ReferenceSyntaxException(
                    "Reference: " + _reference + ". Unable to coerce '" + s + "' to int", e);
            }
        }
        if (obj instanceof Integer)
        {
            return (Integer) obj;
        }
        if (obj instanceof Number)
        {
            return integer(((Number) obj).intValue());
        }
        if (obj instanceof Boolean)
        {
            return ((Boolean) obj).booleanValue() ? ONE : ZERO;
        }
        if (obj instanceof Character)
        {
            return integer(((Character) obj).charValue());
        }

        throw new ReferenceSyntaxException(
            "Reference: " + _reference + ". Unable to coerce " + obj.getClass() + " to int");
    }

    /**
     * Returns an index converted to the proper class depending on index type
     *
     * @param index the index to be processed
     * @return String (for a "named" index), Integer (for a numeric index), ValueBinding (for a subexpression)
     */
    private Object index(String index)
    {
        index = index.trim();
        int len = index.length();

        // Is index empty? (case 'var[]')
        if (len == 0)
        {
            throw new ReferenceSyntaxException(
                "Reference: " + _reference + ". Invalid indexed property--empty index");
        }

        char quote = index.charAt(0);

        // Case 1: index is a string literal (if quoted with ' or ")
        if ((quote == '"') || (quote == '\''))
        {
            // One of var["name"] or var['name']
            // check for cases var[''] (empty index constant) and missing closing quote
            if ((index.charAt(len - 1) != quote) || ((len - 3) < 0))
            {
                throw new ReferenceSyntaxException(
                    "Reference: " + _reference + ". Invalid indexed property");
            }

            // NOTE: this is quoted text--must be used exactly as specified--DO NOT trim()
            return unescape(index.substring(1, len - 1)).intern();
        }

        // Case 2: index is a positive integer (e.g., for arrays)
        if (StringUtils.isUnsignedInteger(index))
        {
            return integer(Integer.parseInt(index));
        }
        
        // If neither of the above, then must be a sub-reference 
        return _application.createValueBinding("#{" + index + '}');
    }

    /**
     * Return the index of the matching (posibly multi-level nested) closing
     * bracket
     *
     * @param str string to search
     * @param indexofOpeningBracket the location of opening bracket to match
     *
     * @return the index of the matching closing bracket
     *
     * @throws ReferenceSyntaxException if matching bracket cannot be found
     */
    private int indexOfMatchingClosingBracket(String str, int indexofOpeningBracket)
    {
        int len = str.length();
        int i = indexofOpeningBracket + 1;
        if (i >= len)
        {
            throw new ReferenceSyntaxException(
                "Reference: " + _reference
                + ". Index incorrectly terminated: missing closing bracket");
        }

        char c = str.charAt(i);

        // 1. If quoted literal, find closing quote
        if ((c == '"') || (c == '\''))
        {
            i = indexOfMatchingClosingQuote(str, i, c) + 1;
            if ((i < len) && (str.charAt(i) == ']'))
            {
                return i;
            }
            else
            {
                throw new ReferenceSyntaxException(
                    "Reference: " + _reference
                    + ". Index incorrectly terminated: missing closing quote");
            }
        }

        // 2. Otherwise, find closing bracket
        for (;;)
        {
            int indexofClose = str.indexOf(']', i);
            if (indexofClose < 0)
            {
                // No closing bracket
                throw new ReferenceSyntaxException(
                    "Reference: " + _reference
                    + ". Index incorrectly terminated: missing closing bracket");
            }
            int indexofOpen  = str.indexOf('[', i);
            if ((indexofOpen < 0) || (indexofClose < indexofOpen))
            {
                // There is no opening bracket, or closing bracket is before opening
                return indexofClose;
            }
            else
            {
                // Closing bracket after opening--we have nested brackets
                i = indexOfMatchingClosingBracket(str, indexofOpen) + 1;

                // (i >= len) will cause indexofClose to be -1 on the next iteration
                // and properly reported as error, therefore we do not check for this case explicitly
            }
        }
    }

    /**
     * Returns the index of the matching closing quote, skipping over escaped quotes
     *
     * @param str string to scan
     * @param indexOfOpeningQuote start from this position in the string
     * @param quote the quote char
     * @return -1 if no match, the index of closing quote otherwise
     */
    private int indexOfMatchingClosingQuote(String str, int indexOfOpeningQuote, char quote)
    {
        for (
            int i = str.indexOf(quote, indexOfOpeningQuote + 1); i >= 0;
                    i = str.indexOf(quote, i + 1))
        {
            if (!isEscaped(str, i))
            {
                return i;
            }
        }

        // No matching quote found
        return -1;
    }

    /**
     * Parses the reference into an array of property token objects.
     * Each property tocken object can either be a String or an Integer.
     * 
     * <p>
     * NOTE: this parser supports extended (non-JSF comliant) syntax--it allows any
     * whitespace characters to be inserted in the reference around 
     * delimiter characters '.', '[', and ']' for readability,
     * similarly to how it is accepted in modern languages. Drawback is that each
     * differently formatted reference, even if the same actual reference, will
     * be cached by Application separately (in other words, whitespace counts when caching). 
     * Examples:
     * <pre>
     *     obj[0]
     *     obj[ 0 ]
     *     obj . prop [ "name" ]
     *     obj[ obj2[0] ]
     *     obj . prop [ obj2 . prop2 . [ 'name2' ] ]
     * </pre>
     * </p> 
     * 
     * @param reference unparsed string reference without '#{' and '}'
     * @return array of parsed tokens
     */
    private Object[] parse(String reference)
    {
        List parsedReference = new ArrayList();

        for (int i = 0, len = reference.length(); i < len;)
        {
            // Find the first occurrence of any delim char
            int indexofDelim = len;
            int indexofDot = reference.indexOf('.', i);
            if ((indexofDot >= 0) && (indexofDot < indexofDelim))
            {
                indexofDelim = indexofDot;
            }
            int indexofBracket = reference.indexOf('[', i);
            if ((indexofBracket >= 0) && (indexofBracket < indexofDelim))
            {
                indexofDelim = indexofBracket;
            }

            String propname = reference.substring(i, indexofDelim).trim();
            if (propname.length() == 0)
            {
                // we have a whitespace substring:
                //     i is the index of the first WS char
                //         and the index after the previous delim char (inlc. ']')
                //     indexofDelim is the index after the last WS char
                //         and the index of the next delim char
                // or we have an empty substring:
                //     i == indexofDelim, and are the index after the previous delim char (incl. ']')
                //         and the index of the next delim char
                
                if (i == 0)
                {
                    // reference (sans whitespace) starts with a delim char
                    throw new ReferenceSyntaxException(
                        "Reference: " + _reference + ". Invalid property starting with '"
                        + reference.charAt(indexofDelim) + "'");
                }
                
                if (indexofDelim == indexofDot) 
                {
                    if (reference.charAt(i - 1) == ']') 
                    {
                        // case '].' (with optional WS between ']' and '.'), skip WS and dot
                        i = indexofDelim + 1;
                        continue;
                    }

                    // name is empty (case 'a..b')
                    throw new ReferenceSyntaxException(
                        "Reference: " + _reference + ". Invalid property: contains '..'");
                }

                if (indexofDelim == indexofBracket) 
                {
                    // check for case like '.[' (example 'a.[0]') ignoring optional WS
                    if (reference.charAt(i - 1) == '.')
                    {
                        throw new ReferenceSyntaxException(
                            "Reference: " + _reference + ". Invalid indexed property: contains '.['");
                    }
    
                    // this is the start of an indexed property (example 'a[0]') with optional WS before it
                    int indexofClosingBracket = indexOfMatchingClosingBracket(reference, indexofDelim);
                    parsedReference.add(
                        index(reference.substring(indexofDelim + 1, indexofClosingBracket)));
                    i = indexofClosingBracket + 1;
    
                    continue;
                }

                // we have indexofDelim == len
                
                // property ends with ']' followed by whitespace
                if (reference.charAt(i - 1) == ']') {
                    break;
                }
                
                // property ends with '.' followed by whitespace
                throw new ReferenceSyntaxException(
                        "Reference: " + _reference + ". Invalid property: ends with '.'");
            }

            // simple property (example 'obj.propName')
            parsedReference.add(propname.intern());
            // if next delimiter is a dot, skip over it
            i = (indexofDelim == indexofDot) ? (indexofDelim + 1) : indexofDelim;
        }

        return parsedReference.toArray();
    }

    /**
     * Strip "#{" and "}" from a modelReference, if any
     *
     * @param modelReference the model reference
     *
     * @return the model reference, with "#{" and "}" removed
     */
    private String stripBracketsFromModelReference(String modelReference)
    {
        if (modelReference.startsWith("#{") && modelReference.endsWith("}"))
        {
            return modelReference.substring(2, modelReference.length() - 1);
        }
        else
        {
            throw new ReferenceSyntaxException(
                "Reference: " + _reference + ". Reference must be enclosed in #{ }");
        }
    }

    // NOTE: this function MUST NOT trim() the value
    private String unescape(String str)
    {
        int indexofBackslash = str.indexOf('\\');
        if (indexofBackslash < 0)
        {
            return str; // nothing to do
        }

        int          lastIndex = str.length() - 1;
        StringBuffer sb  = new StringBuffer(lastIndex);
        int          i = 0;
        do
        {
            // check for ["ashklhj\"] error
            if (indexofBackslash == lastIndex)
            {
                throw new ReferenceSyntaxException(
                    "Reference: " + _reference + ". '\\' at the end of index string '" + str + "'");
            }

            sb.append(str.substring(i, indexofBackslash));
            i                  = indexofBackslash + 1;
            indexofBackslash     = str.indexOf('\\', i + 1);
        }
        while (indexofBackslash >= 0);

        return sb.append(str.substring(i)).toString();
    }
    

    //~ StateHolder support ----------------------------------------------------------------------------

    private boolean _transient = false;

    /**
     * Empty constructor, so that new instances can be created when restoring state.
     */
    public ValueBindingImpl()
    {
        _application = null;
        _reference = null;
        _parsedReference = null;
    }

    public Object saveState(FacesContext facesContext)
    {
        return _reference;
    }

    public void restoreState(FacesContext facesContext, Object obj)
    {
        
        Application application = facesContext.getApplication();
        _application = application;

        // utilize Application ValueBinding cache as much as possible (can we do better?)
        ValueBindingImpl valueBinding = (ValueBindingImpl) application.createValueBinding((String) obj);
        _reference = valueBinding._reference; // get from there since it is intern()-ed
        _parsedReference = valueBinding._parsedReference;
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
