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
package net.sourceforge.myfaces.config.configure;

import net.sourceforge.myfaces.config.AttributeConfig;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.RenderKitConfig;
import net.sourceforge.myfaces.config.RendererConfig;
import net.sourceforge.myfaces.renderkit.html.jspinfo.TLDInfo;
import net.sourceforge.myfaces.util.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RenderKitFactoryConfigurator
{
    private static final Log log = LogFactory.getLog(RenderKitFactoryConfigurator.class);

    protected static final String TLD_HTML_URI = "http://java.sun.com/jsf/html";
    protected static final String TLD_EXT_URI = "http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld";


    private FacesConfig _facesConfig;

    public RenderKitFactoryConfigurator(FacesConfig facesConfig)
    {
        _facesConfig = facesConfig;
    }

    public void configure(RenderKitFactory rkf, ExternalContext externalContext)
    {
        if (log.isDebugEnabled()) log.debug("Completing RenderKitFactory");

        completeRendererComponentClasses();

        completeRendererAttributesByTLD(externalContext, TLD_HTML_URI);
//        completeRendererAttributesByTLD(externalContext, TLD_EXT_URI);


        if (log.isDebugEnabled()) log.debug("Configuring RenderKitFactory");

        for (Iterator it = _facesConfig.getRenderKitIds(); it.hasNext(); )
        {
            String id = (String)it.next();
            RenderKitConfig rkc = _facesConfig.getRenderKitConfig(id);
            RenderKit rk;
            if (renderKitFactoryContains(rkf, id))
            {
                rk = rkf.getRenderKit(null, id);    //FIXME: We need a facesContext!
            }
            else
            {
                rk = rkc.newRenderKit();
                rkf.addRenderKit(id, rk);
            }
            new RenderKitConfigurator(rkc).configure(rk, id);
        }
    }


    /**
     * Helper for {@link #configure}.
     */
    private static boolean renderKitFactoryContains(RenderKitFactory rkf, String renderKitId)
    {
        for (Iterator it = rkf.getRenderKitIds(); it.hasNext(); )
        {
            if (it.next().equals(renderKitId))
            {
                return true;
            }
        }
        return false;
    }




    /**
     * Adds for each renderer's componentType the corresponding componentClass
     * if not already defined.
     */
    private void completeRendererComponentClasses()
    {
        for (Iterator rkIt = _facesConfig.getRenderKitConfigs(); rkIt.hasNext(); )
        {
            RenderKitConfig rkc = (RenderKitConfig)rkIt.next();
            for (Iterator rendIt = rkc.getRendererConfigs(); rendIt.hasNext(); )
            {
                RendererConfig rc = (RendererConfig)rendIt.next();
                for (Iterator ctIt = rc.getSupportedComponentTypes(); ctIt.hasNext(); )
                {
                    String compType = (String)ctIt.next();
                    String className = (String)_facesConfig.getComponentClassMap().get(compType);
                    if (className == null)
                    {
                        log.error("Undefined component type " + compType + ", cannot complete renderer config " + rc.getRendererType());
                    }
                    else
                    {
                        Class clazz = ClassUtils.classForName(className);
                        if (!rc.supportsComponentClass(clazz))
                        {
                            rc.addSupportedComponentClass(clazz);
                        }
                    }
                }
            }
        }

    }


    /**
     * Reads additional renderer attribute information from the given
     * Taglib descriptor.
     */
    protected void completeRendererAttributesByTLD(ExternalContext context,
                                                   String taglibURI)
    {
        TagLibraryInfo tagLibraryInfo = TLDInfo.getTagLibraryInfo(context,
                                                                  taglibURI);
        TagInfo[] tagInfos = tagLibraryInfo.getTags();
        for (int i = 0; i < tagInfos.length; i++)
        {
            TagInfo tagInfo = tagInfos[i];
            completeRendererAttributesByTagInfo(tagInfo);
        }
    }

    private void completeRendererAttributesByTagInfo(TagInfo tagInfo)
    {
        Tag tag = (Tag) ClassUtils.newInstance(tagInfo.getTagClassName());
        if (!(tag instanceof UIComponentTag))
        {
            return;
        }

        String rendererType = ((UIComponentTag)tag).getRendererType();
        TagAttributeInfo[] tagAttributeInfos = tagInfo.getAttributes();

        for (Iterator rkIt = _facesConfig.getRenderKitConfigs(); rkIt.hasNext();)
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
            String attributeClassName = attributeConfig.getAttributeClass();
            if (attributeClassName == null)
            {
                attributeConfig.setAttributeClass(className);
            }
            else if (!attributeClassName.equals(className))
            {
                log.warn("Error in faces-config.xml - inconsistency with TLD: Attribute '" + name + "' of renderer '" +  rendererConfig.getRendererType() + "' has different class in Taglib descriptor.");
            }
        }
    }



}
