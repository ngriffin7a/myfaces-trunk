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
package net.sourceforge.myfaces.util.bundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class BundleUtils
{
    private static final Log log = LogFactory.getLog(BundleUtils.class);

    private BundleUtils() {}

    public static ResourceBundle findResourceBundle(FacesContext facesContext,
                                                    String bundleName)
    {
        //TODO: Could be JSTL LocalizationContext bundle?

        //Lookup as attribute (try different scopes)
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        VariableResolver vr = af.getApplication().getVariableResolver();
        ResourceBundle bundle = (ResourceBundle)vr.resolveVariable(facesContext, bundleName);

        return bundle;
    }

    public static String getString(FacesContext facesContext,
                                   String bundleName, String key)
    {
        ResourceBundle bundle = findResourceBundle(facesContext, bundleName);
        if (bundle != null)
        {
            try
            {
                return bundle.getString(key);
            }
            catch (MissingResourceException e)
            {
                log.warn("Resource string '" + key + "' in bundle '" + bundleName + "' could not be found.");
                return key;
            }
        }
        else
        {
            return key;
        }
    }

}
