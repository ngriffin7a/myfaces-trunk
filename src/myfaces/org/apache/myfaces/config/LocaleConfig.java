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
public class LocaleConfig implements Config
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Log log               = LogFactory.getLog(LocaleConfig.class);

    //~ Instance fields ----------------------------------------------------------------------------

    private final Set _supportedLocales = new HashSet();
    private Locale    _defaultLocale;

    //~ Methods ------------------------------------------------------------------------------------

    public void setDefaultLocale(String name)
    {
        _defaultLocale = locale(name);
        _supportedLocales.add(_defaultLocale);
    }

    public Locale getDefaultLocale()
    {
        return _defaultLocale;
    }

    public Set getSupportedLocales()
    {
        return _supportedLocales;
    }

    public void addSupportedLocale(String name)
    {
        Locale locale = locale(name);
        if (locale != null)
        {
            _supportedLocales.add(locale);
        }
    }

    public void update(LocaleConfig localeConfig)
    {
        _supportedLocales.addAll(localeConfig._supportedLocales);
    }

    private Locale locale(String name)
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
                return new Locale(nameComponents[0]);

            case 2:
                return new Locale(nameComponents[0], nameComponents[1]);

            case 3:
                return new Locale(nameComponents[0], nameComponents[1], nameComponents[2]);

            default:
                log.error("Invalid default locale name, ignoring: " + name);
        }
        return null;
    }
}
