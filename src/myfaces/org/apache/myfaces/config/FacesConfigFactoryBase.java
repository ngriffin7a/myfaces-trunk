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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public abstract class FacesConfigFactoryBase
    implements FacesConfigFactory
{
    private static final Log log = LogFactory.getLog(FacesConfigFactoryBase.class);

    private static final String CONFIG_FILES_INIT_PARAM
        = "javax.faces.application.CONFIG_FILES";

    private static final String FACES_CONFIG_ATTR = FacesConfig.class.getName();


    public FacesConfig getFacesConfig(ExternalContext context)
    {
        Map appMap = context.getApplicationMap();
        FacesConfig facesConfig = (FacesConfig)appMap.get(FACES_CONFIG_ATTR);
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
        //Search through JAR files
        Set jars = context.getResourcePaths("/WEB-INF/lib/");
        if (jars != null)
        {
            for (Iterator it = jars.iterator(); it.hasNext(); )
            {
                String path = (String)it.next();
                if (path.toLowerCase().endsWith(".jar"))
                {
                    parseJarConfig(facesConfig, context, path);
                }
            }
        }

        String configFiles = context.getInitParameter(CONFIG_FILES_INIT_PARAM);
        if (configFiles == null)
        {
            String systemId = "/WEB-INF/faces-config.xml";
            InputStream stream = context.getResourceAsStream(systemId);
            if (stream != null)
            {
                log.info("Reading config /WEB-INF/faces-config.xml");
                parseStreamConfig(facesConfig, stream, systemId,
                                  new FacesConfigEntityResolver(context));
            }
        }
        else
        {
            StringTokenizer st = new StringTokenizer(configFiles, ",", false);
            while (st.hasMoreTokens())
            {
                String systemId = st.nextToken().trim();
                InputStream stream = context.getResourceAsStream(systemId);
                if (stream == null)
                {
                    throw new FacesException("Resource '" + systemId + "' not found!");
                }

                log.info("Reading config " + systemId);

                parseStreamConfig(facesConfig, stream, systemId,
                                  new FacesConfigEntityResolver(context));
            }
        }
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
            ServletContext servletContext = (ServletContext)context.getContext();
            URL url = servletContext.getResource(jarPath);
            if (url == null)
            {
                return;
            }

            JarFile jarFile = new JarFile(url.getPath());
            JarEntry configFile = jarFile.getJarEntry("META-INF/faces-config.xml");
            if (configFile != null) {
                String systemId = url + configFile.getName();
                log.info("Reading config " + systemId);
                InputStream stream = jarFile.getInputStream(configFile);
                parseStreamConfig(facesConfig, stream, systemId,
                        new FacesConfigEntityResolver(jarFile));
            }
        }
        catch (IOException e)
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
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }
    }





}
