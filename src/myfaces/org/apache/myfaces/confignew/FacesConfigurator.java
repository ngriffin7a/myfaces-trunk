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

package net.sourceforge.myfaces.confignew;

import net.sourceforge.myfaces.confignew.impl.dom.DOMFacesConfigDispenserImpl;
import net.sourceforge.myfaces.confignew.impl.dom.DOMFacesConfigUnmarshallerImpl;
import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * Configures everything for a given context.
 * The FacesConfigurator is independent of the concrete implementations that lie
 * behind FacesConfigUnmarshaller and FacesConfigDispenser.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/17 14:28:28  manolito
 * new configuration concept
 *
 */
public class FacesConfigurator
{
    private static final Log log = LogFactory.getLog(FacesConfigurator.class);

    private static final String STANDARD_FACES_CONFIG_RESOURCE
        = "net.sourceforge.myfaces.resource".replace('.', '/') + "/standard-faces-config.xml";
    private static final String CONFIG_FILES_INIT_PARAM
        = "javax.faces.CONFIG_FILES";


    public static final String META_INF_SERVICES_LOCATION = "/META-INF/services/";

    private ExternalContext _externalContext;
    private FacesConfigUnmarshaller _unmarshaller;
    private FacesConfigDispenser _dispenser;

    public FacesConfigurator(ExternalContext externalContext)
    {
        _externalContext = externalContext;

    }

    public void configure()
        throws FacesException
    {
        //TODO: create via Factory !
        _unmarshaller = new DOMFacesConfigUnmarshallerImpl(_externalContext);
        //TODO: create via Factory !
        _dispenser = new DOMFacesConfigDispenserImpl();

        feedStandardConfig();
        feedMetaInfServicesFactories();
        feedJarFileConfigurations();
        feedContextSpecifiedConfig();
        feedWebAppConfig();

        configureFactories();
        configureApplication();
        configureRenderKits();
        configureRuntimeConfig();
        configureLifecycle();
    }

    private void feedStandardConfig()
    {
        InputStream stream = ClassUtils.getResourceAsStream(STANDARD_FACES_CONFIG_RESOURCE);
        if (stream == null) throw new FacesException("Standard faces config " + STANDARD_FACES_CONFIG_RESOURCE + " not found");
        if (log.isInfoEnabled()) log.info("Reading standard config " + STANDARD_FACES_CONFIG_RESOURCE);
        _dispenser.feed(_unmarshaller.getFacesConfig(stream, STANDARD_FACES_CONFIG_RESOURCE));
    }

    /**
     * This method performs part of the factory search outlined in section 10.2.6.1.
     * <p/>
     * FIXME: Should this also search through all the jar files in the WEB-INF/lib
     * directory?
     */
    protected void feedMetaInfServicesFactories()
    {
        Set factoryNames = FactoryFinder.getFactoryNames();
        // keyed on resource names, factory name is the value
        Map resourceNames = expandFactoryNames(factoryNames);
        //Search for factory files in the jar file
        Set services = _externalContext.getResourcePaths(META_INF_SERVICES_LOCATION);
        // retainAll performs the intersection of the factory names that we
        // are looking for the ones found, only the services found that match
        // the expected factory names will be retained
        if (null != services)
        {
            services.retainAll(resourceNames.keySet());
            Iterator itr = services.iterator();
            while (itr.hasNext())
            {
                String resourceName = (String) itr.next();
                InputStream is = _externalContext.getResourceAsStream(resourceName);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String className = null;
                try
                {
                    className = br.readLine();
                }
                catch (IOException e)
                {
                    throw new FacesException("Unable to read class name from file "
                        + resourceName, e);
                }

                String factoryName = (String)resourceNames.get(resourceName);
                if (factoryName.equals(FactoryFinder.APPLICATION_FACTORY))
                {
                    _dispenser.feedApplicationFactory(className);
                }
                else if (factoryName.equals(FactoryFinder.FACES_CONTEXT_FACTORY))
                {
                    _dispenser.feedFacesContextFactory(className);
                }
                else if (factoryName.equals(FactoryFinder.LIFECYCLE_FACTORY))
                {
                    _dispenser.feedLifecycleFactory(className);
                }
                else if (factoryName.equals(FactoryFinder.RENDER_KIT_FACTORY))
                {
                    _dispenser.feedRenderKitFactory(className);
                }
                else
                {
                    throw new IllegalStateException("Unexpected factory name " + factoryName);
                }
            }
        }
    }

    private Map expandFactoryNames(Set factoryNames)
    {
        Map names = new HashMap();
        Iterator itr = factoryNames.iterator();
        while (itr.hasNext())
        {
            String name = (String) itr.next();
            names.put(META_INF_SERVICES_LOCATION + name, name);
        }
        return names;
    }


