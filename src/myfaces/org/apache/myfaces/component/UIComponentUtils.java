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
package net.sourceforge.myfaces.component;

import net.sourceforge.myfaces.convert.ConversionErrorMessage;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.convert.MyFacesConverterException;
import net.sourceforge.myfaces.convert.impl.StringArrayConverter;
import net.sourceforge.myfaces.renderkit.html.state.client.MinimizingStateSaver;
import net.sourceforge.myfaces.tree.TreeUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.tree.Tree;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIComponentUtils
{
    private UIComponentUtils() {}

    public static boolean getBooleanAttribute(UIComponent uiComponent,
                                              String attribute,
                                              boolean defaultValue)
    {
        Boolean b = (Boolean)uiComponent.getAttribute(attribute);
        if (b == null)
        {
            return defaultValue;
        }
        else
        {
            return b.booleanValue();
        }
    }

    public static void setBooleanAttribute(UIComponent uiComponent,
                                           String attribute,
                                           boolean value)
    {
        uiComponent.setAttribute(attribute, value ? Boolean.TRUE : Boolean.FALSE);
    }


    public static void setComponentValue(UIComponent uiComponent,
                                         Object newValue)
    {
        uiComponent.setValue(newValue);
        uiComponent.setAttribute(CommonComponentAttributes.STRING_VALUE_ATTR, null);
        uiComponent.setValid(true);
    }


    public static void convertAndSetValue(FacesContext facesContext,
                                          UIComponent uiComponent,
                                          String newValue,
                                          boolean addErrorMessageOnFail)
    {
        Converter conv = ConverterUtils.findValueConverter(facesContext, uiComponent);
        if (conv == null)
        {
            //default to StringConverter
            conv = ConverterUtils.getConverter(String.class);
        }

        convertAndSetValue(facesContext, uiComponent, newValue, conv, addErrorMessageOnFail);
    }

    public static void convertAndSetValue(FacesContext facesContext,
                                          UIComponent uiComponent,
                                          String[] newValues,
                                          boolean addErrorMessageOnFail)
    {
        Converter conv = ConverterUtils.findValueConverter(facesContext, uiComponent);
        if (conv == null)
        {
            //default to StringConverter
            conv = ConverterUtils.getConverter(String.class);
        }

        if (conv instanceof StringArrayConverter)
        {
            setComponentValue(uiComponent, newValues);
        }
        else
        {
            String s = StringArrayConverter.getAsString(newValues, false);
            convertAndSetValue(facesContext, uiComponent, s, conv, addErrorMessageOnFail);
        }
    }



    public static void convertAndSetValue(FacesContext facesContext,
                                          UIComponent uiComponent,
                                          String newValue,
                                          Converter converter,
                                          boolean addErrorMessageOnFail)
        throws FacesException
    {
        try
        {
            Object objValue = converter.getAsObject(facesContext, uiComponent, newValue);
            setComponentValue(uiComponent, objValue);
        }
        catch (ConverterException e)
        {
            uiComponent.setValue(null);
            uiComponent.setAttribute(CommonComponentAttributes.STRING_VALUE_ATTR, newValue);
            uiComponent.setValid(false);
            if (addErrorMessageOnFail)
            {
                addConversionErrorMessage(facesContext, uiComponent, e);
            }
            else
            {
                throw new FacesException("Error converting value of component " + toString(uiComponent) + " from String to Object: Converter Exception.", e);
            }
        }
    }


    protected static void addConversionErrorMessage(FacesContext facesContext,
                                                    UIComponent comp,
                                                    ConverterException e)
    {
        if (e instanceof MyFacesConverterException)
        {
            facesContext.addMessage(comp,
                                    ((MyFacesConverterException)e).getFacesMessage());
        }
        else
        {
            facesContext.addMessage(comp, new ConversionErrorMessage(e.getMessage()));
        }
    }


    public static void setTransient(UIComponent uiComponent, boolean b)
    {
        uiComponent.setAttribute(MinimizingStateSaver.TRANSIENT_ATTR,
                                 b ? Boolean.TRUE : Boolean.FALSE);
    }


    public static boolean isTransient(UIComponent uiComponent)
    {
        Boolean trans = (Boolean)uiComponent.getAttribute(MinimizingStateSaver.TRANSIENT_ATTR);
        if (trans == null)
        {
            //return MyFacesConfig.isComponentsTransientByDefault();
            return false; //Components never transient by default
        }
        else
        {
            return trans.booleanValue();
        }
    }



    public static List[] getListeners(UIComponent uiComponent)
    {
        if (uiComponent instanceof UICommand)
        {
            return ((UICommand)uiComponent).getListeners();
        }
        else if (uiComponent instanceof UIInput)
        {
            return ((UIInput)uiComponent).getListeners();
        }
        else if (uiComponent instanceof javax.faces.component.UICommand ||
                 uiComponent instanceof javax.faces.component.UIInput)
        {
            //HACK to get protected field "listeners" from the given UICommand or UIInput:
            //TODO: try to convince Sun of making listeners public or give us a method to access them
            try
            {
                Field f = null;
                Class c = uiComponent.getClass();
                while (f == null && c != null && !c.equals(Object.class))
                {
                    try
                    {
                        f = c.getDeclaredField("listeners");
                    }
                    catch (NoSuchFieldException e)
                    {
                    }
                    c = c.getSuperclass();
                }

                if (f == null)
                {
                    throw new RuntimeException(new NoSuchFieldException());
                }

                List[] theListeners;
                if (f.isAccessible())
                {
                    theListeners = (List[])f.get(uiComponent);
                }
                else
                {
                    final Field finalF = f;
                    AccessController.doPrivileged(
                        new PrivilegedAction()
                        {
                            public Object run()
                            {
                                finalF.setAccessible(true);
                                return null;
                            }
                        });
                    theListeners = (List[])f.get(uiComponent);
                    f.setAccessible(false);
                }

                return theListeners;
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            return null;
        }
    }



    public static String toString(UIComponent comp)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TreeUtils.printComponent(comp, new PrintStream(baos));
            baos.close();
            return baos.toString();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static boolean isInternalAttribute(String attrName)
    {
        return (attrName.startsWith("javax.") ||
                attrName.startsWith("net.sourceforge.myfaces."));
    }




    public static String getUniqueComponentId(FacesContext facesContext,
                                              UIComponent uiComponent)
    {
        return uiComponent.getClientId(facesContext);
    }

    public static UIComponent findComponentByUniqueId(FacesContext facesContext,
                                                      Tree tree,
                                                      String uniqueId)
    {
        return tree.getRoot().findComponent(uniqueId);
    }





}
