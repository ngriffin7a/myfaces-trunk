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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;
import java.util.Locale;
import java.io.IOException;

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
        this._stateManager = new StateManagerImpl();
        if (log.isTraceEnabled()) log.trace("New ViewHanldler instance created");
    }

    public Locale calculateLocale(FacesContext facescontext)
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public UIViewRoot createView(FacesContext facescontext, String s)
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public StateManager getStateManager()
    {
        return _stateManager;
    }

    public String getViewIdPath(FacesContext facescontext, String s)
    {
        throw new UnsupportedOperationException("not yet implemented.");

    }

    public void renderView(FacesContext facescontext, UIViewRoot uiviewroot) throws IOException, FacesException
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public UIViewRoot restoreView(FacesContext facescontext, String s)
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public void writeState(FacesContext facescontext) throws IOException
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }
}
