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
package net.sourceforge.myfaces.renderkit.callback;

import javax.faces.component.UIComponent;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Special RenderKit that is installed instead of the current RenderKit when a CallbackRenderer
 * is added to the CallbackSupport via {@link CallbackSupport#addCallbackRenderer}.
 * It's purpose is to route all {@link #getRenderer} calls to a {@link RendererWrapper},
 * that does the callback and then calls the encodeXxx method of the original Renderer.
 *
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class CallbackRenderKit
    extends RenderKit
{
    protected static final String ID = CallbackRenderKit.class.getName() + ".ID";

    private Map _rendererWrappers = new HashMap();

    public CallbackRenderKit()
    {
    }

    public Renderer getRenderer(String rendererType)
    {
        Renderer rendererWrapper = (Renderer)_rendererWrappers.get(rendererType);
        if (rendererWrapper == null)
        {
            rendererWrapper = new RendererWrapper(rendererType);
            _rendererWrappers.put(rendererType, rendererWrapper);
        }
        return rendererWrapper;
    }

    public void addRenderer(String s, Renderer renderer)
    {
        throw new UnsupportedOperationException();
    }

    /*
    public void addComponentClass(Class aClass)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getComponentClasses()
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getRendererTypes()
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getRendererTypes(String s)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getRendererTypes(UIComponent uiComponent)
    {
        throw new UnsupportedOperationException();
    }
    */

}
