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
package javax.faces.application;

import javax.faces.FacesException;
import javax.faces.el.ReferenceSyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * DOCUMENT ME!
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class Application
{
    public abstract javax.faces.event.ActionListener getActionListener();

    public abstract void setActionListener(javax.faces.event.ActionListener listener);

    public abstract Locale getDefaultLocale();

    public abstract void setDefaultLocale(Locale locale);

    public abstract String getDefaultRenderKitId();

    public abstract void setDefaultRenderKitId(String renderKitId);

    public abstract String getMessageBundle();

    public abstract void setMessageBundle(String bundle);

    public abstract javax.faces.application.NavigationHandler getNavigationHandler();

    public abstract void setNavigationHandler(javax.faces.application.NavigationHandler handler);

    public abstract javax.faces.el.PropertyResolver getPropertyResolver();

    public abstract void setPropertyResolver(javax.faces.el.PropertyResolver resolver);

    public abstract javax.faces.el.VariableResolver getVariableResolver();

    public abstract void setVariableResolver(javax.faces.el.VariableResolver resolver);

    public abstract javax.faces.application.ViewHandler getViewHandler();

    public abstract void setViewHandler(javax.faces.application.ViewHandler handler);

    public abstract javax.faces.application.StateManager getStateManager();

    public abstract void setStateManager(javax.faces.application.StateManager manager);

    public abstract void addComponent(String componentType,
                                      String componentClass);

    public abstract javax.faces.component.UIComponent createComponent(String componentType)
            throws FacesException;

    public abstract javax.faces.component.UIComponent createComponent(javax.faces.el.ValueBinding componentBinding,
                                                                      javax.faces.context.FacesContext context,
                                                                      String componentType)
            throws FacesException;

    public abstract Iterator getComponentTypes();

    public abstract void addConverter(String converterId,
                                      String converterClass);

    public abstract void addConverter(Class targetClass,
                                      String converterClass);

    public abstract javax.faces.convert.Converter createConverter(String converterId);

    public abstract javax.faces.convert.Converter createConverter(Class targetClass);

    public abstract Iterator getConverterIds();

    public abstract Iterator getConverterTypes();

    public abstract javax.faces.el.MethodBinding createMethodBinding(String ref,
                                                                     Class[] params)
            throws ReferenceSyntaxException;

    public abstract Iterator getSupportedLocales();

    public abstract void setSupportedLocales(Collection locales);

    public abstract void addValidator(String validatorId,
                                      String validatorClass);

    public abstract javax.faces.validator.Validator createValidator(String validatorId)
            throws FacesException;

    public abstract Iterator getValidatorIds();

    public abstract javax.faces.el.ValueBinding createValueBinding(String ref)
            throws ReferenceSyntaxException;
}
