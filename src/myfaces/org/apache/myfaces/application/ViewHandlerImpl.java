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

package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ViewHandlerImpl
    implements ViewHandler
{
    private static final Log log = LogFactory.getLog(ViewHandlerImpl.class);

    private StateManager _stateManager;

    public ViewHandlerImpl()
    {
        _stateManager = new StateManagerImpl();
        if (log.isTraceEnabled()) log.trace("New ViewHanldler instance created");
    }

    public Locale calculateLocale(FacesContext facescontext)
    {
        Enumeration locales = ((ServletRequest)facescontext.getExternalContext().getRequest()).getLocales();
        while (locales.hasMoreElements())
        {
            Locale locale = (Locale) locales.nextElement();
            for (Iterator it = facescontext.getApplication().getSupportedLocales(); it.hasNext();)
            {
                Locale supportLocale = (Locale)it.next();
                // higher priority to a langauage match over an exact match
                // that occures further down (see Jstl Reference 1.0 8.3.1)
                if (locale.getLanguage().equals(supportLocale.getLanguage()) &&
                    (supportLocale.getCountry() == null ||
                    supportLocale.getCountry().length() == 0))
                {
                    return locale;
                }
                else if (supportLocale.equals(locale))
                {
                    return locale;
                }
            }
        }

        Locale locale = facescontext.getApplication().getDefaultLocale();
        return locale != null ? locale : Locale.getDefault();
    }

    public UIViewRoot createView(FacesContext facescontext, String viewId)
    {
        UIViewRoot uiViewRoot = new UIViewRoot();
        uiViewRoot.setViewId(viewId);
        uiViewRoot.setLocale(calculateLocale(facescontext));
        if (log.isTraceEnabled()) log.trace("Created view " + viewId);
        return uiViewRoot;
    }

    public StateManager getStateManager()
    {
        return _stateManager;
    }

    public String getViewIdPath(FacesContext facescontext, String s)
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");

    }

    public void renderView(FacesContext facesContext, UIViewRoot viewRoot) throws IOException, FacesException
    {
        // TODO: adapt
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletRequest servletRequest = (ServletRequest)externalContext.getRequest();

        //Build component tree from parsed JspInfo so that all components
        //already exist in case a component needs it's children prior to
        //rendering it's body
        /*
        Tree staticTree = JspInfo.getTree(facesContext, tree.getTreeId());
        TreeCopier tc = new TreeCopier(facesContext);
        tc.setOverwriteComponents(false);
        tc.setOverwriteAttributes(false);
        tc.copyTree(staticTree, tree);
        */

        //Look for a StateRenderer and prepare for state saving
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(viewRoot.getRenderKitId());
        Renderer renderer = null;
        try
        {
            renderer = renderKit.getRenderer(StateRenderer.TYPE);
        }
        catch (Exception e)
        {
            //No StateRenderer
        }
        if (renderer != null)
        {
            try
            {
                log.trace("StateRenderer found, calling encodeBegin.");
                renderer.encodeBegin(facesContext, null);
            }
            catch (IOException e)
            {
                throw new FacesException("Error saving state", e);
            }
        }

        //forward request to JSP page
        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(externalContext);
        ServletMapping sm = smf.getServletMapping((ServletContext)externalContext.getContext());
        String forwardURL = sm.mapViewIdToFilename((ServletContext)externalContext.getContext(),
                                                   viewRoot.getViewId());

        RequestDispatcher requestDispatcher
            = servletRequest.getRequestDispatcher(forwardURL);
        try
        {
            requestDispatcher.forward(servletRequest,
                                      (ServletResponse)facesContext.getExternalContext().getResponse());
        }
        catch(IOException ioe)
        {
            log.error("IOException in method renderView of class " + this.getClass().getName(), ioe);
            throw new IOException(ioe.getMessage());
        }
        catch(ServletException se)
        {
            log.error("ServletException in method renderView of class " + this.getClass().getName(), se);
            throw new FacesException(se.getMessage(), se);
        }

    }

    public UIViewRoot restoreView(FacesContext facescontext, String s)
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public void writeState(FacesContext facescontext) throws IOException
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }
}
