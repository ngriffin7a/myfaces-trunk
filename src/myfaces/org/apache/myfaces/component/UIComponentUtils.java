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
import javax.faces.component.NamingContainer;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIComponentUtils
    implements CommonComponentProperties
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


    public static void setComponentValue(javax.faces.component.UIOutput uiOutput,
                                         Object newValue)
    {
        uiOutput.setValue(newValue);
        uiOutput.setAttribute(CommonComponentProperties.STRING_VALUE_ATTR, null);
        uiOutput.setValid(true);
    }


    public static void convertAndSetValue(FacesContext facesContext,
                                          javax.faces.component.UIOutput uiOutput,
                                          String newValue,
                                          boolean addErrorMessageOnFail)
    {
        Converter conv = ConverterUtils.findValueConverter(facesContext, uiOutput);
        if (conv == null)
        {
            //default to StringConverter
            conv = ConverterUtils.getConverter(String.class);
        }

        convertAndSetValue(facesContext, uiOutput, newValue, conv, addErrorMessageOnFail);
    }

    public static void convertAndSetValue(FacesContext facesContext,
                                          javax.faces.component.UIOutput uiOutput,
                                          String[] newValues,
                                          boolean addErrorMessageOnFail)
    {
        Converter conv = ConverterUtils.findValueConverter(facesContext, uiOutput);
        if (conv == null)
        {
            //default to StringConverter
            conv = ConverterUtils.getConverter(String.class);
        }

        if (conv instanceof StringArrayConverter)
        {
            setComponentValue(uiOutput, newValues);
        }
        else
        {
            String s = StringArrayConverter.getAsString(newValues, false);
            convertAndSetValue(facesContext, uiOutput, s, conv, addErrorMessageOnFail);
        }
    }



    public static void convertAndSetValue(FacesContext facesContext,
                                          javax.faces.component.UIOutput uiOutput,
                                          String newValue,
                                          Converter converter,
                                          boolean addErrorMessageOnFail)
        throws FacesException
    {
        try
        {
            Object objValue = converter.getAsObject(facesContext, uiOutput, newValue);
            setComponentValue(uiOutput, objValue);
        }
        catch (ConverterException e)
        {
            uiOutput.setValue(null);
            uiOutput.setAttribute(CommonComponentProperties.STRING_VALUE_ATTR, newValue);
            uiOutput.setValid(false);
            if (addErrorMessageOnFail)
            {
                addConversionErrorMessage(facesContext, uiOutput, e);
            }
            else
            {
                throw new FacesException("Error converting value of component " + toString(uiOutput) + " from String to Object: Converter Exception.", e);
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
        if (uiComponent instanceof MyFacesUICommand)
        {
            return ((MyFacesUICommand)uiComponent).getListeners();
        }
        else if (uiComponent instanceof MyFacesUIInput)
        {
            return ((MyFacesUIInput)uiComponent).getListeners();
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
                attrName.startsWith("net.sf.myfaces.") ||
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

    public static String getClientId(FacesContext facesContext,
                                     UIComponent uiComponent)
    {
        String clientId = (String)uiComponent.getAttribute(CommonComponentProperties.CLIENT_ID_ATTR);
        if (clientId != null)
        {
            return clientId;
        }

        //Find namingContainer
        UIComponent find = UIComponentUtils.getParentOrFacetOwner(uiComponent);
        if (find == null)
        {
            //we have got the root component
            if (!(uiComponent instanceof NamingContainer))
            {
                throw new FacesException("Root is no naming container?!");
            }

            if (uiComponent.getComponentId() == null)
            {
                uiComponent.setComponentId(UIRoot.ROOT_COMPONENT_ID);
            }
            clientId = uiComponent.getComponentId();
        }
        else
        {
            while (!(find instanceof NamingContainer))
            {
                find = UIComponentUtils.getParentOrFacetOwner(find);
                if (find == null)
                {
                    throw new FacesException("Root is no naming container?!");
                }
            }
            NamingContainer namingContainer = (NamingContainer)find;

            if (uiComponent.getComponentId() == null)
            {
                uiComponent.setComponentId(namingContainer.generateClientId());
            }

            if (UIComponentUtils.getParentOrFacetOwner(find) == null)
            {
                //NamingContainer is root, so nothing to be prepended
                clientId = uiComponent.getComponentId();
            }
            else
            {
                clientId = find.getClientId(facesContext) + UIComponent.SEPARATOR_CHAR + uiComponent.getComponentId();
            }
        }

        uiComponent.setAttribute(CommonComponentProperties.CLIENT_ID_ATTR, clientId);
        return clientId;
    }


    public static final NamingContainer findNamingContainer(UIComponent uiComponent)
    {
        //Find namingContainer
        UIComponent find = getParentOrFacetOwner(uiComponent);
        if (find == null)
        {
            //we have got the root component
            throw new IllegalArgumentException("Cannot find naming container for root!");
        }
        while (!(find instanceof NamingContainer))
        {
            find = UIComponentUtils.getParentOrFacetOwner(find);
            if (find == null)
            {
                throw new FacesException("Root is no naming container?!");
            }
        }
        return (NamingContainer)find;
    }


    /**
     * DOCUMENT ME!
     * @param facesContext
     * @param uiComponent
     * @param attrName
     * @return
     */
    public static Converter findConverterForAttribute(FacesContext facesContext,
                                                      UIComponent uiComponent,
                                                      String attrName)
    {
        if (uiComponent instanceof javax.faces.component.UIOutput &&
            attrName.equals(MyFacesUIOutput.VALUE_PROP))
        {
            return ConverterUtils.findValueConverter(facesContext,
                                                     (javax.faces.component.UIOutput)uiComponent);
        }
        else if (attrName.equals(CommonComponentProperties.STRING_VALUE_ATTR))
        {
            return ConverterUtils.getConverter(String.class);
        }
        else
        {
            return ConverterUtils.findPropertyOrAttributeConverter(facesContext,
                                                         uiComponent,
                                                         attrName);
        }
    }



    private static final String FACETS_AND_CHILDREN_COLLECTION_ATTR
        = UIComponentUtils.class.getName() + ".FACETS_AND_CHILDREN_COLLECTION";

    public static int getFacetAndChildCount(UIComponent uiComponent)
    {
        return getFacetsAndChildrenCollection(uiComponent).size();
    }

    public static UIComponent getFacetOrChild(UIComponent uiComponent, int index)
    {
        return (UIComponent)getFacetsAndChildrenCollection(uiComponent).get(index);
    }

    private static List getFacetsAndChildrenCollection(UIComponent uiComponent)
    {
        List lst = (List)uiComponent.getAttribute(FACETS_AND_CHILDREN_COLLECTION_ATTR);
        if (lst == null)
        {
            Iterator it = uiComponent.getFacetsAndChildren();
            lst = new ArrayList();
            while (it.hasNext())
            {
                lst.add(it.next());
            }
            uiComponent.setAttribute(FACETS_AND_CHILDREN_COLLECTION_ATTR, lst);
        }
        return lst;
    }




    /**
     * Returns this component's parent. If this component is not a real
     * child but a facet, the "facet owner" is returned. This is done by
     * returning the "javax.faces.component.FacetParent" attribute, which is
     * set by the addFacet method in UIComponentBase.
     */
    public static UIComponent getParentOrFacetOwner(UIComponent uiComponent)
    {
        /*
        UIComponent parent = uiComponent.getParent();
        return parent != null
                ? parent
                : (UIComponent)uiComponent.getAttribute(FACET_PARENT_ATTR);
                */
        return uiComponent.getParent();
    }



    /**
     * HACK: {@link javax.faces.component.UIComponentBase#removeChild} does not
     * remove the child from the naming container. This hack properly removes a
     * child from it's parent and from the naming container.
     * @param parent
     * @param child
     */
    public static void removeChild(UIComponent parent,
                                   UIComponent child)
    {
        parent.removeChild(child);

        NamingContainer namingContainer;
        if (parent instanceof NamingContainer)
        {
            namingContainer = (NamingContainer)parent;
        }
        else
        {
            namingContainer = UIComponentUtils.findNamingContainer(parent);
        }

        recursiveRemoveFromNamingContainer(namingContainer, child);
    }

    /**
     * HACK: {@link javax.faces.component.UIComponentBase#removeChild} does not
     * remove the child from the naming container. This hack properly removes a
     * child from it's parent and from the naming container.
     * @param parent
     * @param i
     */
    public static void removeChild(UIComponent parent, int i)
    {
        UIComponent child = parent.getChild(i);
        parent.removeChild(i);

        NamingContainer namingContainer;
        if (parent instanceof NamingContainer)
        {
            namingContainer = (NamingContainer)parent;
        }
        else
        {
            namingContainer = UIComponentUtils.findNamingContainer(parent);
        }

        recursiveRemoveFromNamingContainer(namingContainer, child);
    }


    /**
     * HACK: {@link javax.faces.component.UIComponentBase#removeFacet} does not
     * remove the child from the naming container. This hack properly removes a
     * child from it's parent and from the naming container.
     * @param parent
     * @param facetName
     */
    public static void removeFacet(UIComponent parent, String facetName)
    {
        UIComponent facet = parent.getFacet(facetName);
        parent.removeFacet(facetName);

        NamingContainer namingContainer;
        if (parent instanceof NamingContainer)
        {
            namingContainer = (NamingContainer)parent;
        }
        else
        {
            namingContainer = UIComponentUtils.findNamingContainer(parent);
        }

        recursiveRemoveFromNamingContainer(namingContainer, facet);
    }



    private static void recursiveRemoveFromNamingContainer(NamingContainer namingContainer,
                                                           UIComponent comp)
    {
        namingContainer.removeComponentFromNamespace(comp);

        if (comp instanceof NamingContainer)
        {
            //Component is itself a naming container, all children are
            //registered in this component and not in parent container.
            return;
        }

        for (Iterator it = comp.getFacetsAndChildren(); it.hasNext(); )
        {
            recursiveRemoveFromNamingContainer(namingContainer,
                                               (UIComponent)it.next());
        }
    }

}
