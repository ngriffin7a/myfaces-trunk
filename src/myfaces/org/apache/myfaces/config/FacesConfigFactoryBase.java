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
package net.sourceforge.myfaces.config;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class FacesConfigFactoryBase
    implements FacesConfigFactory
{
    private static final String CONFIG_FILES_INIT_PARAM
        = "javax.faces.application.CONFIG_FILES";

    private static final String FACES_CONFIG_ATTR = FacesConfig.class.getName();


    public FacesConfig getFacesConfig(ServletContext servletContext)
    {
        FacesConfig facesConfig = (FacesConfig)servletContext.getAttribute(FACES_CONFIG_ATTR);
        if (facesConfig != null)
        {
            return facesConfig;
        }

        facesConfig = new FacesConfig();
        parseFacesConfigFiles(facesConfig, servletContext);

        servletContext.setAttribute(FACES_CONFIG_ATTR, facesConfig);
        return facesConfig;
    }

    protected abstract void parseFacesConfig(FacesConfig facesConfig,
                                             InputStream in) throws IOException, FacesException;


    private void parseFacesConfigFiles(FacesConfig facesConfig, ServletContext servletContext)
        throws FacesException
    {
        //Search through JAR files
        Set jars = servletContext.getResourcePaths("/WEB-INF/lib/");
        if (jars != null)
        {
            for (Iterator it = jars.iterator(); it.hasNext(); )
            {
                String path = (String)it.next();
                if (path.endsWith(".jar"))  //TODO: What about ucase extensions?
                {
                    parseJarConfig(facesConfig, servletContext, path);
                }
            }
        }

        String configFiles = servletContext.getInitParameter(CONFIG_FILES_INIT_PARAM);
        if (configFiles == null)
        {
            InputStream stream = servletContext.getResourceAsStream("/WEB-INF/faces-config.xml");
            if (stream != null)
            {
                parseStreamConfig(facesConfig, stream);
            }
        }
        else
        {
            StringTokenizer st = new StringTokenizer(configFiles, ",", false);
            while (st.hasMoreTokens())
            {
                String t = st.nextToken();
                InputStream stream = servletContext.getResourceAsStream("/WEB-INF/faces-config.xml");
                if (stream == null)
                {
                    throw new FacesException("Resource '" + t + "' not found!");
                }
                parseStreamConfig(facesConfig, stream);
            }
        }
    }


    private void parseJarConfig(FacesConfig facesConfig,
                                  ServletContext servletContext,
                                  String jarPath)
        throws FacesException
    {
        try
        {
            URL url = servletContext.getResource(jarPath);
            if (url == null)
            {
                return;
            }

            url = new URL("jar:" + url.toString() + "!/");
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            JarFile jarFile = conn.getJarFile();
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements())
            {
                JarEntry entry = (JarEntry) entries.nextElement();
                String name = entry.getName();
                if (!name.equals("META-INF/faces-config.xml"))
                {
                    continue;
                }
                InputStream stream = jarFile.getInputStream(entry);
                parseStreamConfig(facesConfig, stream);
            }
        }
        catch (java.io.IOException e)
        {
            throw new FacesException(e);
        }
    }


    private void parseStreamConfig(FacesConfig facesConfig,
                                     InputStream stream)
        throws FacesException
    {
        try
        {
            parseFacesConfig(facesConfig, stream);
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }
    }


}
