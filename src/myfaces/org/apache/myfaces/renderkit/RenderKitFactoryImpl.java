/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
package net.sourceforge.myfaces.renderkit;

import net.sourceforge.myfaces.renderkit.html.RenderKitImpl;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.Collections;
import java.util.Iterator;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class RenderKitFactoryImpl
        extends RenderKitFactory
{
    private static final Object _lock = new Object();
    private static RenderKit _renderKit = null;

    public void addRenderKit(String s, RenderKit renderkit)
    {
        throw new UnsupportedOperationException();
    }

    public RenderKit getRenderKit(String s)
            throws FacesException
    {
        if (!s.equals(DEFAULT_RENDER_KIT))
        {
            throw new IllegalArgumentException("Only default renderkit supported!");
        }
        synchronized (_lock)
        {
            if (_renderKit == null)
            {
                _renderKit = new RenderKitImpl();
            }
            return _renderKit;
        }
    }

    public RenderKit getRenderKit(String s, FacesContext facescontext)
            throws FacesException
    {
        return getRenderKit(s);
    }

    public Iterator getRenderKitIds()
    {
        return Collections.singleton(getRenderKit(DEFAULT_RENDER_KIT)).iterator();
    }
}
