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
package net.sourceforge.myfaces.renderkit;

import net.sourceforge.myfaces.util.HashMapUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.10  2004/05/18 14:31:40  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.9  2004/04/28 10:38:33  tinytoony
 * child is of type added to exception message
 *
 * Revision 1.8  2004/04/28 10:37:14  tinytoony
 * child is of type added to exception message
 *
 * Revision 1.7  2004/04/28 08:17:11  tinytoony
 * child is of type added to exception message
 *
 * Revision 1.6  2004/04/07 08:21:10  manolito
 * handling of select items with label == null
 *
 * Revision 1.5  2004/04/06 15:33:21  manolito
 * getStringValue must return submitted value if any
 *
 */
public class RendererUtils
{
    private static final Log log = LogFactory.getLog(RendererUtils.class);

    public static final String SELECT_ITEM_LIST_ATTR = RendererUtils.class.getName() + ".LIST";


    public static String getStringValue(FacesContext facesContext,
                                        UIComponent component)
    {
        if (!(component instanceof ValueHolder))
        {
            throw new IllegalArgumentException("Component is not a ValueHolder");
        }

        if (component instanceof EditableValueHolder)
        {
            Object submittedValue = ((EditableValueHolder)component).getSubmittedValue();
            if (submittedValue != null)
            {
                if (submittedValue instanceof String)
                {
                    return (String)submittedValue;
                }
                else
                {
                    throw new IllegalArgumentException("Expected submitted value of type String");
                }
            }
        }

        Object value = ((ValueHolder)component).getValue();

        Converter converter = ((ValueHolder)component).getConverter();
        if (converter == null  && value != null)
        {
            if (value instanceof String)
            {
                return (String) value;
            }

            try
            {
                converter = facesContext.getApplication().createConverter(value.getClass());
            }
            catch (FacesException e)
            {
                log.error("No converter for class " + value.getClass().getName() + " found (component id=" + component.getId() + ").");
                // converter stays null
            }
        }

        if (converter == null)
        {
            if (value == null)
            {
                return "";
            }
            else
            {
                return value.toString();
            }
        }
        else
        {
            return converter.getAsString(facesContext, component, value);
        }
    }

    /**
     * See JSF Spec. 8.5 Table 8-1
     * @param value
     * @return
     */
    public static boolean isDefaultAttributeValue(Object value)
    {
        if (value == null)
        {
            return true;
        }
        else if (value instanceof Boolean)
        {
            return ((Boolean)value).booleanValue() == false;
        }
        else if (value instanceof Number)
        {
            if (value instanceof Integer)
            {
                return ((Number)value).intValue() == Integer.MIN_VALUE;
            }
            else if (value instanceof Double)
            {
                return ((Number)value).doubleValue() == Double.MIN_VALUE;
            }
            else if (value instanceof Long)
            {
                return ((Number)value).longValue() == Long.MIN_VALUE;
            }
            else if (value instanceof Byte)
            {
                return ((Number)value).byteValue() == Byte.MIN_VALUE;
            }
            else if (value instanceof Float)
            {
                return ((Number)value).floatValue() == Float.MIN_VALUE;
            }
            else if (value instanceof Short)
            {
                return ((Number)value).shortValue() == Short.MIN_VALUE;
            }
        }
        return false;
    }
    
    /**
     * Find the proper Converter for the given UIOutput component.
     * @return the Converter or null if no Converter specified or needed
     * @throws FacesException if the Converter could not be created
     */
    public static Converter findUIOutputConverter(FacesContext facesContext,
                                                  UIOutput component)
            throws FacesException
    {
        return _SharedRendererUtils.findUIOutputConverter(facesContext, component);
    }


    /**
     * Find proper Converter for the entries in the associated List or Array of
     * the given UISelectMany as specified in API Doc of UISelectMany.
     * @return the Converter or null if no Converter specified or needed
     * @throws FacesException if the Converter could not be created
     */
    public static Converter findUISelectManyConverter(FacesContext facesContext,
                                                      UISelectMany component)
    {
        Converter converter = component.getConverter();
        if (converter != null) return converter;

        //Try to find out by value binding
        ValueBinding vb = component.getValueBinding("value");
        if (vb == null) return null;

        Class valueType = vb.getType(facesContext);
        if (valueType == null) return null;

        if (List.class.isAssignableFrom(valueType))
        {
            //According to API Doc of UISelectMany the assumed entry type for a List is String
            //--> no converter needed
            return null;
        }

        if (!valueType.isArray())
        {
            throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
        }

        Class arrayComponentType = valueType.getComponentType();
        if (String.class.equals(arrayComponentType)) return null;    //No converter needed for String type
        if (Object.class.equals(arrayComponentType)) return null;    //There is no converter for Object class

        try
        {
            return facesContext.getApplication().createConverter(arrayComponentType);
        }
        catch (FacesException e)
        {
            log.error("No Converter for type " + arrayComponentType.getName() + " found", e);
            return null;
        }
    }


