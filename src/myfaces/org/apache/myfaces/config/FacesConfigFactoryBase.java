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
package net.sourceforge.myfaces.config;

import net.sourceforge.myfaces.util.ClassUtils;
import org.xml.sax.EntityResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.26  2004/04/29 17:46:14  o_rossmueller
 *          extract faces-config.xml to temp file so it works for Weblogic, too
 *
 *          Revision 1.25  2004/04/13 08:26:49  manolito
 *          Log
 */
public abstract class FacesConfigFactoryBase
    implements FacesConfigFactory
{

    private static final Log log = LogFactory.getLog(FacesConfigFactoryBase.class);

    private static HashSet factoryNames = new HashSet();
    public static final String META_INF_SERVICES_LOCATION = "/META-INF/services/";

    private static final String STANDARD_FACES_CONFIG_RESOURCE
        = "net.sourceforge.myfaces.resource".replace('.', '/') + "/standard-faces-config.xml";
    private static final String CONFIG_FILES_INIT_PARAM
        = "javax.faces.application.CONFIG_FILES";

    private static final String FACES_CONFIG_ATTR = FacesConfig.class.getName();


    public FacesConfig getFacesConfig(ExternalContext context)
    {
        Map appMap = context.getApplicationMap();
        FacesConfig facesConfig = (FacesConfig) appMap.get(FACES_CONFIG_ATTR);
        if (facesConfig != null)
        {
            return facesConfig;
        }

        facesConfig = new FacesConfig();
        parseFacesConfigFiles(facesConfig, context);

        appMap.put(FACES_CONFIG_ATTR, facesConfig);
        return facesConfig;
    }


    protected abstract void parseFacesConfig(FacesConfig facesConfig,
                                             InputStream in,
                                             String systemId,
                                             EntityResolver entityResolver) throws IOException, FacesException;


    private void parseFacesConfigFiles(FacesConfig facesConfig, ExternalContext context)
        throws FacesException
    {
        InputStream stream;

        // load the standard config stuff from my-faces
        performStandardFacesConfig(facesConfig);
        	
        // this might need to use some of the code from the search through jar files
        performMetaInfFactoryConfig(facesConfig, context);

        performJarFileConfig(facesConfig, context);

        performContextSpecifiedConfig(facesConfig, context);

        performWebAppConfig(facesConfig, context);
    }


    private void performWebAppConfig(FacesConfig facesConfig, ExternalContext context)
    {
        //web application config
        String systemId = "/WEB-INF/faces-config.xml";
        InputStream stream = context.getResourceAsStream(systemId);
        if (stream != null)
        {
            if (log.isInfoEnabled()) log.info("Reading config /WEB-INF/faces-config.xml");
            parseStreamConfig(facesConfig, stream, systemId,
                new FacesConfigEntityResolver(context));
        }
    }


    private void performContextSpecifiedConfig(FacesConfig facesConfig, ExternalContext context)
    {
        String configFiles = context.getInitParameter(CONFIG_FILES_INIT_PARAM);
        if (configFiles != null)
        {
            StringTokenizer st = new StringTokenizer(configFiles, ",", false);
            while (st.hasMoreTokens())
            {
                String systemId = st.nextToken().trim();
                InputStream stream = context.getResourceAsStream(systemId);
                if (stream == null)
                {
                    log.error("Faces config resource " + systemId + " not found");
                    continue;
                }

                if (log.isInfoEnabled()) log.info("Reading config " + systemId);

                parseStreamConfig(facesConfig, stream, systemId,
                    new FacesConfigEntityResolver(context));
            }
        }
    }


    private void performJarFileConfig(FacesConfig facesConfig, ExternalContext context)
    {
        Set jars = context.getResourcePaths("/WEB-INF/lib/");
        if (jars != null)
        {
            for (Iterator it = jars.iterator(); it.hasNext();)
            {
                String path = (String) it.next();
                if (path.toLowerCase().endsWith(".jar"))
                {
                    parseJarConfig(facesConfig, context, path);
                }
            }
        }
    }


    private void performStandardFacesConfig(FacesConfig facesConfig)
    {
        InputStream stream = ClassUtils.getResourceAsStream(STANDARD_FACES_CONFIG_RESOURCE);
        if (stream == null)
        {
            throw new FacesException("Standard faces config " + STANDARD_FACES_CONFIG_RESOURCE + " not found");
        }
        if (log.isInfoEnabled()) log.info("Reading standard config " + STANDARD_FACES_CONFIG_RESOURCE);
        parseStreamConfig(facesConfig, stream, STANDARD_FACES_CONFIG_RESOURCE,
            new FacesConfigEntityResolver());
    }


    /**
     * This method performs part of the factory search outlined in section 10.2.6.1.
     * Fails early if the class name can not be found.
     * <p/>
     * FIXME: Should this also search through all the jar files in the WEB-INF/lib
     * directory?
     */
    protected void performMetaInfFactoryConfig(FacesConfig facesConfig,
                                               ExternalContext context) throws FacesException
    {
        Set factoryNames = FactoryFinder.getFactoryNames();
        // keyed on resource names, factory name is the value
        Map resourceNames = expandFactoryNames(factoryNames);
        //Search for factory files in the jar file
        Set services = context.getResourcePaths(META_INF_SERVICES_LOCATION);
        // retainAll performs the intersection of the factory names that we
        // are looking for the ones found, only the services found that match
        // the expected factory names will be retained
        if (null != services)
        {
            services.retainAll(resourceNames.keySet());
            Iterator itr = services.iterator();
            FactoryConfig config = new FactoryConfig();
            while (itr.hasNext())
            {
                String resourceName = (String) itr.next();
                InputStream is = context.getResourceAsStream(resourceName);
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
                try
                {
                    Class.forName(className);
                } catch (ClassNotFoundException e)
                {
                    throw new FacesException("Unable to find class "
                        + className, e);
                }
                config.setFactory((String) resourceNames.get(resourceName),
                    className);
            }
            facesConfig.setFactoryConfig(config);
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


    private void parseJarConfig(FacesConfig facesConfig,
                                ExternalContext context,
                                String jarPath)
        throws FacesException
    {
        try
        {
            if (!(context.getContext() instanceof ServletContext))
            {
                log.error("ServletContext expected");
            }
            ServletContext servletContext = (ServletContext) context.getContext();

            // not all containers expand archives, so we have to do it the generic way:
            // 1. get the stream from servlet context
            InputStream in = servletContext.getResourceAsStream(jarPath);
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

                JarFile jarFile = new JarFile(tmp);
                JarEntry configFile = jarFile.getJarEntry("META-INF/faces-config.xml");
                if (configFile != null)
                {
                    if (log.isDebugEnabled()) log.debug("faces-config.xml found in jar " + jarPath);
                    InputStream stream = jarFile.getInputStream(configFile);
                    String systemId = "jar:" + tmp.toURL() + "!/" + configFile.getName();
                    if (log.isInfoEnabled()) log.info("Reading config " + systemId);
                    parseStreamConfig(facesConfig, stream, systemId,
                        new FacesConfigEntityResolver(jarFile));
                }
                tmp.delete();
            } else
            {
                if (log.isDebugEnabled()) log.debug("Jar " + jarPath + " contains no faces-config.xml");
            }
        } catch (IOException e)
        {
            throw new FacesException(e);
        }
    }


    private void parseStreamConfig(FacesConfig facesConfig,
                                   InputStream stream,
                                   String baseURI,
                                   EntityResolver entityResolver)
        throws FacesException
    {
        try
        {
            parseFacesConfig(facesConfig, stream, baseURI, entityResolver);
        } catch (IOException e)
        {
            throw new FacesException(e);
        }
    }


}
