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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;

/**
 * TODO: The "META-INF/services/" thing
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

    private static Set factoryNames = new HashSet();
    
    private static final int FACTORY_COUNT = 4;
    private static final Map _classLoaderFactoriesMap = new HashMap();
    private static final Class[][] FACTORY_CONSTRUCTOR_PARAMS = {new Class[]{ApplicationFactory.class},
                                                                 new Class[]{FacesContextFactory.class},
                                                                 new Class[]{LifecycleFactory.class},
                                                                 new Class[]{RenderKitFactory.class}};
    private static final Class[] FACTORY_CONSTRUCTOR_EMPTY_PARAMS = new Class[0];

    /**
     * The returned set is immutable
     * @return
     */
    public static Set getFactoryNames() {
      if(factoryNames.size() == 0) {
        factoryNames.add(FactoryFinder.APPLICATION_FACTORY);
        factoryNames.add(FactoryFinder.FACES_CONTEXT_FACTORY);
        factoryNames.add(FactoryFinder.LIFECYCLE_FACTORY);
        factoryNames.add(FactoryFinder.RENDER_KIT_FACTORY);
      }
      return Collections.unmodifiableSet(factoryNames);
    }
    
    public static Object getFactory(String factoryName)
            throws FacesException
    {
        int factoryIdx = checkFactoryNameAndGetIndex(factoryName);
        ClassLoader classLoader = getClassLoader();
        Object[] factoryStacks = (Object[])_classLoaderFactoriesMap.get(classLoader);
        if (factoryStacks == null)
        {
            String message = "No Factories configured for this Application - typically this is because " +
            "a context listener is not setup in your web.xml.\n" + 
            "A typical config looks like this;\n<listener>\n" +
            "  <listener-class>net.sourceforge.myfaces.webapp.StartupServletContextListener</listener-class>\n" +
            "</listener>\n";
            throw new IllegalStateException(message);
        }
        Object factory = factoryStacks[factoryIdx];
        if (factory == null)
        {
            throw new IllegalStateException("no factory " + factoryName + " configured for this appliction");
        }
        else if (factory instanceof List)
        {
            //Not yet instantiated
            factory = initFactory((List)factory, classLoader, factoryIdx);
            factoryStacks[factoryIdx] = factory;
            return factory;
        }
        else
        {
            return factory;
        }
    }

    private static Object initFactory(List factoryNames,
                                      ClassLoader classLoader,
                                      int factoryIdx)
    {
        Object parentFactory = null;
        for (int i = 0, size = factoryNames.size(); i < size; i++)
        {
            String factoryImplName = (String) factoryNames.get(i);
            parentFactory = newFactoryInstance(factoryImplName, parentFactory, classLoader, factoryIdx);
        }
        return parentFactory;
    }

    private static Object newFactoryInstance(String factoryImplName,
                                             Object parentFactory,
                                             ClassLoader classLoader,
                                             int factoryIdx)
    {
        Class factoryClass = null;
        try
        {
            factoryClass = classLoader.loadClass(factoryImplName);
        }
        catch (ClassNotFoundException e)
        {
            throw new FacesException(e);
        }
        //Try 1 arguments constructor
        try
        {
            Constructor constructor = factoryClass.getConstructor(FACTORY_CONSTRUCTOR_PARAMS[factoryIdx]);
            try
            {
                return constructor.newInstance(new Object[] {parentFactory});
            }
            catch (Exception ex)
            {
                throw new FacesException(ex);
            }
        }
        catch (NoSuchMethodException e)
        {
            // ignore
        }

        try
        {
            Constructor constructor = factoryClass.getConstructor(FACTORY_CONSTRUCTOR_EMPTY_PARAMS);
            try
            {
                return constructor.newInstance(null);
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
            Object factory;
            Object[] factoryStacks = (Object[])_classLoaderFactoriesMap.get(classLoader);
            if (factoryStacks == null)
            {
                factoryStacks = new Object[FACTORY_COUNT];
                _classLoaderFactoriesMap.put(classLoader, factoryStacks);
                factory = null;
            }
            else
            {
                factory = factoryStacks[factoryIdx];
            }

            if (factory == null)
            {
                factory = new ArrayList();
                ((List)factory).add(implName);
                factoryStacks[factoryIdx] = (List)factory;
            }
            else if (factory instanceof List)
            {
                int size = ((List)factory).size();
                if (size > 0)
                {
                    String topFactory = (String) ((List)factory).get(size - 1);
                    if (implName.equals(topFactory))
                    {
                        //Already registered as top factory
                        return;
                    }
                }
                ((List)factory).add(implName);
            }
            else
            {
                // else do nothing:
                // Javadoc says ... This method has no effect if getFactory() has already been
                // called looking for a factory for this factoryName.
            }
        }
    }


    public static void releaseFactories()
            throws FacesException
    {
        ClassLoader classLoader = getClassLoader();
        Object[] factoryStacks = (Object[])_classLoaderFactoriesMap.get(classLoader);
        for (int i = 0; i < factoryStacks.length; i++)
        {
            factoryStacks[i] = null;
        }
        factoryStacks = null;
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
        throw new IllegalArgumentException("factoryName '" + factoryName + "'");
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
