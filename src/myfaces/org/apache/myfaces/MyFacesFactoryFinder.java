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
 * Revision 1.19  2004/07/01 22:05:13  mwessendorf
 * ASF switch
 *
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

