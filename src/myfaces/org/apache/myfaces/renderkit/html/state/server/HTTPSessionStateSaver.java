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
package net.sourceforge.myfaces.renderkit.html.state.server;

import net.sourceforge.myfaces.renderkit.html.state.StateSaver;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HTTPSessionStateSaver
    implements StateSaver
{
    protected static final String TREE_SESSION_ATTR
        = HTTPSessionStateSaver.class.getName() + ".TREE";
    protected static final String LOCALE_SESSION_ATTR
        = HTTPSessionStateSaver.class.getName() + ".LOCALE";

    public void init(FacesContext facesContext) throws IOException
    {
    }

    public void encodeState(FacesContext facesContext, int encodingType) throws IOException
    {
    }

    public void release(FacesContext facesContext) throws IOException
    {
        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
        HttpSession session = request.getSession(true);
        session.setAttribute(TREE_SESSION_ATTR, facesContext.getTree());
        session.setAttribute(LOCALE_SESSION_ATTR, facesContext.getLocale());
    }
}
