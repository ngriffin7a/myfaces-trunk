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

/**
 * DOCUMENT ME!
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class Application
{
    //private static final Log log = LogFactory.getLog(Application.class);

    public abstract javax.faces.event.ActionListener getActionListener();

    public abstract void setActionListener(javax.faces.event.ActionListener listener);

    public abstract java.util.Locale getDefaultLocale();

    public abstract void setDefaultLocale(java.util.Locale locale);

    public abstract java.lang.String getDefaultRenderKitId();

    public abstract void setDefaultRenderKitId(java.lang.String renderKitId);

    public abstract java.lang.String getMessageBundle();

    public abstract void setMessageBundle(java.lang.String bundle);

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

    public abstract void addComponent(java.lang.String componentType,
                                      java.lang.String componentClass);

    public abstract javax.faces.component.UIComponent createComponent(java.lang.String componentType)
            throws FacesException;

    public abstract javax.faces.component.UIComponent createComponent(javax.faces.el.ValueBinding componentBinding,
                                                                      javax.faces.context.FacesContext context,
                                                                      java.lang.String componentType)
            throws FacesException;

    public abstract java.util.Iterator getComponentTypes();

    public abstract void addConverter(java.lang.String converterId,
                                      java.lang.String converterClass);

    public abstract void addConverter(java.lang.Class targetClass,
                                      java.lang.String converterClass);

    public abstract javax.faces.convert.Converter createConverter(java.lang.String converterId);

    public abstract javax.faces.convert.Converter createConverter(java.lang.Class targetClass);

    public abstract java.util.Iterator getConverterIds();

    public abstract java.util.Iterator getConverterTypes();

    public abstract javax.faces.el.MethodBinding createMethodBinding(java.lang.String ref,
                                                                     java.lang.Class[] params)
            throws ReferenceSyntaxException;

    public abstract java.util.Iterator getSupportedLocales();

    public abstract void setSupportedLocales(java.util.Collection locales);

    public abstract void addValidator(java.lang.String validatorId,
                                      java.lang.String validatorClass);

    public abstract javax.faces.validator.Validator createValidator(java.lang.String validatorId)
            throws FacesException;

    public abstract java.util.Iterator getValidatorIds();

    public abstract javax.faces.el.ValueBinding createValueBinding(java.lang.String ref)
            throws ReferenceSyntaxException;
}
