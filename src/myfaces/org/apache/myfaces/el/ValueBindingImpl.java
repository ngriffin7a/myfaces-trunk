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
    static final int            INTEGER_CACHE_LOWER = -1000;
    static final int            INTEGER_CACHE_UPPER = 1000;
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
        _reference           = reference;
        _parsedReference     = parse(stripBracketsFromModelReference(reference));
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
        ? _application.getPropertyResolver().isReadOnly(base, (String) name)
        : _application.getPropertyResolver().isReadOnly(base, ((Integer) name).intValue());
    }

    private Class getPropertyType(FacesContext facesContext, Object base, Object name)
    {
        name = coerceProperty(facesContext, base, name);

        return (name instanceof String)
        ? _application.getPropertyResolver().getType(base, (String) name)
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
            _application.getPropertyResolver().setValue(base, (String) name, newValue);
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

        return (name instanceof String)
        ? _application.getPropertyResolver().getValue(base, (String) name)
        : _application.getPropertyResolver().getValue(base, ((Integer) name).intValue());
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
            // Note: ReferenceSyntaxException would be thrown by coerceToInteger(), if needed
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

        // If none of the special bean types (or is Map)
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

    private boolean isEscaped(String str, int pos)
    {
        int escapeCharCount = 0;
        while ((--pos >= 0) && (str.charAt(pos) == '\\'))
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
            return unescape(index.substring(1, len - 1));
        }

        // Case 2: index is an integer (e.g., for arrays)
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
        int pos = indexofOpeningBracket + 1;
        if (pos >= len)
        {
            throw new ReferenceSyntaxException(
                "Reference: " + _reference
                + ". Index incorrectly terminated: missing closing bracket");
        }

        char c = str.charAt(pos);

        // 1. If quoted literal, find closing quote
        if ((c == '"') || (c == '\''))
        {
            pos = indexOfMatchingClosingQuote(str, pos, c) + 1;
            if ((pos < len) && (str.charAt(pos) == ']'))
            {
                return pos;
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
            int indexofOpen  = str.indexOf('[', pos);
            int indexofClose = str.indexOf(']', pos);
            if (indexofClose < 0)
            {
                // No closing bracket
                throw new ReferenceSyntaxException(
                    "Reference: " + _reference
                    + ". Index incorrectly terminated: missing closing bracket");
            }
            if ((indexofOpen < 0) || (indexofClose < indexofOpen))
            {
                // There is no opening bracket, or closing bracket is before opening
                return indexofClose;
            }
            else
            {
                // Closing bracket after opening--we have nested brackets
                pos = indexOfMatchingClosingBracket(str, indexofOpen) + 1;

                // (pos >= len) will cause indexofClose to be -1 on the next iteration
                // and properly reported as error, therefore we do not check for this case explicitly
            }
        }
    }

    /**
     * Returns the index of the matching closing quote, checking for escaped quotes
     *
     * @param str string to scan
     * @param indexOfOpeningQuote start from this position in the string
     * @param quote the quote char
     * @return -1 if no match, the index of closing quote otherwise
     */
    private int indexOfMatchingClosingQuote(String str, int indexOfOpeningQuote, char quote)
    {
        for (
            int pos = str.indexOf(quote, indexOfOpeningQuote + 1); pos >= 0;
                    pos = str.indexOf(quote, pos + 1))
        {
            if (!isEscaped(str, pos))
            {
                return pos;
            }
        }

        // No matching quote found
        return -1;
    }

    // NOTE: after adding all the functionality, this function has become overly complicated, should rewrite
    private Object[] parse(String reference)
    {
        List parsedReference = new ArrayList();

        for (int pos = 0, len = reference.length(); pos < len;)
        {
            // Process indexed property
            if (reference.charAt(pos) == '[')
            {
                // check for case like 'a.b.[0]'
                // Note: case '[0]' is ok as this may not be the first element
                if ((pos == 0) || (reference.charAt(pos - 1) == '.'))
                {
                    throw new ReferenceSyntaxException(
                        "Reference: " + _reference + ". Invalid indexed property '[' following '.'");
                }

                int    indexofClosingBracket = indexOfMatchingClosingBracket(reference, pos);
                Object index = index(reference.substring(pos + 1, indexofClosingBracket).trim());
                parsedReference.add(index);
                pos = indexofClosingBracket + 1;

                continue;
            }

            // Not an indexed property, process simple nesting
            int indexofBracket = reference.indexOf('[', pos);
            int indexofDot = reference.indexOf('.', pos);
            int newpos     = len;

            // Find the first occurrence of any delim char
            if ((indexofDot >= 0) && (indexofDot < newpos))
            {
                newpos = indexofDot;
            }
            if ((indexofBracket >= 0) && (indexofBracket < newpos))
            {
                newpos = indexofBracket;
            }

            // newpos is the end of the property name
            String propname = reference.substring(pos, newpos).trim();
            if (propname.length() == 0)
            {
                if (pos == 0)
                {
                    throw new ReferenceSyntaxException(
                        "Reference: " + _reference + ". Invalid property starting with '"
                        + reference.charAt(pos) + "'");
                }
                else if (reference.charAt(pos - 1) == ']')
                {
                    // case 'a[0].b' or 'a[0][1]' (or, whitespace between . and [ or ] and [), skip
                    pos = (newpos == indexofDot) ? (newpos + 1) : newpos;

                    continue;
                }
                else
                {
                    // name is empty (case 'a..b')?
                    throw new ReferenceSyntaxException(
                        "Reference: " + _reference + ". Invalid property, double '.'");
                }
            }

            parsedReference.add(propname);
            pos = (newpos == indexofDot) ? (newpos + 1) : newpos;
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
        int          pos = 0;
        do
        {
            // check for ["ashklhj\"] error
            if (indexofBackslash == lastIndex)
            {
                throw new ReferenceSyntaxException(
                    "Reference: " + _reference + ". '\\' at the end of index string '" + str + "'");
            }

            sb.append(str.substring(pos, indexofBackslash));
            pos                  = indexofBackslash + 1;
            indexofBackslash     = str.indexOf('\\', pos + 1);
        }
        while (indexofBackslash >= 0);

        return sb.append(str.substring(pos)).toString();
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

    public Object saveState(FacesContext facescontext)
    {
        return _reference;
    }

    public void restoreState(FacesContext facescontext, Object obj)
    {
        _application = facescontext.getApplication();
        _reference = (String)obj;
        _parsedReference = parse(stripBracketsFromModelReference(_reference));
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
