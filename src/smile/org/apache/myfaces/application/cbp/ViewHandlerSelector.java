/**
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
package net.sourceforge.myfaces.application.cbp;

import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

/**
 * This class selects the ViewHandler to use, on a per-request basis. Basically
 * it looks if the viewId of the page maps onto a CBP page, or not. If it does
 * it does, the ViewHandler from Smile will be used.
 * 
 * @author Dimitry D'hondt.
 * @author Anton Koinov
 */
public class ViewHandlerSelector extends ViewHandler
{
    private ViewHandler jspHandler = null;
    private ViewHandler cbpHandler = null;

    //	private StateManager stateManager = null;

    /**
     * Default constructor
     */
    public ViewHandlerSelector()
    {
        jspHandler = new JspViewHandlerImpl();
        cbpHandler = new net.sourceforge.myfaces.application.cbp.CbpViewHandlerImpl();
        //		stateManager = new CbpStateManagerImpl();
    }

    /**
     * @see javax.faces.application.ViewHandler#renderView(javax.faces.context.FacesContext,
     *      javax.faces.component.UIViewRoot)
     */
    public void renderView(FacesContext ctx, UIViewRoot viewRoot)
        throws IOException, FacesException
    {
        if (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isPageJSP(ctx, viewRoot))
        {
            cbpHandler.renderView(ctx, viewRoot);
        }
        else
        {
            jspHandler.renderView(ctx, viewRoot);
        }
    }

    /**
     * @see javax.faces.application.ViewHandler#restoreView(javax.faces.context.FacesContext,
     *      java.lang.String)
     */
    public UIViewRoot restoreView(FacesContext ctx, String viewId)
    {
        return (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isPageJSP(ctx, viewId))
            ? cbpHandler.restoreView(ctx, viewId)
            : jspHandler.restoreView(ctx, viewId);
    }

    /**
     * @see javax.faces.application.ViewHandler#createView(javax.faces.context.FacesContext,
     *      java.lang.String)
     */
    public UIViewRoot createView(FacesContext ctx, String viewId)
    {
        return (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isPageJSP(ctx, viewId))
            ? cbpHandler.createView(ctx, viewId)
            : jspHandler.createView(ctx, viewId);
    }

    /**
     * @see javax.faces.application.ViewHandler#writeState(javax.faces.context.FacesContext)
     */
    public void writeState(FacesContext ctx)
        throws IOException
    {
        if (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isCurrentPageJSP(ctx))
        {
            cbpHandler.writeState(ctx);
        }
        else
        {
            jspHandler.writeState(ctx);
        }
    }

    /**
     * @see javax.faces.application.ViewHandler#calculateLocale(javax.faces.context.FacesContext)
     */
    public Locale calculateLocale(FacesContext ctx)
    {
        return (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isCurrentPageJSP(ctx))
            ? cbpHandler.calculateLocale(ctx)
            : jspHandler.calculateLocale(ctx);
    }

    /**
     * @see javax.faces.application.ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)
     */
    public String calculateRenderKitId(FacesContext ctx)
    {
        return (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isCurrentPageJSP(ctx))
            ?  cbpHandler.calculateRenderKitId(ctx)
            :  jspHandler.calculateRenderKitId(ctx);
    }

    /**
     * @see javax.faces.application.ViewHandler#getActionURL(javax.faces.context.FacesContext,
     *      java.lang.String)
     */
    public String getActionURL(FacesContext ctx, String viewId)
    {
        return (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isPageJSP(ctx, viewId))
            ? cbpHandler.getActionURL(ctx, viewId)
            : jspHandler.getActionURL(ctx, viewId);
    }

    /**
     * @see javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,
     *      java.lang.String)
     */
    public String getResourceURL(FacesContext ctx, String path)
    {
        return (!net.sourceforge.myfaces.application.cbp.SelectorUtils.isCurrentPageJSP(ctx))
            ? cbpHandler.getResourceURL(ctx, path)
            : jspHandler.getResourceURL(ctx, path);
    }
}