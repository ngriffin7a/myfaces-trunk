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
package net.sourceforge.myfaces.config;

import javax.faces.event.ActionListener;
import javax.faces.application.NavigationHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationConfig
    implements Config
{
    private ActionListener _actionListener;
    private NavigationHandler _navigationHandler;
    private PropertyResolver _propertyResolver;
    private VariableResolver _variableResolver;

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
}
