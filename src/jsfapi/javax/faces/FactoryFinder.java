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
package javax.faces;

import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * DOCUMENT ME!
 *
 * TODO:
 * - The "META-INF/services/" thing: is it done by configuration reader?
 * - Who calls the setFactory method, and is it guaranteed that this is done in the right order?
 * - Does spec explicitly say, that reading faces-config is not the job of the FactoryFinder?
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FactoryFinder
{
    public static final String APPLICATION_FACTORY = "javax.faces.application.ApplicationFactory";
    public static final String FACES_CONTEXT_FACTORY = "javax.faces.context.FacesContextFactory";
    public static final String LIFECYCLE_FACTORY = "javax.faces.lifecycle.LifecycleFactory";
    public static final String RENDER_KIT_FACTORY = "javax.faces.render.RenderKitFactory";

    private static final int FACTORY_COUNT = 4;
    private static final Map _classLoaderFactoriesMap = new HashMap();
    private static final Class[][] FACTORY_CONSTRUCTOR_PARAMS = {new Class[]{ApplicationFactory.class},
                                                                 new Class[]{FacesContextFactory.class},
                                                                 new Class[]{LifecycleFactory.class},
                                                                 new Class[]{RenderKitFactory.class}};
    private static final Class[] FACTORY_CONSTRUCTOR_EMPTY_PARAMS = new Class[0];

    public static Object getFactory(String factoryName)
            throws FacesException
    {
        int factoryIdx = checkFactoryNameAndGetIndex(factoryName);
        ClassLoader classLoader = getClassLoader();
        Stack[] factoryStacks = (Stack[])_classLoaderFactoriesMap.get(classLoader);
        if (factoryStacks == null)
        {
            throw new IllegalStateException("no factories configured for this application");
        }
        Stack factoryStack = factoryStacks[factoryIdx];
        if (factoryStack == null || factoryStack.isEmpty())
        {
            throw new IllegalStateException("no factory " + factoryName + " configured for this appliction");
        }
        Object topFactory = factoryStack.peek();
        if (!(topFactory instanceof String))
        {
            return topFactory;
        }

        //new instance
        Class factoryClass = null;
        try
        {
            factoryClass = classLoader.loadClass((String)topFactory);
        }
        catch (ClassNotFoundException e)
        {
            throw new FacesException(e);
        }

        int stackSize = factoryStack.size();
        if (stackSize > 1)
        {
            //Try 1 arguments constructor
            try
            {
                Constructor constructor = factoryClass.getConstructor(FACTORY_CONSTRUCTOR_PARAMS[factoryIdx]);
                Object previousFactory = factoryStack.get(stackSize - 2);
                try
                {
                    Object factory = constructor.newInstance(new Object[] {previousFactory});
                    factoryStack.set(stackSize - 1, factory);
                    return factory;
                }
                catch (Exception ex)
                {
                    throw new FacesException(ex);
                }
            }
            catch (NoSuchMethodException e)
            {
            }
        }

        try
        {
            Constructor constructor = factoryClass.getConstructor(FACTORY_CONSTRUCTOR_EMPTY_PARAMS);
            try
            {
                Object factory = constructor.newInstance(null);
                factoryStack.set(stackSize - 1, factory);
                return factory;
            }
            catch (Exception ex)
            {
                throw new FacesException(ex);
            }
        }
        catch (NoSuchMethodException e)
        {
            throw new FacesException(e);
        }
    }

    public static void setFactory(String factoryName,
                                  String implName)
    {
        int factoryIdx = checkFactoryNameAndGetIndex(factoryName);
        ClassLoader classLoader = getClassLoader();
        synchronized(_classLoaderFactoriesMap)
        {
            Stack[] factoryStacks = (Stack[])_classLoaderFactoriesMap.get(classLoader);
            if (factoryStacks == null)
            {
                factoryStacks = new Stack[FACTORY_COUNT];
                _classLoaderFactoriesMap.put(classLoader, factoryStacks);
            }

            Stack factoryStack = factoryStacks[factoryIdx];
            if (factoryStack == null)
            {
                factoryStack = new Stack();
                factoryStacks[factoryIdx] = factoryStack;
            }
            factoryStack.push(implName);
        }
    }


    public static void releaseFactories()
            throws FacesException
    {
        ClassLoader classLoader = getClassLoader();
        Stack[] factoryStacks = (Stack[])_classLoaderFactoriesMap.get(classLoader);
        for (int i = 0; i < factoryStacks.length; i++)
        {
            Stack factoryStack = factoryStacks[i];
            factoryStack.clear();
        }
    }


    private static int checkFactoryNameAndGetIndex(String factoryName)
    {
        if (factoryName == null)
        {
            throw new NullPointerException("factoryName");
        }
        else if (factoryName.equals(APPLICATION_FACTORY))
        {
            return 0;
        }
        else if (factoryName.equals(FACES_CONTEXT_FACTORY))
        {
            return 1;
        }
        else if (factoryName.equals(LIFECYCLE_FACTORY))
        {
            return 2;
        }
        else if (factoryName.equals(RENDER_KIT_FACTORY))
        {
            return 3;
        }
        throw new IllegalArgumentException("factoryName " + factoryName);
    }


    private static ClassLoader getClassLoader()
    {
        try
        {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null)
            {
                throw new FacesException("web application class loader cannot be identified", null);
            }
            return classLoader;
        }
        catch (Exception e)
        {
            throw new FacesException("web application class loader cannot be identified", e);
        }
    }
}
