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
package net.sourceforge.myfaces.config.configure;

import net.sourceforge.myfaces.config.ApplicationConfig;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.LocaleConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Applies a configuration to a given Application instance.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationConfigurator
{
    private static final Log log = LogFactory.getLog(ApplicationConfigurator.class);

    private FacesConfig _facesConfig;

    public ApplicationConfigurator(FacesConfig facesConfig)
    {
        _facesConfig = facesConfig;
    }

    public void configure(Application a)
    {
        if (log.isDebugEnabled()) log.debug("Configuring Application");

        ApplicationConfig appConfig = _facesConfig.getApplicationConfig();

        NavigationHandler navigationHandler = appConfig.getNavigationHandler();
        if (navigationHandler != null)
        {
            a.setNavigationHandler(navigationHandler);
        }

        PropertyResolver propertyResolver = appConfig.getPropertyResolver();
        if (propertyResolver != null)
        {
            a.setPropertyResolver(propertyResolver);
        }

        VariableResolver variableResolver = appConfig.getVariableResolver();
        if (variableResolver != null)
        {
            a.setVariableResolver(variableResolver);
        }

        ViewHandler viewHandler = appConfig.getViewHandler();
        if (viewHandler != null)
        {
            a.setViewHandler(viewHandler);
        }

        ActionListener actionListener = appConfig.getActionListener();
        if (actionListener != null)
        {
            a.setActionListener(actionListener);
        }

        String messageBundle = appConfig.getMessageBundle();
        if (messageBundle != null)
        {
            a.setMessageBundle(messageBundle);
        }

        LocaleConfig localeConfig = appConfig.getLocaleConfig();
        if (localeConfig != null)
        {
            if (localeConfig.getDefaultLocale() != null)
            {
                a.setDefaultLocale(localeConfig.getDefaultLocale());
            }
            else
            {
                if (localeConfig.getSupportedLocales() != null &&
                    !localeConfig.getSupportedLocales().isEmpty())
                {
                    Locale first = (Locale)localeConfig.getSupportedLocales().iterator().next();
                    log.warn("No default locale defined. Using first supported locale " + first);
                    a.setDefaultLocale(first);
                }
                else
                {
                    log.error("No default locale and no supported locales defined. Using java default locale.");
                    a.setDefaultLocale(Locale.getDefault());
                }
            }

            if (localeConfig.getSupportedLocales() != null &&
                !localeConfig.getSupportedLocales().isEmpty())
            {
                a.setSupportedLocales(localeConfig.getSupportedLocales());
            }
            else
            {
                log.warn("No supported locales defined.");
                a.setSupportedLocales(Collections.EMPTY_SET);
            }
        }

        for (Iterator it = _facesConfig.getConverterMap().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String converterId = (String) entry.getKey();
            String converterClass = (String) entry.getValue();
            a.addConverter(converterId, converterClass);
        }

        for (Iterator it = _facesConfig.getConverterTypeMap().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            Class targetClass = (Class) entry.getKey();
            String converterClass = (String) entry.getValue();
            a.addConverter(targetClass, converterClass);
        }

        for (Iterator it = _facesConfig.getComponentClassMap().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String componentType = (String) entry.getKey();
            String componentClass = (String) entry.getValue();
            a.addComponent(componentType, componentClass);
        }

        for (Iterator it = _facesConfig.getValidatorClassMap().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String validatorId = (String) entry.getKey();
            String validatorClass = (String) entry.getValue();
            a.addValidator(validatorId, validatorClass);
        }

    }
}
