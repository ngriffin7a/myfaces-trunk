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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.el.PropertyResolverImpl;
import net.sourceforge.myfaces.el.ValueBindingImpl;
import net.sourceforge.myfaces.el.VariableResolverImpl;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.event.ActionListener;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;
import javax.faces.component.UIComponent;
import javax.faces.FacesException;
import javax.faces.validator.Validator;
import javax.faces.context.MessageResources;
import javax.faces.convert.Converter;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationMockImpl
    extends Application
{
    private ActionListener _actionListener = new ActionListenerImpl();
    private NavigationHandler _navigationHandler = new NavigationHandlerImpl();
    private PropertyResolver _propertyResolver = new PropertyResolverImpl();
    private VariableResolver _variableResolver = new VariableResolverImpl();

    public ActionListener getActionListener()
    {
        return _actionListener;
    }

    public void setActionListener(ActionListener actionListener)
    {
        _actionListener = actionListener;
    }

    public NavigationHandler getNavigationHandler()
    {
        return _navigationHandler;
    }

    public void setNavigationHandler(NavigationHandler navigationHandler)
    {
        _navigationHandler = navigationHandler;
    }

    public PropertyResolver getPropertyResolver()
    {
        return _propertyResolver;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver)
    {
        _propertyResolver = propertyResolver;
    }

    public ValueBinding getValueBinding(String s) throws ReferenceSyntaxException
    {
        return new ValueBindingImpl(s, this);
    }

    public VariableResolver getVariableResolver()
    {
        return _variableResolver;
    }

    public void setVariableResolver(VariableResolver variableResolver)
    {
        _variableResolver = variableResolver;
    }


    public void addComponent(String s, String s1)
    {
        throw new UnsupportedOperationException();
    }

    public UIComponent getComponent(String s) throws FacesException
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getComponentTypes()
    {
        throw new UnsupportedOperationException();
    }

    public void addConverter(String s, String s1)
    {
        throw new UnsupportedOperationException();
    }

    public Converter getConverter(String s) throws FacesException
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getConverterIds()
    {
        throw new UnsupportedOperationException();
    }

    public void addMessageResources(String s, String s1)
    {
        throw new UnsupportedOperationException();
    }

    public MessageResources getMessageResources(String s) throws FacesException
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getMessageResourcesIds()
    {
        throw new UnsupportedOperationException();
    }

    public void addValidator(String s, String s1)
    {
        throw new UnsupportedOperationException();
    }

    public Validator getValidator(String s) throws FacesException
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getValidatorIds()
    {
        throw new UnsupportedOperationException();
    }
}
