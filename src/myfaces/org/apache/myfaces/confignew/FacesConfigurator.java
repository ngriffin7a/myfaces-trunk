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

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.webapp.FacesServlet;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.application.*;
import javax.faces.context.ExternalContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;

import net.sourceforge.myfaces.confignew.element.ManagedBean;
import net.sourceforge.myfaces.confignew.element.NavigationRule;
import net.sourceforge.myfaces.confignew.element.Renderer;
import net.sourceforge.myfaces.confignew.impl.dom.DOMFacesConfigDispenserImpl;
import net.sourceforge.myfaces.confignew.impl.dom.DOMFacesConfigUnmarshallerImpl;
import net.sourceforge.myfaces.confignew.impl.digester.DigesterFacesConfigUnmarshallerImpl;
import net.sourceforge.myfaces.confignew.impl.digester.DigesterFacesConfigDispenserImpl;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderKitImpl;
import net.sourceforge.myfaces.renderkit.RenderKitFactoryImpl;
import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.util.StringUtils;
import net.sourceforge.myfaces.application.jsp.JspStateManagerImpl;
import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;
import net.sourceforge.myfaces.application.NavigationHandlerImpl;
import net.sourceforge.myfaces.application.ActionListenerImpl;
import net.sourceforge.myfaces.application.ApplicationFactoryImpl;
import net.sourceforge.myfaces.el.PropertyResolverImpl;
import net.sourceforge.myfaces.el.VariableResolverImpl;
import net.sourceforge.myfaces.context.FacesContextFactoryImpl;
import net.sourceforge.myfaces.lifecycle.LifecycleFactoryImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;


/**
 * Configures everything for a given context.
 * The FacesConfigurator is independent of the concrete implementations that lie
 * behind FacesConfigUnmarshaller and FacesConfigDispenser.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
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
        = "net.sourceforge.myfaces.resource".replace('.', '/') + "/standard-faces-config.xml";

    public static final String APPLICATION_MAP_LIFECYCLE_KEY = Lifecycle.class.getName();

    public static final String META_INF_SERVICES_LOCATION = "/META-INF/services/";

    private static final String DEFAULT_RENDER_KIT_CLASS = HtmlRenderKitImpl.class.getName();
    private static final String DEFAULT_STATE_MANAGER_CLASS = JspStateManagerImpl.class.getName();
    private static final String DEFAULT_VIEW_HANDLER_CLASS = JspViewHandlerImpl.class.getName();
    private static final String DEFAULT_NAVIGATION_HANDLER_CLASS = NavigationHandlerImpl.class.getName();
    private static final String DEFAULT_PROPERTY_RESOLVER_CLASS = PropertyResolverImpl.class.getName();
    private static final String DEFAULT_VARIABLE_RESOLVER_CLASS = VariableResolverImpl.class.getName();
    private static final String DEFAULT_ACTION_LISTENER_CLASS = ActionListenerImpl.class.getName();
    private static final String DEFAULT_APPLICATION_FACTORY = ApplicationFactoryImpl.class.getName();
    private static final String DEFAULT_FACES_CONTEXT_FACTORY = FacesContextFactoryImpl.class.getName();
    private static final String DEFAULT_LIFECYCLE_FACTORY = LifecycleFactoryImpl.class.getName();
    private static final String DEFAULT_RENDER_KIT_FACTORY = RenderKitFactoryImpl.class.getName();


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
        _unmarshaller = new DigesterFacesConfigUnmarshallerImpl();
        //TODO: create via Factory !
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
        Set factoryNames = FactoryFinder.getValidFactoryNames();
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
                } else
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

                try
                {
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
                } finally
                {
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
            application.setDefaultLocale(locale(_dispenser.getDefaultLocale()));
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
            locales.add(locale((String) it.next()));
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
            application.addConverter(ClassUtils.classForName(converterClass),
                _dispenser.getConverterClassByClass(converterClass));
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
            Class implClass = ClassUtils.classForName(implClassName);

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


    private Locale locale(String name)
    {
        if ((name == null) || (name.length() == 0))
        {
            log.error("Default locale name null or empty, ignoring");
            return null;
        }

        char separator = (name.indexOf('_') >= 0) ? '_' : '-';

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
                javax.faces.render.Renderer renderer = (javax.faces.render.Renderer) ClassUtils.newInstance(element.getRendererClass());

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
        if (_externalContext.getContext() instanceof ServletContext)
        {
            ServletContext context = (ServletContext) _externalContext.getContext();
            String id = context.getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);

            if (id != null)
            {
                return id;
            }
        } else
        {
            log.warn("No servlet context available, don't know how to obtain LIFECYCLE_ID. Using default!");
        }
        return LifecycleFactory.DEFAULT_LIFECYCLE;
    }
}
