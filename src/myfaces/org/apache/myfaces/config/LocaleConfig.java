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

import net.sourceforge.myfaces.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LocaleConfig
    implements Config
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Log log      = LogFactory.getLog(LocaleConfig.class);

    //~ Instance fields ----------------------------------------------------------------------------

    private final Set _locales = new HashSet();

    //~ Methods ------------------------------------------------------------------------------------

    public void setDefaultLocale(String name)
    {
        if ((name == null) || (name.length() == 0))
        {
            log.error("Default locale name null or empty, ignoring");
        }

        char     separator      = (name.indexOf('_') >= 0) ? '_' : '-';

        String[] nameComponents = StringUtils.splitShortString(name, separator);

        switch (nameComponents.length)
        {
            case 1:
                _locales.add(new Locale(nameComponents[0]));
                break;

            case 2:
                _locales.add(new Locale(nameComponents[0], nameComponents[1]));
                break;

            case 3:
                _locales.add(new Locale(nameComponents[0], nameComponents[1], nameComponents[2]));
                break;

            default:
                log.error("Invalid default locale name, ignoring: " + name);
        }
    }

    public Set getDefaultLocales()
    {
        return _locales;
    }

    public void update(LocaleConfig localeConfig)
    {
        _locales.addAll(localeConfig._locales);
    }
}
