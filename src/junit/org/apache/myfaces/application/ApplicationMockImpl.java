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
 * TODO: liven up this class
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ApplicationMockImpl
    extends Application
{
    public ActionListener getActionListener()
    {
        return null;
    }

    public void setActionListener(ActionListener actionlistener)
    {
    }

    public NavigationHandler getNavigationHandler()
    {
        return null;
    }

    public void setNavigationHandler(NavigationHandler navigationhandler)
    {
    }

    public PropertyResolver getPropertyResolver()
    {
        return null;
    }

    public void setPropertyResolver(PropertyResolver propertyresolver)
    {
    }

    public ValueBinding getValueBinding(String s) throws ReferenceSyntaxException
    {
        return null;
    }

    public VariableResolver getVariableResolver()
    {
        return null;
    }

    public void setVariableResolver(VariableResolver variableresolver)
    {
    }

    public void addComponent(String s, String s1)
    {
    }

    public UIComponent getComponent(String s) throws FacesException
    {
        return null;
    }

    public Iterator getComponentTypes()
    {
        return null;
    }

    public void addConverter(String s, String s1)
    {
    }

    public Converter getConverter(String s) throws FacesException
    {
        return null;
    }

    public Iterator getConverterIds()
    {
        return null;
    }

    public void addMessageResources(String s, String s1)
    {
    }

    public MessageResources getMessageResources(String s) throws FacesException
    {
        return null;
    }

    public Iterator getMessageResourcesIds()
    {
        return null;
    }

    public void addValidator(String s, String s1)
    {
    }

    public Validator getValidator(String s) throws FacesException
    {
        return null;
    }

    public Iterator getValidatorIds()
    {
        return null;
    }
}
