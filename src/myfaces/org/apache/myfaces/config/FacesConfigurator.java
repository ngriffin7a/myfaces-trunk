/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.FacesServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.application.ApplicationFactoryImpl;
import org.apache.myfaces.config.element.ManagedBean;
import org.apache.myfaces.config.element.NavigationRule;
import org.apache.myfaces.config.element.Renderer;
import org.apache.myfaces.config.impl.digester.DigesterFacesConfigDispenserImpl;
import org.apache.myfaces.config.impl.digester.DigesterFacesConfigUnmarshallerImpl;
import org.apache.myfaces.context.FacesContextFactoryImpl;
import org.apache.myfaces.lifecycle.LifecycleFactoryImpl;
import org.apache.myfaces.renderkit.RenderKitFactoryImpl;
import org.apache.myfaces.renderkit.html.HtmlRenderKitImpl;
import org.apache.myfaces.util.ClassUtils;
import org.apache.myfaces.util.LocaleUtils;
import org.xml.sax.SAXException;


/**
 * Configures everything for a given context.
 * The FacesConfigurator is independent of the concrete implementations that lie
 * behind FacesConfigUnmarshaller and FacesConfigDispenser.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.10  2005/01/26 17:03:11  matzew
 *          MYFACES-86. portlet support provided by Stan Silver (JBoss Group)
 *
 *          Revision 1.9  2004/12/13 22:20:34  oros
 *          fix #1046763: close temporary jar file before trying to delete it
 *
 *          Revision 1.8  2004/11/11 22:46:35  bdudney
 *          added some error reporting
 *
 *          Revision 1.7  2004/10/13 11:50:59  matze
 *          renamed packages to org.apache
 *
 *          Revision 1.6  2004/08/23 05:13:39  dave0000
 *          Externalize String-to-Locale conversion
 *
 *          Revision 1.5  2004/08/10 10:57:38  manolito
 *          fixed StackOverflow in ClassUtils and cleaned up ClassUtils methods
 *
 *          Revision 1.4  2004/07/20 14:56:41  manolito
 *          removed public FactoryFinder method getValidFactoryNames - there is no such method in JSF 1.1 !
 *
 *          Revision 1.3  2004/07/13 06:42:43  tinytoony
 *          does not break if converter-class has not been found, instead logs as error.
 *
 *          Revision 1.2  2004/07/07 08:34:58  mwessendorf
 *          removed unused import-statements
 *
 *          Revision 1.1  2004/07/07 00:25:05  o_rossmueller
 *          tidy up config/confignew package (moved confignew classes to package config)
 *
 *          Revision 1.7  2004/07/06 23:21:19  o_rossmueller
 *          fix #985217: decoration support for factories
 *
 *          Revision 1.6  2004/07/01 22:05:09  mwessendorf
 *          ASF switch
 *
 *          Revision 1.5  2004/06/17 23:23:48  o_rossmueller
 *          fix: entity resolver
 *
 *          Revision 1.4  2004/06/16 23:02:24  o_rossmueller
 *          merged confignew_branch
 *
 *          Revision 1.3.2.3  2004/06/16 01:25:52  o_rossmueller
 *          refactorings: FactoryFinder, decorator creation, dispenser (removed reverse order)
 *          bug fixes
 *          additional tests
 *
 *          Revision 1.3.2.2  2004/06/15 11:54:22  o_rossmueller
 *          fixed decorator pattern support
 *          <p/>
 *          Revision 1.3.2.1  2004/06/13 15:59:07  o_rossmueller
 *          started integration of new config mechanism:
 *          - factories
 *          - components
 *          - render kits
 *          - managed beans + managed properties (no list/map initialization)
 *          <p/>
 *          Revision 1.3  2004/06/08 20:50:09  o_rossmueller
 *          completed configurator
 *          <p/>
 *          Revision 1.2  2004/06/04 23:51:48  o_rossmueller
 *          Digester-based config parser/dispenser
 *          <p/>
 *          Revision 1.1  2004/05/17 14:28:28  manolito
 *          new configuration concept
 */
public class FacesConfigurator
{

    private static final Log log = LogFactory.getLog(FacesConfigurator.class);

    private static final String STANDARD_FACES_CONFIG_RESOURCE
        = "org.apache.myfaces.resource".replace('.', '/') + "/standard-faces-config.xml";

