/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package org.apache.myfaces.config;

import java.util.Locale;
import java.io.IOException;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class TestViewHandlerA extends ViewHandler
{

    private ViewHandler delegate;


    public TestViewHandlerA(ViewHandler delegate)
    {
        this.delegate = delegate;
    }


    public ViewHandler getDelegate()
    {
        return delegate;
    }


    public Locale calculateLocale(FacesContext context)
    {
        return delegate.calculateLocale(context);
    }


    public String calculateRenderKitId(FacesContext context)
    {
        return delegate.calculateRenderKitId(context);
    }


    public UIViewRoot createView(FacesContext context, String viewId)
    {
        return delegate.createView(context, viewId);
    }


    public String getActionURL(FacesContext context, String viewId)
    {
        return delegate.getActionURL(context, viewId);
    }


    public String getResourceURL(FacesContext context, String path)
    {
        return delegate.getResourceURL(context, path);
    }


    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException
    {
        delegate.renderView(context, viewToRender);
    }


    public UIViewRoot restoreView(FacesContext context, String viewId)
    {
        return delegate.restoreView(context, viewId);
    }


    public void writeState(FacesContext context) throws IOException
    {
        delegate.writeState(context);
    }

}
