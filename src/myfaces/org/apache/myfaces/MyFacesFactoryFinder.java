/**
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
package net.sourceforge.myfaces;

import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.FacesConfigFactoryImpl;
import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.18  2004/06/16 23:02:26  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.17.2.1  2004/06/16 02:07:24  o_rossmueller
 * get navigation rules from RuntimeConfig
 * refactored all remaining usages of MyFacesFactoryFinder to use RuntimeConfig
 *
 * Revision 1.17  2004/03/30 16:59:47  manolito
 * MessageFactory removed, MessageUtils moved to util in src/share
 *
 */
public class MyFacesFactoryFinder
{
    // TODO: this class is no longer used; remove???
    public static final String FACES_CONFIG_FACTORY = FacesConfigFactory.class.getName();

    private static final HashMap DEFAULT_FACTORIES = new HashMap();
    static
    {
        DEFAULT_FACTORIES.put(FACES_CONFIG_FACTORY, FacesConfigFactoryImpl.class.getName());
    }

    public static FacesConfigFactory getFacesConfigFactory(ExternalContext context)
        throws FacesException
    {
        return (FacesConfigFactory)getFactory(context, FACES_CONFIG_FACTORY);
    }

    private static Object getFactory(ExternalContext context, String factoryName)
            throws FacesException
    {
        Map appMap = context.getApplicationMap();
        Object fact = appMap.get(factoryName);
        if (fact == null)
        {
            String compFactClass = context.getInitParameter(factoryName);
            if (compFactClass == null)
            {
                compFactClass = (String)DEFAULT_FACTORIES.get(factoryName);
                if (compFactClass == null)
                {
                    throw new FacesException("Unknown factory " + factoryName);
                }
            }

            Class c = ClassUtils.classForName(compFactClass);

            try
            {
                fact = c.newInstance();
            }
            catch (InstantiationException e)
            {
                throw new FacesException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new FacesException(e);
            }

            appMap.put(factoryName, fact);
        }

        return fact;
    }

}