    public static final String APPLICATION_MAP_LIFECYCLE_KEY = Lifecycle.class.getName();

    public static final String META_INF_SERVICES_LOCATION = "/META-INF/services/";

    private static final String DEFAULT_RENDER_KIT_CLASS = HtmlRenderKitImpl.class.getName();
    private static final String DEFAULT_APPLICATION_FACTORY = ApplicationFactoryImpl.class.getName();
    private static final String DEFAULT_FACES_CONTEXT_FACTORY = FacesContextFactoryImpl.class.getName();
    private static final String DEFAULT_LIFECYCLE_FACTORY = LifecycleFactoryImpl.class.getName();
    private static final String DEFAULT_RENDER_KIT_FACTORY = RenderKitFactoryImpl.class.getName();

    private static final Set FACTORY_NAMES  = new HashSet();
    {
        FACTORY_NAMES.add(FactoryFinder.APPLICATION_FACTORY);
        FACTORY_NAMES.add(FactoryFinder.FACES_CONTEXT_FACTORY);
        FACTORY_NAMES.add(FactoryFinder.LIFECYCLE_FACTORY);
        FACTORY_NAMES.add(FactoryFinder.RENDER_KIT_FACTORY);
    }


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
        //These two classes can be easily replaced by alternative implementations.
        //As long as there is no need to switch implementations we need no
        //factory pattern to create them.
        _unmarshaller = new DigesterFacesConfigUnmarshallerImpl(_externalContext);
        _dispenser = new DigesterFacesConfigDispenserImpl();

        try
        {
            feedStandardConfig();
            feedMetaInfServicesFactories();
            feedJarFileConfigurations();
            feedContextSpecifiedConfig();
            feedWebAppConfig();
        } catch (IOException e)
        {
            throw new FacesException(e);
        } catch (SAXException e)
        {
            throw new FacesException(e);
        }

