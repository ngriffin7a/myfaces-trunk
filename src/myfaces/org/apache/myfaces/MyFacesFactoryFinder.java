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
package net.sourceforge.myfaces;

import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.FacesConfigFactoryImpl;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;
import net.sourceforge.myfaces.webapp.ServletMappingFactoryImpl;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import java.util.HashMap;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesFactoryFinder
{
    public static final String SERVLET_MAPPING_FACTORY = ServletMapping.class.getName();
    public static final String FACES_CONFIG_FACTORY = FacesConfigFactory.class.getName();

    private static final HashMap DEFAULT_FACTORIES = new HashMap();
    static
    {
        DEFAULT_FACTORIES.put(SERVLET_MAPPING_FACTORY, ServletMappingFactoryImpl.class.getName());
        DEFAULT_FACTORIES.put(FACES_CONFIG_FACTORY, FacesConfigFactoryImpl.class.getName());
    }

    private static Object getFactory(ExternalContext context, String factoryName)
            throws FacesException
    {
        ServletContext servletContext = (ServletContext)context;
        Object fact = servletContext.getAttribute(factoryName);
        if (fact == null)
        {
            String compFactClass = servletContext.getInitParameter(factoryName);
            if (compFactClass == null)
            {
                compFactClass = (String)DEFAULT_FACTORIES.get(factoryName);
                if (compFactClass == null)
                {
                    throw new FacesException("Unknown factory " + factoryName);
                }
            }

            try
            {
                Class c = Class.forName(compFactClass, true, Thread.currentThread().getContextClassLoader());
                fact = c.newInstance();
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

            servletContext.setAttribute(factoryName, fact);
        }

        return fact;
    }


    public static ServletMappingFactory getServletMappingFactory(ExternalContext context)
        throws FacesException
    {
        return (ServletMappingFactory)getFactory(context, SERVLET_MAPPING_FACTORY);
    }

    public static FacesConfigFactory getFacesConfigFactory(ExternalContext context)
        throws FacesException
    {
        return (FacesConfigFactory)getFactory(context, FACES_CONFIG_FACTORY);
    }

}