    private void feedJarFileConfigurations()
    {
        Set jars = _externalContext.getResourcePaths("/WEB-INF/lib/");
        if (jars != null)
        {
            for (Iterator it = jars.iterator(); it.hasNext();)
            {
                String path = (String)it.next();
                if (path.toLowerCase().endsWith(".jar"))
                {
                    feedJarConfig(path);
                }
            }
        }
    }

    private void feedJarConfig(String jarPath)
        throws FacesException
    {
        try
        {
            if (!(_externalContext.getContext() instanceof ServletContext))
            {
                log.error("ServletContext expected");
                return;
            }
            ServletContext servletContext = (ServletContext) _externalContext.getContext();

            // not all containers expand archives, so we have to do it the generic way:
            // 1. get the stream from servlet context
            InputStream in = servletContext.getResourceAsStream(jarPath);
            if (in == null)
            {
                if (jarPath.startsWith("/"))
                {
                    in = servletContext.getResourceAsStream(jarPath.substring(1));
                }
                else
                {
                    in = servletContext.getResourceAsStream("/" + jarPath);
                }
            }
            if (in == null)
            {
                log.error("Resource " + jarPath + " not found");
                return;
            }

            // 2. search the jar stream for META-INF/faces-config.xml
            JarInputStream jar = new JarInputStream(in);
            JarEntry entry = jar.getNextJarEntry();
            boolean found = false;

            while (entry != null)
            {
                if (entry.getName().equals("META-INF/faces-config.xml"))
                {
                    if (log.isDebugEnabled()) log.debug("faces-config.xml found in " + jarPath);
                    found = true;
                    break;
                }
                entry = jar.getNextJarEntry();
            }
            jar.close();

            File tmp = null;

            // 3. if faces-config.xml was found, extract the jar and copy it to a temp file; hand over the temp file
            // to the parser and delete it afterwards
            if (found)
            {
                tmp = File.createTempFile("myfaces", ".jar");
                in = servletContext.getResourceAsStream(jarPath);
                FileOutputStream out = new FileOutputStream(tmp);
                byte[] buffer = new byte[4096];
                int r;

                while ((r = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, r);
                }
                out.close();

                try {
                    JarFile jarFile = new JarFile(tmp);
                    JarEntry configFile = jarFile.getJarEntry("META-INF/faces-config.xml");
                    if (configFile != null)
                    {
                        if (log.isDebugEnabled()) log.debug("faces-config.xml found in jar " + jarPath);
                        InputStream stream = jarFile.getInputStream(configFile);
                        String systemId = "jar:" + tmp.toURL() + "!/" + configFile.getName();
                        if (log.isInfoEnabled()) log.info("Reading config " + systemId);
                        _dispenser.feed(_unmarshaller.getFacesConfig(stream, systemId));
                    }
                } finally {
                    tmp.delete();
                }
            } else
            {
                if (log.isDebugEnabled()) log.debug("Jar " + jarPath + " contains no faces-config.xml");
            }
        } catch (IOException e)
        {
            throw new FacesException(e);
        }
    }

    private void feedContextSpecifiedConfig()
    {
        String configFiles = _externalContext.getInitParameter(CONFIG_FILES_INIT_PARAM);
        if (configFiles != null)
        {
            StringTokenizer st = new StringTokenizer(configFiles, ",", false);
            while (st.hasMoreTokens())
            {
                String systemId = st.nextToken().trim();
                InputStream stream = _externalContext.getResourceAsStream(systemId);
                if (stream == null)
                {
                    log.error("Faces config resource " + systemId + " not found");
                    continue;
                }

                if (log.isInfoEnabled()) log.info("Reading config " + systemId);

                _dispenser.feed(_unmarshaller.getFacesConfig(stream, systemId));
            }
        }
    }

    private void feedWebAppConfig()
    {
        //web application config
        String systemId = "/WEB-INF/faces-config.xml";
        InputStream stream = _externalContext.getResourceAsStream(systemId);
        if (stream != null)
        {
            if (log.isInfoEnabled()) log.info("Reading config /WEB-INF/faces-config.xml");

            _dispenser.feed(_unmarshaller.getFacesConfig(stream, systemId));
        }
    }










    private void configureFactories()
    {
        setFactories(FactoryFinder.APPLICATION_FACTORY, _dispenser.getApplicationFactoryIterator());
        setFactories(FactoryFinder.FACES_CONTEXT_FACTORY, _dispenser.getFacesContextFactoryIterator());
        setFactories(FactoryFinder.LIFECYCLE_FACTORY, _dispenser.getLifecycleFactoryIterator());
        setFactories(FactoryFinder.RENDER_KIT_FACTORY, _dispenser.getRenderKitFactoryIterator());
    }

    private void setFactories(String factoryName, Iterator factories)
    {
        while (factories.hasNext())
        {
            FactoryFinder.setFactory(factoryName, (String)factories.next());
        }
    }


