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

package net.sourceforge.myfaces.confignew;

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
        return delegate.getActionURL(context, path);
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