        configureFactories();
        configureApplication();
        configureRenderKits();
        configureRuntimeConfig();
        configureLifecycle();
    }


    private void feedStandardConfig() throws IOException, SAXException
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
        // keyed on resource names, factory name is the value
        Map resourceNames = expandFactoryNames(FACTORY_NAMES);
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
                } catch (IOException e)
                {
                    throw new FacesException("Unable to read class name from file "
                        + resourceName, e);
                }

                String factoryName = (String) resourceNames.get(resourceName);
                if (factoryName.equals(FactoryFinder.APPLICATION_FACTORY))
                {
                    _dispenser.feedApplicationFactory(className);
                } else if (factoryName.equals(FactoryFinder.FACES_CONTEXT_FACTORY))
                {
                    _dispenser.feedFacesContextFactory(className);
                } else if (factoryName.equals(FactoryFinder.LIFECYCLE_FACTORY))
                {
                    _dispenser.feedLifecycleFactory(className);
                } else if (factoryName.equals(FactoryFinder.RENDER_KIT_FACTORY))
                {
                    _dispenser.feedRenderKitFactory(className);
                } else
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
                String path = (String) it.next();
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
            // not all containers expand archives, so we have to do it the generic way:
            // 1. get the stream from external context
            InputStream in = _externalContext.getResourceAsStream(jarPath);
            if (in == null)
            {
                if (jarPath.startsWith("/"))
                {
                    in = _externalContext.getResourceAsStream(jarPath.substring(1));
                } else
                {
                    in = _externalContext.getResourceAsStream("/" + jarPath);
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
                in = _externalContext.getResourceAsStream(jarPath);
                FileOutputStream out = new FileOutputStream(tmp);
                byte[] buffer = new byte[4096];
                int r;

                while ((r = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, r);
                }
                out.close();

                JarFile jarFile = new JarFile(tmp);
                try
                {
                    JarEntry configFile = jarFile.getJarEntry("META-INF/faces-config.xml");
                    if (configFile != null)
                    {
                        if (log.isDebugEnabled()) log.debug("faces-config.xml found in jar " + jarPath);
                        InputStream stream = jarFile.getInputStream(configFile);
                        String systemId = "jar:" + tmp.toURL() + "!/" + configFile.getName();
                        if (log.isInfoEnabled()) log.info("Reading config " + systemId);
                        _dispenser.feed(_unmarshaller.getFacesConfig(stream, systemId));
                    }
                } finally
                {
                    jarFile.close();
                    tmp.delete();
                }
            } else
            {
                if (log.isDebugEnabled()) log.debug("Jar " + jarPath + " contains no faces-config.xml");
            }
        } catch (Exception e)
        {
            throw new FacesException(e);
        }
    }


    private void feedContextSpecifiedConfig() throws IOException, SAXException
    {
        String configFiles = _externalContext.getInitParameter(FacesServlet.CONFIG_FILES_ATTR);
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


    private void feedWebAppConfig() throws IOException, SAXException
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
        setFactories(FactoryFinder.APPLICATION_FACTORY, _dispenser.getApplicationFactoryIterator(), DEFAULT_APPLICATION_FACTORY);
        setFactories(FactoryFinder.FACES_CONTEXT_FACTORY, _dispenser.getFacesContextFactoryIterator(), DEFAULT_FACES_CONTEXT_FACTORY);
        setFactories(FactoryFinder.LIFECYCLE_FACTORY, _dispenser.getLifecycleFactoryIterator(), DEFAULT_LIFECYCLE_FACTORY);
        setFactories(FactoryFinder.RENDER_KIT_FACTORY, _dispenser.getRenderKitFactoryIterator(), DEFAULT_RENDER_KIT_FACTORY);
    }


    private void setFactories(String factoryName, Iterator factories, String defaultFactory)
    {
        FactoryFinder.setFactory(factoryName, defaultFactory);
        while (factories.hasNext())
        {
            FactoryFinder.setFactory(factoryName, (String) factories.next());
        }
    }


    private void configureApplication()
    {
        Application application = ((ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY)).getApplication();
        application.setActionListener((ActionListener) getApplicationObject(ActionListener.class, _dispenser.getActionListenerIterator(), null));

        if (_dispenser.getDefaultLocale() != null)
        {
            application.setDefaultLocale(
                LocaleUtils.toLocale(_dispenser.getDefaultLocale()));
        }

        if (_dispenser.getDefaultRenderKitId() != null)
        {
            application.setDefaultRenderKitId(_dispenser.getDefaultRenderKitId());
        }

        if (_dispenser.getMessageBundle() != null)
        {
            application.setMessageBundle(_dispenser.getMessageBundle());
        }

        application.setNavigationHandler((NavigationHandler) getApplicationObject(NavigationHandler.class,
            _dispenser.getNavigationHandlerIterator(), application.getNavigationHandler()));
        application.setPropertyResolver((PropertyResolver) getApplicationObject(PropertyResolver.class,
            _dispenser.getPropertyResolverIterator(), application.getPropertyResolver()));
        application.setStateManager((StateManager) getApplicationObject(StateManager.class,
            _dispenser.getStateManagerIterator(), application.getStateManager()));
        List locales = new ArrayList();
        for (Iterator it = _dispenser.getSupportedLocalesIterator(); it.hasNext();)
        {
            locales.add(LocaleUtils.toLocale((String) it.next()));
        }
        application.setSupportedLocales(locales);

        application.setVariableResolver((VariableResolver) getApplicationObject(VariableResolver.class,
            _dispenser.getVariableResolverIterator(), application.getVariableResolver()));
        application.setViewHandler((ViewHandler) getApplicationObject(ViewHandler.class,
            _dispenser.getViewHandlerIterator(), application.getViewHandler()));

        for (Iterator it = _dispenser.getComponentTypes(); it.hasNext();)
        {
            String componentType = (String) it.next();
            application.addComponent(componentType,
                _dispenser.getComponentClass(componentType));
        }

        for (Iterator it = _dispenser.getConverterIds(); it.hasNext();)
        {
            String converterId = (String) it.next();
            application.addConverter(converterId,
                _dispenser.getConverterClassById(converterId));
        }

        for (Iterator it = _dispenser.getConverterClasses(); it.hasNext();)
        {
            String converterClass = (String) it.next();
            try
            {
                application.addConverter(ClassUtils.simpleClassForName(converterClass),
                    _dispenser.getConverterClassByClass(converterClass));
            }
            catch(Exception ex)
            {
                log.error("Converter could not be added. Reason:",ex);
            }
        }

        for (Iterator it = _dispenser.getValidatorIds(); it.hasNext();)
        {
            String validatorId = (String) it.next();
            application.addValidator(validatorId,
                _dispenser.getValidatorClass(validatorId));
        }
    }


    private Object getApplicationObject(Class interfaceClass, Iterator classNamesIterator, Object defaultObject)
    {
        Object current = defaultObject;

        while (classNamesIterator.hasNext())
        {
            String implClassName = (String) classNamesIterator.next();
            Class implClass = ClassUtils.simpleClassForName(implClassName);

            // check, if class is of expected interface type
            if (!interfaceClass.isAssignableFrom(implClass))
            {
                throw new IllegalArgumentException("Class " + implClassName + " is no " + interfaceClass.getName());
            }

            if (current == null)
            {
                // nothing to decorate
                current = ClassUtils.newInstance(implClass);
            } else
            {
                // let's check if class supports the decorator pattern
                try
                {
                    Constructor delegationConstructor = implClass.getConstructor(new Class[]{interfaceClass});
                    // impl class supports decorator pattern,
                    try
                    {
                        // create new decorator wrapping current
                        current = delegationConstructor.newInstance(new Object[]{current});
                    } catch (InstantiationException e)
                    {
                        log.error(e.getMessage(), e);
                        throw new FacesException(e);
                    } catch (IllegalAccessException e)
                    {
                        log.error(e.getMessage(), e);
                        throw new FacesException(e);
                    } catch (InvocationTargetException e)
                    {
                        log.error(e.getMessage(), e);
                        throw new FacesException(e);
                    }
                } catch (NoSuchMethodException e)
                {
                    // no decorator pattern support
                    current = ClassUtils.newInstance(implClass);
                }
            }
        }

        return current;
    }


    private void configureRuntimeConfig()
    {
        RuntimeConfig runtimeConfig = RuntimeConfig.getCurrentInstance(_externalContext);

        for (Iterator iterator = _dispenser.getManagedBeans(); iterator.hasNext();)
        {
            ManagedBean bean = (ManagedBean) iterator.next();
            runtimeConfig.addManagedBean(bean.getManagedBeanName(), bean);

        }

        for (Iterator iterator = _dispenser.getNavigationRules(); iterator.hasNext();)
        {
            NavigationRule rule = (NavigationRule) iterator.next();
            runtimeConfig.addNavigationRule(rule);

        }
    }


    private void configureRenderKits()
    {
        RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

        for (Iterator iterator = _dispenser.getRenderKitIds(); iterator.hasNext();)
        {
            String renderKitId = (String) iterator.next();
            String renderKitClass = _dispenser.getRenderKitClass(renderKitId);

            if (renderKitClass == null)
            {
                renderKitClass = DEFAULT_RENDER_KIT_CLASS;
            }

            RenderKit renderKit = (RenderKit) ClassUtils.newInstance(renderKitClass);

            for (Iterator renderers = _dispenser.getRenderers(renderKitId); renderers.hasNext();)
            {
                Renderer element = (Renderer) renderers.next();
                javax.faces.render.Renderer renderer = null;
                try {
                  renderer = (javax.faces.render.Renderer) ClassUtils.newInstance(element.getRendererClass());
                } catch(FacesException e) {
                  // ignore the failure so that the render kit is configured
                  log.error("failed to configure class " + element.getRendererClass(), e);
                  continue;
                }

                renderKit.addRenderer(element.getComponentFamily(), element.getRendererType(), renderer);
            }

            renderKitFactory.addRenderKit(renderKitId, renderKit);
        }
    }


    private void configureLifecycle()
    {
        // create the lifecycle used by the app
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());

        // add phase listeners
        for (Iterator iterator = _dispenser.getLifecyclePhaseListeners(); iterator.hasNext();)
        {
            String listenerClassName = (String) iterator.next();
            try
            {
                lifecycle.addPhaseListener((PhaseListener) ClassUtils.newInstance(listenerClassName));
            } catch (ClassCastException e)
            {
                log.error("Class " + listenerClassName + " does not implement PhaseListener");
            }
        }
    }


    private String getLifecycleId()
    {
        String id = _externalContext.getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);

        if (id != null)
        {
            return id;
        }

        return LifecycleFactory.DEFAULT_LIFECYCLE;
    }
}
