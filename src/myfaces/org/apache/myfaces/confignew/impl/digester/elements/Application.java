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

package net.sourceforge.myfaces.confignew.impl.digester.elements;

import java.util.List;
import java.util.ArrayList;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class Application
{

    private List actionListener = new ArrayList();
    private List defaultRenderkitId = new ArrayList();
    private List messageBundle = new ArrayList();
    private List navigationHandler = new ArrayList();
    private List viewHandler = new ArrayList();
    private List stateManager = new ArrayList();
    private List propertyResolver = new ArrayList();
    private List variableResolver = new ArrayList();
    private List localeConfig = new ArrayList();


    public void addActionListener(String listener)
    {
        actionListener.add(listener);
    }


    public void addDefaultRenderkitId(String id)
    {
        defaultRenderkitId.add(id);
    }


    public void addMessageBundle(String bundle)
    {
        messageBundle.add(bundle);
    }


    public void addNavigationHandler(String handler)
    {
        navigationHandler.add(handler);
    }


    public void addStateManager(String manager)
    {
        stateManager.add(manager);
    }


    public void addPropertyResolver(String resolver)
    {
        propertyResolver.add(resolver);
    }


    public void addVariableResolver(String handler)
    {
        variableResolver.add(handler);
    }


    public void addLocaleConfig(LocaleConfig config)
    {
        localeConfig.add(config);
    }


    public void addViewHandler(String handler)
    {
        viewHandler.add(handler);
    }


    public List getActionListener()
    {
        return actionListener;
    }


    public List getDefaultRenderkitId()
    {
        return defaultRenderkitId;
    }


    public List getMessageBundle()
    {
        return messageBundle;
    }


    public List getNavigationHandler()
    {
        return navigationHandler;
    }


    public List getViewHandler()
    {
        return viewHandler;
    }


    public List getStateManager()
    {
        return stateManager;
    }


    public List getPropertyResolver()
    {
        return propertyResolver;
    }


    public List getVariableResolver()
    {
        return variableResolver;
    }


    public List getLocaleConfig()
    {
        return localeConfig;
    }
}
