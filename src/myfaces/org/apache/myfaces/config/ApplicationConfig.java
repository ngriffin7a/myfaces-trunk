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

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
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
    
    public LocaleConfig getLocaleConfig(LocaleConfig localeConfig) 
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
