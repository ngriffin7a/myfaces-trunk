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
package javax.faces.application;

import javax.faces.FacesException;
import javax.faces.el.ReferenceSyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
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
