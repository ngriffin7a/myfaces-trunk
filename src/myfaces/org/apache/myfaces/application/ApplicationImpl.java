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

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.el.ValueBindingImpl;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.MessageResources;
import javax.faces.convert.Converter;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ApplicationImpl
    extends Application
{
    private ServletContext _servletContext;
    private FacesConfig _facesConfig;
    private Map _valueBindingMap = new HashMap();

    public ApplicationImpl(ServletContext servletContext)
    {
        _servletContext = servletContext;
        _facesConfig = null;
    }

    protected FacesConfig getFacesConfig()
    {
        if (_facesConfig == null)
        {
            FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(_servletContext);
            _facesConfig = fcf.getFacesConfig(_servletContext);
        }
        return _facesConfig;
    }

    public ActionListener getActionListener()
    {
        return getFacesConfig().getApplicationConfig().getActionListener();
    }

    public void setActionListener(ActionListener actionListener)
    {
        getFacesConfig().getApplicationConfig().setActionListener(actionListener);
    }

    public NavigationHandler getNavigationHandler()
    {
        return getFacesConfig().getApplicationConfig().getNavigationHandler();
    }

    public void setNavigationHandler(NavigationHandler navigationHandler)
    {
        getFacesConfig().getApplicationConfig().setNavigationHandler(navigationHandler);
    }

    public PropertyResolver getPropertyResolver()
    {
        return getFacesConfig().getApplicationConfig().getPropertyResolver();
    }

    public void setPropertyResolver(PropertyResolver propertyResolver)
    {
        getFacesConfig().getApplicationConfig().setPropertyResolver(propertyResolver);
    }

    public VariableResolver getVariableResolver()
    {
        return getFacesConfig().getApplicationConfig().getVariableResolver();
    }

    public void setVariableResolver(VariableResolver variableResolver)
    {
        getFacesConfig().getApplicationConfig().setVariableResolver(variableResolver);
    }

    public ValueBinding getValueBinding(String ref) throws ReferenceSyntaxException
    {
        ValueBinding vb = (ValueBinding)_valueBindingMap.get(ref);
        if (vb == null)
        {
        	// Note: we cannot cache VariableResolver and PropertyResolve directly in ValueBinding since those can change through the set methods
            vb = new ValueBindingImpl(ref, this);
            _valueBindingMap.put(ref, vb);
        }
        return vb;
    }

    public void addConverter(String converterId, String converterClass)
    {
        getFacesConfig().addConverter(converterId, converterClass);
    }

    public Converter getConverter(String converterId) throws FacesException
    {
        return getFacesConfig().getConverter(converterId);
    }

    public Iterator getConverterIds()
    {
        return getFacesConfig().getConverterIds();
    }


    public void addComponent(String componentType, String componentClass)
    {
        getFacesConfig().addComponent(componentType, componentClass);
    }

    public UIComponent getComponent(String componentType) throws FacesException
    {
        return getFacesConfig().getComponent(componentType);
    }

    public Iterator getComponentTypes()
    {
        return getFacesConfig().getComponentTypes();
    }




    public void addMessageResources(String id, String messageResourcesClass)
    {
        getFacesConfig().addMessageResources(id, messageResourcesClass);
    }

    public MessageResources getMessageResources(String id) throws FacesException
    {
        return getFacesConfig().getMessageResources(id);
    }

    public Iterator getMessageResourcesIds()
    {
        return getFacesConfig().getMessageResourcesIds();
    }


    public void addValidator(String validatorId, String validatorClass)
    {
        getFacesConfig().addValidator(validatorId, validatorClass);
    }

    public Validator getValidator(String validatorId) throws FacesException
    {
        return getFacesConfig().getValidator(validatorId);
    }

    public Iterator getValidatorIds()
    {
        return getFacesConfig().getValidatorIds();
    }
}
