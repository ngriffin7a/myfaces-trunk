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

import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.10  2004/07/01 22:05:08  mwessendorf
 * ASF switch
 *
 * Revision 1.9  2004/05/03 09:25:05  manolito
 * Log
 *
 */
public class ApplicationConfig
    implements Config
{
    private ActionListener _actionListener;
    private String _messageBundle;
    private NavigationHandler _navigationHandler;
    private ViewHandler _viewHandler;
    private PropertyResolver _propertyResolver;
    private VariableResolver _variableResolver;
    private LocaleConfig _localeConfig;

    public ApplicationConfig() {
        _viewHandler = new JspViewHandlerImpl();
    }

    public void setActionListener(ActionListener actionListener)
    {
        _actionListener = actionListener;
    }

    public ActionListener getActionListener()
    {
        return _actionListener;
    }

    public void setVariableResolver(VariableResolver variableResolver)
    {
        _variableResolver = variableResolver;
    }

    public VariableResolver getVariableResolver()
    {
        return _variableResolver;
    }

    public void setNavigationHandler(NavigationHandler navigationHandler)
    {
        _navigationHandler = navigationHandler;
    }

    public NavigationHandler getNavigationHandler()
    {
        return _navigationHandler;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver)
    {
        _propertyResolver = propertyResolver;
    }

    public PropertyResolver getPropertyResolver()
    {
        return _propertyResolver;
    }

    public String getMessageBundle()
    {
        return _messageBundle;
    }

    public void setMessageBundle(String messageBundle)
    {
        _messageBundle = messageBundle.intern();
    }

    public LocaleConfig getLocaleConfig()
    {
        return _localeConfig;
    }

    public void setLocaleConfig(LocaleConfig localeConfig) {
        _localeConfig = localeConfig;
    }

    public void addLocaleConfig(LocaleConfig localeConfig) {
        if (_localeConfig == null)
        {
            _localeConfig = localeConfig;
        }
        else
        {
            _localeConfig.update(localeConfig);
        }
    }

    public ViewHandler getViewHandler()
    {
        return _viewHandler;
    }

    public void setViewHandler(ViewHandler viewHandler)
    {
        _viewHandler = viewHandler;
    }

    public void update(ApplicationConfig applicationConfig)
    {
        if (applicationConfig._actionListener != null)
        {
            _actionListener = applicationConfig._actionListener;
        }

        if (applicationConfig._localeConfig != null)
        {
            _localeConfig = applicationConfig._localeConfig;
        }

        if (applicationConfig._messageBundle != null)
        {
            _messageBundle = applicationConfig._messageBundle;
        }

        if (applicationConfig._navigationHandler != null)
        {
            _navigationHandler = applicationConfig._navigationHandler;
        }

        if (applicationConfig._propertyResolver != null)
        {
            _propertyResolver = applicationConfig._propertyResolver;
        }

        if (applicationConfig._variableResolver != null)
        {
            _variableResolver = applicationConfig._variableResolver;
        }

        if (applicationConfig._viewHandler != null)
        {
            _viewHandler = applicationConfig._viewHandler;
        }
    }
}
