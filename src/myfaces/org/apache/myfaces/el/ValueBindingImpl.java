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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;


/**
 * JSF 1.0 PRD2, 5.2.3
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ValueBindingImpl extends ValueBinding {
    //~ Static fields/initializers ---------------------------------------------

    private static final Integer ZERO_FROM_EMPTY = new Integer(0);
    private static final Integer ZERO = new Integer(0);
    private static final Integer ONE  = new Integer(1);

    //~ Instance fields --------------------------------------------------------

    protected Application _application;
    protected String      _reference;
    protected Object[]    _parsedReference;

    //~ Constructors -----------------------------------------------------------

    public ValueBindingImpl(String reference, Application application) {
        _application         = application;
        _reference           = reference;
        _parsedReference     = parse(
                stripBracketsFromModelReference(reference));
    }

    //~ Methods ----------------------------------------------------------------

    public boolean isReadOnly(FacesContext facesContext)
    throws PropertyNotFoundException
    {
        Object base = resolve(facesContext);

        if (base == null)
            throw new NullPointerException(
                "Null bean, property: " + _reference);

        int maxIndex = _parsedReference.length - 1;

        if (maxIndex > 0)
            return isPropertyReadOnly(
                facesContext, base, _parsedReference[maxIndex]);

        return true;
    }

    public Class getType(FacesContext facesContext)
    throws PropertyNotFoundException
    {
        Object base = resolve(facesContext);

        if (base == null)
            throw new NullPointerException(
                "Null bean, property: " + _reference);

        int maxIndex = _parsedReference.length - 1;

        if (maxIndex > 0)
            return getPropertyType(
                facesContext, base, _parsedReference[maxIndex]);

        return base.getClass();
    }

    public void setValue(FacesContext facesContext, Object newValue)
    throws PropertyNotFoundException
    {
        // TODO: allow setting of base class (myfaces extension)
        Object base = resolve(facesContext);

        if (base == null)
            throw new NullPointerException(
                "Null bean, property: " + _reference);

        int maxIndex = _parsedReference.length - 1;

        if (maxIndex > 0)
            setPropertyValue(
                facesContext, base, _parsedReference[maxIndex], newValue);
        else
            throw new EvaluationException("Cannot set base class");
    }

    public Object getValue(FacesContext facesContext)
    throws PropertyNotFoundException
    {
        Object base = resolve(facesContext);

        if (base == null)
            return null; // (see JSF 1.0, PRD2, 5.1.2.1)

        int maxIndex = _parsedReference.length - 1;

        if (maxIndex > 0)
            base =
                getPropertyValue(
                    facesContext, base, _parsedReference[maxIndex]);

        return base;
    }

    protected boolean isPropertyReadOnly(
        FacesContext facesContext, Object base, Object name) {
        name = preprocessProperty(facesContext, base, name);

        // Map is a special case, need to use the String property
        return (name instanceof String)
        ? _application.getPropertyResolver().isReadOnly(base, (String)name)
        : _application.getPropertyResolver().isReadOnly(
            base, ((Integer)name).intValue());
    }

    protected Class getPropertyType(
        FacesContext facesContext, Object base, Object name) {
        name = preprocessProperty(facesContext, base, name);

        // Map is a special case, need to use the String property
        return (name instanceof String)
        ? _application.getPropertyResolver().getType(base, (String)name)
        : _application.getPropertyResolver().getType(
            base, ((Integer)name).intValue());
    }

    protected void setPropertyValue(
        FacesContext facesContext, Object base, Object name, Object newValue) {
        name = preprocessProperty(facesContext, base, name);

        // Map is a special case, need to use the String property
        if (name instanceof String)
            _application.getPropertyResolver().setValue(
                base, (String)name, newValue);
        else
            _application.getPropertyResolver().setValue(
                base, ((Integer)name).intValue(), newValue);
    }

    /**
     * Gets the value of property <code>name</code> from object
     * <code>base</code>. Call the appropriate
     * <code>PropertyResolver.getValue</code> method based on the type (int or
     * String) of <code>name</code>
     *
     * @param facesContext facesContext to resolve abgainst
     * @param base the bean whose property to get
     * @param name the name or index of the property
     *
     * @return the value of requested property
     */
    protected Object getPropertyValue(
        FacesContext facesContext, Object base, Object name) {
        name = preprocessProperty(facesContext, base, name);

        if (name == ZERO_FROM_EMPTY)
            return null; // (see JSF 1.0, PRD2, 5.1.2.1)

        return (name instanceof String)
        ? _application.getPropertyResolver().getValue(base, (String)name)
        : _application.getPropertyResolver().getValue(
            base, ((Integer)name).intValue());
    }

    protected Object resolve(FacesContext facesContext) {
        Object base =
            _application.getVariableResolver().resolveVariable(
                facesContext, (String)_parsedReference[0]);

        return resolve(facesContext, base, 1);
    }

    protected Object resolve(FacesContext facesContext, Object base, int start) {
        for (int i = start, max = _parsedReference.length - 1; i < max; i++) {
            Object curProperty = _parsedReference[i];

            base = getPropertyValue(facesContext, base, curProperty);

            if (base == null)
                return null; // (see JSF 1.0, PRD2, 5.1.2.1)
        }

        return base;
    }

    /**
     * Get a simple (not nested) property name and type (int or String) as
     * defined in JSF 1.0, PRD2, 5.1.2
     *
     * <p>
     * We need this function, because <code>PropertyResolver</code> does not
     * provide <code>getValue</code> function where the property is of type
     * <code>Object</code>. Therefore, we must decide which
     * <code>getValue</code> function to call
     * </p>
     *
     * @param base the bean to access
     * @param name the property to access, will be coerced to String or Integer
     *        as needed
     *
     * @return the name or index of the property
     */
    private Object coerceProperty(Object base, Object name) {
        if ((base == null) || (name == null))
            return null; // (see JSF 1.0, PRD2, 5.1.2.1)

        if ((base instanceof List) || (base.getClass().isArray()))

            // Note: ReferenceSyntaxException would be thrown by coerceToInt(), if needed
            return coerceToInteger(name);

        if (base instanceof UIComponent) {
            try {
                return coerceToInteger(name);
            } catch (Throwable t) {
                return coerceToString(name);
            }
        }

        // If none of the special bean types (or Map)
        return coerceToString(name);
    }

    /**
     * Coerces the supplied object to Integer based on coercion rules defined
     * in JSF 1.0, PRD2, 5.1.2.4
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
    private Integer coerceToInteger(Object obj) {
        if (obj == null)
            return ZERO_FROM_EMPTY; // (see JSF 1.0, PRD2, 5.1.2.4)

        if (obj instanceof String) {
            String s = (String)obj;

            if (s.length() == 0)
                return ZERO_FROM_EMPTY; // (see JSF 1.0, PRD2, 5.1.2.4)

            try {
                return Integer.valueOf(s);
            } catch (NumberFormatException e) {
                throw new ReferenceSyntaxException(e);
            }
        }

        if (obj instanceof Integer)
            return (Integer)obj;

        if (obj instanceof Number)
            return new Integer(((Number)obj).intValue());

        if (obj instanceof Character)

            // REVISIT: per spec, convert to Short first--what's the point of converting to Short to get an int???
            //			 (see JSF 1.0, PRD2, 5.1.2.4)
            return new Integer(((Character)obj).charValue());

        // WARNING: JSF 1.0, PRD2, 5.1.2.4 requires that we throw ReferenceSyntaxException
        //   		for Boolean. The following implementation violates the spec
        if (obj instanceof Boolean)
            return ((Boolean)obj).booleanValue() ? ONE : ZERO;

        // JSF spec mentiones about coercion of primitive types,
        //   we do not handle the primitive numeric types here,
        //   since there is no way to pass those to this function
        throw new ReferenceSyntaxException(
            "Unable to coerce " + obj.getClass() + " to int");
    }

    /**
     * Coerces the supplied object to String based on coercion rules defined in
     * JSF 1.0, PRD2, 5.1.2.4
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
    private String coerceToString(Object obj) {
        if (obj == null)
            return "";

        try {
            return obj.toString();
        } catch (Throwable t) {
            throw new ReferenceSyntaxException(
                "Unable to coerce " + obj.getClass() + " to String", t);
        }
    }

    /**
     * Return the index of the matching (posibly multi-level nested) closing
     * bracket
     *
     * @param str string to search param indexofOpeningBracket the start index
     * @param indexofOpeningBracket the location of opening bracket to match
     *
     * @return the index of the matching closing bracket
     *
     * @throws ReferenceSyntaxException if matching bracket cannot be found
     */
    private static int indexOfMatchingClosingBracket(
        String str, int indexofOpeningBracket) {
        int curpos = indexofOpeningBracket + 1;

        int nestingDepth = 1;
        int indexofOpen  = str.indexOf('[', curpos);
        int indexofClose = str.indexOf(']', curpos);

        for (int i = indexofOpeningBracket, len = str.length(); i < len;) {
            if (indexofClose < 0)
                throw new ReferenceSyntaxException(
                    "Invalid property '" + str + "'--missing ']'");

            // We check for '\' before the bracket to skip quoted brackets
            if ((indexofOpen < 0) || (indexofClose < indexofOpen)) {
                if (
                    ((indexofClose == 0)
                        || (str.charAt(indexofClose - 1) != '\\'))
                        && (--nestingDepth == 0))
                    return indexofClose;

                i = indexofClose = str.indexOf(']', indexofClose + 1);
            } else {
                if ((indexofOpen == 0) || (str.charAt(indexofOpen - 1) != '\\'))
                    nestingDepth++;

                i = indexofOpen = str.indexOf('[', indexofOpen + 1);
            }
        }

        throw new ReferenceSyntaxException(
            "Invalid property '" + str + "'--missing ']'");
    }

    private Object preprocessProperty(
        FacesContext facesContext, Object base, Object name) {
        // Map is a special case, need to force property to String
        return (name instanceof ValueBinding)
        ? coerceProperty(base, ((ValueBinding)name).getValue(facesContext))
        : ((base instanceof Map) ? name.toString() : name);
    }

    /**
     * Strip "${" and "}" from a modelReference, if any
     *
     * @param modelReference the model reference
     *
     * @return the model reference, with "${" and "}" removed
     */
    private static String stripBracketsFromModelReference(
        String modelReference) {
        modelReference = modelReference.trim();

        if (modelReference.startsWith("${") && modelReference.endsWith("}"))
            return modelReference.substring(2, modelReference.length() - 1);
        else

            return modelReference;
    }

    private Object getIndex(
        String reference, int indexofOpeningBracket, int indexofClosingBracket) {
        char quote = reference.charAt(indexofOpeningBracket + 1);

        // Case 1: index is a string literal (must be quoted with ' or ")
        if ((quote == '"') || (quote == '\'')) {
            // One of var["name"] or var['name']
            // check for cases a[''] and no closing quote
            if (
                (reference.charAt(indexofClosingBracket - 1) != quote)
                    || (indexofOpeningBracket >= (indexofClosingBracket - 3)))
                throw new ReferenceSyntaxException(
                    "Invalid indexed property: " + reference);

            return unescape(
                reference.substring(
                    indexofOpeningBracket + 2, indexofClosingBracket - 1));
        }

        String index =
            reference.substring(
                indexofOpeningBracket + 1, indexofClosingBracket);

        // Case 2: index is integer (e.g., for arrays)
        if (isUnsignedInteger(index))
            return Integer.valueOf(index);

        return _application.getValueBinding(index);
    }

    private static boolean isUnsignedInteger(String str) {
        int len = str.length();

        if (len == 0)
            return false;

        for (int i = 0; i < len; i++)
            if (!Character.isDigit(str.charAt(i)))
                return false;

        return true;
    }

    private Object[] parse(String reference) {
        if ((reference == null) || (reference.length() == 0))
            throw new ReferenceSyntaxException(
                "Invalid reference: " + reference);

        ArrayList parsedReference = new ArrayList();

        for (int pos = 0, len = reference.length(); pos < len;) {
            // Process indexed property
            if (reference.charAt(pos) == '[') {
                // check for case like 'a.b.[0]'
                // Note: case '[0]' is ok as this may not be the first element
                if ((pos == 0) || (reference.charAt(pos - 1) == '.'))
                    throw new ReferenceSyntaxException(
                        "Invalid indexed property '" + reference
                        + "'--'[' following '.'");

                int indexofClosingBracket =
                    indexOfMatchingClosingBracket(reference, pos);

                // Is index empty? (case 'a.b[]')
                if (pos == (indexofClosingBracket - 1))
                    throw new ReferenceSyntaxException(
                        "Invalid indexed property '" + reference
                        + "'--empty index");

                Object index = getIndex(reference, pos, indexofClosingBracket);
                parsedReference.add(index);
                pos = indexofClosingBracket + 1;

                continue;
            }

            // Not an indexed property, process simple nesting
            int newpos = reference.indexOf('[', pos);
            int indexofDot = reference.indexOf('.', pos);

            // Find the first occurrence of any delim char
            if ((indexofDot >= 0) && (indexofDot < newpos))
                newpos = indexofDot;
                
            if (newpos < 0)
                newpos = len;

            // newpos is the end of the property name
            if (pos == newpos)
                if (pos == 0)
                    throw new ReferenceSyntaxException(
                        "Invalid property '" + reference + "'--starting with '"
                        + reference.charAt(pos) + "'");
                else if (reference.charAt(pos - 1) == ']') {
                    // case 'a[0].b', skip the dot
                    pos++;

                    continue;
                } else

                    // name is empty (case 'a..b')?
                    throw new ReferenceSyntaxException(
                        "Invalid property '" + reference + "'--double '.'");

            parsedReference.add(reference.substring(pos, newpos));
            pos = (newpos == indexofDot) ? (newpos + 1) : newpos;
        }

        return parsedReference.toArray();
    }

    private static String unescape(String str) {
        int indexofBackslash = str.indexOf('\\');

        if (indexofBackslash < 0)
            return str; // nothing to do

        int          lastIndex = str.length() - 1;
        StringBuffer sb     = new StringBuffer(lastIndex);
        int          curpos = 0;

        do {
            // check for ["ashklhj\"] error
            if (indexofBackslash == lastIndex)
                throw new ReferenceSyntaxException(
                    "'\\' at the end of index string '" + str + "'");

            sb.append(str.substring(curpos, indexofBackslash));
            curpos               = indexofBackslash + 1;
            indexofBackslash     = str.indexOf('\\', curpos + 1);
        } while (indexofBackslash >= 0);

        return sb.append(str.substring(curpos)).toString();
    }
}
