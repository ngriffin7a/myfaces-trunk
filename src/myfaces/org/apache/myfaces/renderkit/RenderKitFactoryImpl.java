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
package net.sourceforge.myfaces.renderkit;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * RenderKitFactory implementation as defined in Spec. JSF.7.3
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RenderKitFactoryImpl
    extends RenderKitFactory
{
    private static final Object _lock = new Object();
    private static Map _renderkits = new HashMap();

    public RenderKitFactoryImpl()
    {
        //_renderkits.put(DEFAULT_RENDER_KIT, new RenderKitImpl());
    }

    public void addRenderKit(String id, RenderKit renderkit)
    {
        synchronized (_lock)
        {
            if (_renderkits.get(id) != null)
            {
                throw new IllegalArgumentException("Renderkit with id '" + id + "' already exists.");
            }
            _renderkits.put(id, renderkit);
        }
    }

    public RenderKit getRenderKit(String id)
            throws FacesException
    {
        synchronized (_lock)
        {
            RenderKit renderkit = (RenderKit)_renderkits.get(id);
            if (renderkit == null)
            {
                throw new IllegalArgumentException("Unknown renderkit '" + id + "'.");
            }
            return renderkit;
        }
    }

    public RenderKit getRenderKit(String s, FacesContext facescontext)
            throws FacesException
    {
        return getRenderKit(s);
    }

    public Iterator getRenderKitIds()
    {
        return _renderkits.keySet().iterator();
    }
}
