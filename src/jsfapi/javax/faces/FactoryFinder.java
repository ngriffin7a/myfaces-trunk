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
import java.util.*;

/**
 * TODO: The "META-INF/services/" thing
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.12.2.1  2004/06/16 01:25:50  o_rossmueller
 * refactorings: FactoryFinder, decorator creation, dispenser (removed reverse order)
 * bug fixes
 * additional tests
 *
 * Revision 1.12  2004/04/29 07:24:51  manolito
 * fixed NPE in releaseFactories
 *
 * Revision 1.11  2004/04/13 08:25:59  manolito
 * Log
 *
 */
public class FactoryFinder
{
    public static final String APPLICATION_FACTORY = "javax.faces.application.ApplicationFactory";
    public static final String FACES_CONTEXT_FACTORY = "javax.faces.context.FacesContextFactory";
    public static final String LIFECYCLE_FACTORY = "javax.faces.lifecycle.LifecycleFactory";
    public static final String RENDER_KIT_FACTORY = "javax.faces.render.RenderKitFactory";

    private static Set validFactoryNames = new HashSet();

    private static final Map _registeredFactoryNames = new HashMap();
    private static final Map _factories = new HashMap();

    /**
     * The returned set is immutable
     * @return
     */
    public static Set getValidFactoryNames() {
      if(validFactoryNames.size() == 0) {
        validFactoryNames.add(FactoryFinder.APPLICATION_FACTORY);
        validFactoryNames.add(FactoryFinder.FACES_CONTEXT_FACTORY);
        validFactoryNames.add(FactoryFinder.LIFECYCLE_FACTORY);
        validFactoryNames.add(FactoryFinder.RENDER_KIT_FACTORY);

          validFactoryNames = Collections.unmodifiableSet(validFactoryNames);
      }
      return validFactoryNames;
    }
    
    public static Object getFactory(String factoryName)
            throws FacesException
    {
        ClassLoader classLoader = getClassLoader();
        Map factoryClassNames = (Map) _registeredFactoryNames.get(classLoader);

        if (factoryClassNames == null)
        {
            String message = "No Factories configured for this Application - typically this is because " +
            "a context listener is not setup in your web.xml.\n" + 
            "A typical config looks like this;\n<listener>\n" +
            "  <listener-class>net.sourceforge.myfaces.webapp.StartupServletContextListener</listener-class>\n" +
            "</listener>\n";
            throw new IllegalStateException(message);
        }

        if (! factoryClassNames.containsKey(factoryName)) {
            throw new IllegalStateException("no factory " + factoryName + " configured for this appliction");
        }

        Map factoryMap = (Map) _factories.get(classLoader);

        if (factoryMap == null) {
            factoryMap = new HashMap();
            _factories.put(classLoader, factoryMap);
        }
        Object factory = factoryMap.get(factoryName);

        if (factory == null) {
            factory = newFactoryInstance((String)factoryClassNames.get(factoryName), classLoader);
            factoryMap.put(factoryName, factory);
            return factory;
        }
        else
        {
            return factory;
        }
    }

    private static Object newFactoryInstance(String factoryImplName,
                                             ClassLoader classLoader)
    {
        Class factoryClass = null;
        try
        {
            factoryClass = classLoader.loadClass(factoryImplName);
            return factoryClass.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new FacesException(e);
        } catch (IllegalAccessException e)
        {
            throw new FacesException(e);
        } catch (InstantiationException e)
        {
            throw new FacesException(e);
        }
    }

    public static void setFactory(String factoryName,
                                  String implName)
    {
        checkFactoryName(factoryName);

        ClassLoader classLoader = getClassLoader();
        synchronized(_registeredFactoryNames)
        {
            Map factories = (Map) _factories.get(classLoader);

            if (factories != null && factories.containsKey(factoryName)) {
                // Javadoc says ... This method has no effect if getFactory() has already been
                // called looking for a factory for this factoryName.
                return;
            }

            Map factoryClassNames = (Map) _registeredFactoryNames.get(classLoader);

            if (factoryClassNames == null)
            {
                factoryClassNames = new HashMap();
                _registeredFactoryNames.put(classLoader, factoryClassNames);
            }

            factoryClassNames.put(factoryName, implName);
        }
    }


    public static void releaseFactories()
            throws FacesException
    {
        ClassLoader classLoader = getClassLoader();
        _factories.remove(classLoader);
    }


    private static void checkFactoryName(String factoryName)
    {
        if (! validFactoryNames.contains(factoryName)) {
            throw new IllegalArgumentException("factoryName '" + factoryName + "'");
        }
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