    public static void checkParamValidity(FacesContext facesContext, UIComponent uiComponent, Class compClass)
    {
        if(facesContext == null)
            throw new NullPointerException("facesContext may not be null");
        if(uiComponent == null)
            throw new NullPointerException("uiComponent may not be null");

        //if (compClass != null && !(compClass.isAssignableFrom(uiComponent.getClass())))
        // why isAssignableFrom with additional getClass method call if isInstance does the same?
        if (compClass != null && !(compClass.isInstance(uiComponent)))
        {
            throw new IllegalArgumentException("uiComponent is instanceof "
                + uiComponent.getClass().getName()+" and not of "+compClass.getName()+" as it should be");
        }
    }


    public static void renderChildren(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        if (component.getChildCount() > 0)
        {
            for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                renderChild(facesContext, child);
            }
        }
    }


    public static void renderChild(FacesContext facesContext, UIComponent child)
            throws IOException
    {
        if (!child.isRendered())
        {
            return;
        }
        
        child.encodeBegin(facesContext);
        if (child.getRendersChildren())
        {
            child.encodeChildren(facesContext);
        }
        else
        {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }



    /**
     * @param uiSelectOne
     * @return List of SelectItem Objects
     */
    public static List getSelectItemList(UISelectOne uiSelectOne)
    {
        return internalGetSelectItemList(uiSelectOne);
    }

    /**
     * @param uiSelectMany
     * @return List of SelectItem Objects
     */
    public static List getSelectItemList(UISelectMany uiSelectMany)
    {
        return internalGetSelectItemList(uiSelectMany);
    }

    private static List internalGetSelectItemList(UIComponent uiComponent)
    {
        /* TODO: Shall we cache the list in a component attribute?
        ArrayList list = (ArrayList)uiComponent.getAttributes().get(SELECT_ITEM_LIST_ATTR);
        if (list != null)
        {
            return list;
        }
         */

        List list = new ArrayList(uiComponent.getChildCount());
        for (Iterator children = uiComponent.getChildren().iterator(); children.hasNext(); )
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UISelectItem)
            {
                Object value = ((UISelectItem)child).getValue();
                if (value != null)
                {
                    //get SelectItem from model via value binding
                    if (!(value instanceof SelectItem))
                    {
                        FacesContext facesContext = FacesContext.getCurrentInstance();
                        throw new IllegalArgumentException("Value binding of UISelectItem with id " + child.getClientId(facesContext) + " does not reference an Object of type SelectItem");
                    }
                    list.add(value);
                }
                else
                {
                    Object itemValue = ((UISelectItem)child).getItemValue();
                    String label = ((UISelectItem)child).getItemLabel();
                    String description = ((UISelectItem)child).getItemDescription();
                    boolean disabled = ((UISelectItem)child).isItemDisabled();
                    if (label == null)
                    {
                        list.add(new SelectItem(itemValue, itemValue.toString(), description, disabled));
                    }
                    else
                    {
                        list.add(new SelectItem(itemValue, label, description, disabled));
                    }
                }
            }
            else if (child instanceof UISelectItems)
            {
                Object value = ((UISelectItems)child).getValue();
                if (value instanceof SelectItem)
                {
                    list.add(value);
                }
                else if (value instanceof SelectItem[])
                {
                    for (int i = 0; i < ((SelectItem[])value).length; i++)
                    {
                        list.add(((SelectItem[])value)[i]);
                    }
                }
                else if (value instanceof Collection)
                {
                    for (Iterator it = ((Collection)value).iterator(); it.hasNext();)
                    {
                        Object item = it.next();
                        if (!(item instanceof SelectItem))
                        {
                            FacesContext facesContext = FacesContext.getCurrentInstance();
                            throw new IllegalArgumentException("Collection referenced by UISelectItems with id " + child.getClientId(facesContext) + " does not contain Objects of type SelectItem");
                        }
                        list.add(item);
                    }
                }
                else if (value instanceof Map)
                {
                    for (Iterator it = ((Map)value).entrySet().iterator(); it.hasNext();)
                    {
                        Map.Entry entry = (Map.Entry)it.next();
                        list.add(new SelectItem(entry.getValue(), entry.getKey().toString()));
                    }
                }
                else
                {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    throw new IllegalArgumentException("Value binding of UISelectItems with id " + child.getClientId(facesContext) + " does not reference an Object of type SelectItem, SelectItem[], Collection or Map");
                }
            }
            else
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                log.error("Invalid child with id " + child.getClientId(facesContext) + "of component with id : "+uiComponent.getClientId(facesContext)
                        +" : must be UISelectItem or UISelectItems, is of type : "+((child==null)?"null":child.getClass().getName()));
            }
        }

        return list;
    }


    /**
     * Convenient utility method that returns the currently submitted values of
     * a UISelectMany component as a Set, of which the contains method can then be
     * easily used to determine if a select item is currently selected.
     * Calling the contains method of this Set with the renderable (String converted) item value
     * as argument returns true if this item is selected.
     * @param uiSelectMany
     * @return Set containing all currently selected values
     */
    public static Set getSubmittedValuesAsSet(UISelectMany uiSelectMany)
    {
        Object submittedValues = uiSelectMany.getSubmittedValue();
        if (submittedValues == null)
        {
            return null;
        }
        else
        {
            try
            {
                return internalSubmittedOrSelectedValuesAsSet(uiSelectMany, submittedValues);
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalArgumentException("Submitted value of UISelectMany component with id " + uiSelectMany.getClientId(FacesContext.getCurrentInstance()) + " is not of type Array or List");
            }
        }
    }


    /**
     * Convenient utility method that returns the currently selected values of
     * a UISelectMany component as a Set, of which the contains method can then be
     * easily used to determine if a value is currently selected.
     * Calling the contains method of this Set with the item value
     * as argument returns true if this item is selected.
     * @param uiSelectMany
     * @return Set containing all currently selected values
     */
    public static Set getSelectedValuesAsSet(UISelectMany uiSelectMany)
    {
        Object selectedValues = uiSelectMany.getValue();
        if (selectedValues == null)
        {
            return Collections.EMPTY_SET;
        }
        else
        {
            try
            {
                return internalSubmittedOrSelectedValuesAsSet(uiSelectMany, selectedValues);
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalArgumentException("Value of UISelectMany component with id " + uiSelectMany.getClientId(FacesContext.getCurrentInstance()) + " is not of type Array or List");
            }
        }
    }

    private static Set internalSubmittedOrSelectedValuesAsSet(UISelectMany uiSelectMany,
                                                              Object values)
    {
        if (values == null)
        {
            return Collections.EMPTY_SET;
        }
        else if (values instanceof Object[])
        {
            //Object array
            Object[] ar = (Object[])values;
            if (ar.length == 0)
            {
                return Collections.EMPTY_SET;
            }
            else
            {
                HashSet set = new HashSet(HashMapUtils.calcCapacity(ar.length));
                for (int i = 0; i < ar.length; i++)
                {
                    set.add(ar[i]);
                }
                return set;
            }
        }
        else if (values.getClass().isArray())
        {
            //primitive array
            int len = Array.getLength(values);
            HashSet set = new HashSet(HashMapUtils.calcCapacity(len));
            for (int i = 0; i < len; i++)
            {
                set.add(Array.get(values, i));
            }
            return set;
        }
        else if (values instanceof List)
        {
            List lst = (List)values;
            if (lst.size() == 0)
            {
                return Collections.EMPTY_SET;
            }
            else
            {
                HashSet set = new HashSet(HashMapUtils.calcCapacity(lst.size()));
                set.addAll(lst);
                return set;
            }
        }
        else
        {
            throw new IllegalArgumentException("Value of UISelectMany component with id " + uiSelectMany.getClientId(FacesContext.getCurrentInstance()) + " is not of type Array or List");
        }
    }



    public static Object getConvertedUIOutputValue(FacesContext facesContext,
                                                   UIOutput output,
                                                   Object submittedValue)
        throws ConverterException
    {
        if (!(submittedValue instanceof String))
        {
            throw new IllegalArgumentException("Submitted value of type String expected");
        }

        Converter converter;
        try
        {
            converter = findUIOutputConverter(facesContext, output);
        }
        catch (FacesException e)
        {
            throw new ConverterException(e);
        }

        if (converter == null)
        {
            //No conversion needed
            return submittedValue;
        }
        else
        {
            //Conversion
            return converter.getAsObject(facesContext, output, (String)submittedValue);
        }
    }


    public static Object getConvertedUISelectManyValue(FacesContext facesContext,
                                                       UISelectMany selectMany,
                                                       Object submittedValue)
            throws ConverterException
    {
        if (!(submittedValue instanceof String[]))
        {
            throw new ConverterException("Submitted value of type String[] expected");
        }
        return _SharedRendererUtils.getConvertedUISelectManyValue(facesContext,
                                                                  selectMany,
                                                                  (String[])submittedValue);
    }


    public static boolean getBooleanAttribute(UIComponent component,
                                              String attrName,
                                              boolean defaultValue)
    {
        Boolean b = (Boolean)component.getAttributes().get(attrName);
        return b != null ? b.booleanValue() : defaultValue;
    }
    
    public static int getIntegerAttribute(UIComponent component,
                                          String attrName,
                                          int defaultValue)
    {
        Integer i = (Integer)component.getAttributes().get(attrName);
        return i != null ? i.intValue() : defaultValue;
    }

    public static UIForm findParentForm(UIComponentBase comp)
    {
        UIComponent parent = comp.getParent();
        while (parent != null)
        {
            if (parent instanceof UIForm)
            {
                return (UIForm)parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

}
