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

import net.sourceforge.myfaces.renderkit.html.jspinfo.TLDInfo;
import net.sourceforge.myfaces.util.logging.LogUtil;
import org.xml.sax.EntityResolver;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
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
    protected static final String TLD_HTML_URI = "http://java.sun.com/jsf/html";
    protected static final String TLD_EXT_URI = "http://myfaces.sourceforge.net/tld/myfaces_ext_0_4.tld";

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

        completeRendererComponentClasses(facesConfig);

        completeRendererAttributesByTLD(servletContext, facesConfig, TLD_HTML_URI);
        completeRendererAttributesByTLD(servletContext, facesConfig, TLD_EXT_URI);

        servletContext.setAttribute(FACES_CONFIG_ATTR, facesConfig);
        return facesConfig;
    }

    protected abstract void parseFacesConfig(FacesConfig facesConfig,
                                             InputStream in,
                                             String systemId,
                                             EntityResolver entityResolver) throws IOException, FacesException;


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
                if (path.toLowerCase().endsWith(".jar"))
                {
                    parseJarConfig(facesConfig, servletContext, path);
                }
            }
        }

        String configFiles = servletContext.getInitParameter(CONFIG_FILES_INIT_PARAM);
        if (configFiles == null)
        {
            String systemId = "/WEB-INF/faces-config.xml";
            InputStream stream = servletContext.getResourceAsStream(systemId);
            if (stream != null)
            {
                LogUtil.getLogger().info("Reading config /WEB-INF/faces-config.xml");
                parseStreamConfig(facesConfig, stream, systemId,
                                  new FacesConfigEntityResolver(servletContext));
            }
        }
        else
        {
            StringTokenizer st = new StringTokenizer(configFiles, ",", false);
            while (st.hasMoreTokens())
            {
                String systemId = st.nextToken().trim();
                InputStream stream = servletContext.getResourceAsStream(systemId);
                if (stream == null)
                {
                    throw new FacesException("Resource '" + systemId + "' not found!");
                }

                LogUtil.getLogger().info("Reading config " + systemId);

                parseStreamConfig(facesConfig, stream, systemId,
                                  new FacesConfigEntityResolver(servletContext));
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
                if (name.equals("META-INF/faces-config.xml"))
                {
                    String systemId = url + name;
                    LogUtil.getLogger().info("Reading config " + systemId);
                    InputStream stream = jarFile.getInputStream(entry);
                    parseStreamConfig(facesConfig, stream, systemId,
                                      new FacesConfigEntityResolver(jarFile));
                }
            }
        }
        catch (java.io.IOException e)
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



    /**
     * Adds for each renderer's componentType the corresponding componentClass
     * if not already defined.
     */
    protected void completeRendererComponentClasses(FacesConfig facesConfig)
    {
        for (Iterator rkIt = facesConfig.getRenderKitConfigs(); rkIt.hasNext(); )
        {
            RenderKitConfig rkc = (RenderKitConfig)rkIt.next();
            for (Iterator rendIt = rkc.getRendererConfigs(); rendIt.hasNext(); )
            {
                RendererConfig rc = (RendererConfig)rendIt.next();
                for (Iterator ctIt = rc.getComponentTypes(); ctIt.hasNext(); )
                {
                    String compType = (String)ctIt.next();
                    UIComponent comp = facesConfig.getComponent(compType);
                    if (!rc.supportsComponentClass(comp.getClass()))
                    {
                        rc.addComponentClass(comp.getClass().getName());
                    }
                }
            }
        }

    }


    /**
     * Reads additional renderer attribute information from the given
     * Taglib descriptor.
     */
    protected void completeRendererAttributesByTLD(ServletContext servletContext,
                                                   FacesConfig facesConfig,
                                                   String taglibURI)
    {
        TagLibraryInfo tagLibraryInfo = TLDInfo.getTagLibraryInfo(servletContext,
                                                                  taglibURI);
        TagInfo[] tagInfos = tagLibraryInfo.getTags();
        for (int i = 0; i < tagInfos.length; i++)
        {
            TagInfo tagInfo = tagInfos[i];
            completeRendererAttributesByTagInfo(facesConfig, tagInfo);
        }
    }

    private void completeRendererAttributesByTagInfo(FacesConfig facesConfig,
                                                     TagInfo tagInfo)
    {
        Tag tag = null;
        try
        {
            Class tagClass = Class.forName(tagInfo.getTagClassName(), true, Thread.currentThread().getContextClassLoader());
            tag = (Tag)tagClass.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new FacesException(e);
        }
        catch (InstantiationException e)
        {
            throw new FacesException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new FacesException(e);
        }

        if (!(tag instanceof UIComponentTag))
        {
            return;
        }
        String rendererType = ((UIComponentTag)tag).getRendererType();
        TagAttributeInfo[] tagAttributeInfos = tagInfo.getAttributes();

        for (Iterator rkIt = facesConfig.getRenderKitConfigs(); rkIt.hasNext();)
        {
            RenderKitConfig rkc = (RenderKitConfig)rkIt.next();
            RendererConfig rc = rkc.getRendererConfig(rendererType);
            if (rc != null)
            {
                for (int i = 0; i < tagAttributeInfos.length; i++)
                {
                    TagAttributeInfo tagAttributeInfo = tagAttributeInfos[i];
                    addRendererAttribute(rc, tagAttributeInfo);
                }
            }
        }
    }

    private void addRendererAttribute(RendererConfig rendererConfig,
                                      TagAttributeInfo tagAttributeInfo)
    {
        String name = tagAttributeInfo.getName();
        if (name.equals("id"))
        {
            return;
        }

        String className = tagAttributeInfo.getTypeName();
        if (className == null)
        {
            className = String.class.getName(); //TODO: or Object?
        }

        AttributeConfig attributeConfig = rendererConfig.getAttributeConfig(name);
        if (attributeConfig == null)
        {
            attributeConfig = new AttributeConfig();
            attributeConfig.setAttributeName(name);
            attributeConfig.setAttributeClass(className);
            rendererConfig.addAttributeConfig(attributeConfig);
//System.out.println("Added renderer attribute '" + name + "' for Renderer '" + rendererConfig.getRendererType() + "'.");
        }
        else
        {
            String attrClass = attributeConfig.getAttributeClass();
            if (attrClass == null)
            {
                attributeConfig.setAttributeClass(className);
            }
            else if (!attrClass.equals(className))
            {
                LogUtil.getLogger().warning("Error in faces-config.xml - inconsistency with TLD: Attribute '" + name + "' of renderer '" +  rendererConfig.getRendererType() + "' has different class in Taglib descriptor.");
            }
        }
    }

}
