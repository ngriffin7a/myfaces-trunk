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

import net.sourceforge.myfaces.component.ComponentUtils;
import net.sourceforge.myfaces.component.UserRoleSupport;
import net.sourceforge.myfaces.util.HashMapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
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

        if (component instanceof EditableValueHolder &&
            !((EditableValueHolder)component).isValid())
        {
            Object submittedValue = ((EditableValueHolder)component).getSubmittedValue();
            if (!(submittedValue instanceof String))
            {
                throw new IllegalArgumentException("Expected submitted value of type String");
            }
            return (String)submittedValue; 
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
    
    public static boolean isEnabledOnUserRole(FacesContext facesContext, UIComponent component)
    {
        String userRole;
        if (component instanceof UserRoleSupport)
        {
            userRole = ((UserRoleSupport)component).getEnabledOnUserRole();
        }
        else
        {
            userRole = (String)component.getAttributes().get(JSFAttr.ENABLED_ON_USER_ROLE_ATTR);
        }

        if (userRole == null)
        {
            //no restriction
            return true;
        }
        else
        {
            return facesContext.getExternalContext().isUserInRole(userRole);
        }
    }

    public static boolean isVisibleOnUserRole(FacesContext facesContext, UIComponent component)
    {
        String userRole;
        if (component instanceof UserRoleSupport)
        {
            userRole = ((UserRoleSupport)component).getVisibleOnUserRole();
        }
        else
        {
            userRole = (String)component.getAttributes().get(JSFAttr.VISIBLE_ON_USER_ROLE_ATTR);
        }

        if (userRole == null)
        {
            //no restriction
            return true;
        }
        else
        {
            return facesContext.getExternalContext().isUserInRole(userRole);
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
        Converter converter = component.getConverter();
        if (converter != null)
        {
            return converter;
        }
        else
        {
            //Try to find out by value binding
            ValueBinding vb = component.getValueBinding("value");
            if (vb != null)
            {
                Class valueType = vb.getType(facesContext);
                if (String.class.equals(valueType))
                {
                    return null;    //No converter needed for String type
                }

                try
                {
                    return facesContext.getApplication().createConverter(valueType);
                }
                catch (FacesException e)
                {
                    log.error("No Converter for type " + valueType.getName() + " found");
                    throw e;
                }
            }
            else
            {
                //no ValueBinding, assume String type
                return null;    //No converter needed for String type
            }
        }
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
        if (converter != null)
        {
            return converter;
        }

        //Try to find out by value binding
        ValueBinding vb = component.getValueBinding("value");
        if (vb != null)
        {
            Class valueType = vb.getType(facesContext);
            if (List.class.isAssignableFrom(valueType))
            {
                //According to API Doc of UISelectMany the assumed entry type is String
                return null;    //No converter needed for String type
            }
            else if (valueType.isArray())
            {
                Class arrayComponentType = valueType.getComponentType();
                if (String.class.equals(arrayComponentType))
                {
                    return null;    //No converter needed for String type
                }

                try
                {
                    return facesContext.getApplication().createConverter(arrayComponentType);
                }
                catch (FacesException e)
                {
                    log.error("No Converter for type " + valueType.getName() + " found");
                    throw e;
                }
            }
            else
            {
                throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
            }
        }
        else
        {
            //no ValueBinding, assume String type
            return null;    //No converter needed for String type
        }
    }


    public static void checkParamValidity(FacesContext facesContext, UIComponent uiComponent, Class compClass)
    {
        if(facesContext == null)
            throw new NullPointerException("facesContext may not be null");
        if(uiComponent == null)
            throw new NullPointerException("uiComponent may not be null");

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
        ArrayList list = (ArrayList)uiComponent.getAttributes().get(SELECT_ITEM_LIST_ATTR);
        if (list != null)
        {
            return list;
        }

        list = new ArrayList(uiComponent.getChildCount());
        for(Iterator children = uiComponent.getChildren().iterator(); children.hasNext(); )
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UISelectItem)
            {
                list.add(ComponentUtils.getSelectItemFromUISelectItem((UISelectItem)child));
            }
            else if (child instanceof UISelectItems)
            {
                ComponentUtils.addSelectItemsToCollection((UISelectItems)child, list);
            }
        }
        return list;
    }


    /**
     * Convenient utility method that returns the currently selected values of
     * a UISelectMany component as a Set, of which the contains method can then be
     * easily used to determine if a value is currently selected.
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

        if (selectedValues.getClass().isArray())
        {
            Object[] ar = (Object[])selectedValues;
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
        else if (selectedValues instanceof List)
        {
            List lst = (List)selectedValues;
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

        Converter converter;
        try
        {
            converter = findUISelectManyConverter(facesContext, selectMany);
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
            //convert each item value
            String[] submittedStrValues = (String[])submittedValue;
            Object[] convertedValues;
            convertedValues = new Object[submittedStrValues.length];
            for (int i = 0; i < submittedStrValues.length; i++)
            {
                convertedValues[i]
                    = converter.getAsObject(facesContext, selectMany, submittedStrValues[i]);
            }
            return convertedValues;
        }
    }

    
    public static boolean getBooleanAttribute(UIComponent component,
                                              String attrName,
                                              boolean defaultValue)
    {
        Boolean b = (Boolean)component.getAttributes().get(attrName);
        return b != null ? b.booleanValue() : defaultValue;
    }
    
}