    private void configureApplication()
    {
        Application application = (Application)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application.setActionListener((ActionListener)getApplicationObject(ActionListener.class,
                                                                           _dispenser.getActionListenerIterator()));
        application.setDefaultLocale(locale(_dispenser.getDefaultLocale()));
        application.setDefaultRenderKitId(_dispenser.getDefaultRenderKitId());
        application.setMessageBundle(_dispenser.getMessageBundle());
        application.setNavigationHandler((NavigationHandler)getApplicationObject(NavigationHandler.class,
                                                                                 _dispenser.getNavigationHandlerIterator()));
        application.setPropertyResolver((PropertyResolver)getApplicationObject(PropertyResolver.class,
                                                                               _dispenser.getPropertyResolverIterator()));
        application.setStateManager((StateManager)getApplicationObject(StateManager.class,
                                                                       _dispenser.getStateManagerIterator()));
        List locales = new ArrayList();
        for (Iterator it = _dispenser.getSupportedLocalesIterator(); it.hasNext(); )
        {
            locales.add(locale((String)it.next()));
        }
        application.setSupportedLocales(locales);

        application.setVariableResolver((VariableResolver)getApplicationObject(VariableResolver.class,
                                                                               _dispenser.getVariableResolverIterator()));
        application.setViewHandler((ViewHandler)getApplicationObject(ViewHandler.class,
                                                                     _dispenser.getViewHandlerIterator()));

        for (Iterator it = _dispenser.getComponentTypes(); it.hasNext(); )
        {
            String componentType = (String)it.next();
            application.addComponent(componentType,
                                     _dispenser.getComponentClass(componentType));
        }

        for (Iterator it = _dispenser.getConverterIds(); it.hasNext(); )
        {
            String converterId = (String)it.next();
            application.addConverter(converterId,
                                     _dispenser.getConverterClassById(converterId));
        }

        for (Iterator it = _dispenser.getConverterClasses(); it.hasNext(); )
        {
            String converterClass = (String)it.next();
            application.addConverter(ClassUtils.classForName(converterClass),
                                     _dispenser.getConverterClassByClass(converterClass));
        }

        for (Iterator it = _dispenser.getValidatorIds(); it.hasNext(); )
        {
            String validatorId = (String)it.next();
            application.addValidator(validatorId,
                                     _dispenser.getValidatorClass(validatorId));
        }
    }

    private Object getApplicationObject(Class interfaceClass, Iterator classNamesIterator)
    {
        if (!classNamesIterator.hasNext())
        {
            throw new IllegalStateException("No implementation class for " + interfaceClass.getName() + " defined!");

        }
        String implClassName = (String)classNamesIterator.next();
        Class implClass = ClassUtils.classForName(implClassName);

        // check, if class is of expected interface type
        if (!interfaceClass.isAssignableFrom(implClass))
        {
            throw new IllegalArgumentException("Class " + implClassName + " is no " + interfaceClass.getName());
        }

        if (classNamesIterator.hasNext())
        {
            // there is a superordinate class,
            // so let's check if class supports the decorator pattern
            try
            {
                Constructor delegationConstructor = implClass.getConstructor(new Class[] {interfaceClass});
                // impl class supports decorator pattern,
                // get instance of superordinate class
                Object superObj = getApplicationObject(interfaceClass, classNamesIterator);
                // and call constructor with this object as argument
                try
                {
                    return delegationConstructor.newInstance(new Object[] {superObj});
                }
                catch (InstantiationException e)
                {
                    log.error(e.getMessage(), e);
                    throw new FacesException(e);
                }
                catch (IllegalAccessException e)
                {
                    log.error(e.getMessage(), e);
                    throw new FacesException(e);
                }
                catch (InvocationTargetException e)
                {
                    log.error(e.getMessage(), e);
                    throw new FacesException(e);
                }
            }
            catch (NoSuchMethodException e)
            {
                // decorator pattern not supported
            }
        }

        // no superordinate class or decorator pattern not supported
        return ClassUtils.newInstance(implClass);
    }


    private Locale locale(String name)
    {
        if ((name == null) || (name.length() == 0))
        {
            log.error("Default locale name null or empty, ignoring");
        }

        char     separator      = (name.indexOf('_') >= 0) ? '_' : '-';

        String[] nameComponents = StringUtils.splitShortString(name, separator);

        switch (nameComponents.length)
        {
            case 1:
                return new Locale(nameComponents[0]);

            case 2:
                return new Locale(nameComponents[0], nameComponents[1]);

            case 3:
                return new Locale(nameComponents[0], nameComponents[1], nameComponents[2]);

            default:
                log.error("Invalid default locale name, ignoring: " + name);
        }
        return null;
    }



    private void configureRuntimeConfig()
    {
        RuntimeConfig runtimeConfig = RuntimeConfig.getCurrentInstance(_externalContext);

        //TODO: managed-bean
        //TODO: navigation-rule
    }

    private void configureRenderKits()
    {
        RenderKitFactory renderKitFactory
                = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        //TODO
    }


    private void configureLifecycle()
    {
        //TODO
    }
}
