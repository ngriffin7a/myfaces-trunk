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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.renderkit.html.ext.NavigationItemRenderer;
import net.sourceforge.myfaces.renderkit.html.ext.NavigationRenderer;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.state.ZippingStateRenderer;

import javax.faces.component.UIComponent;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import java.util.*;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RenderKitImpl
        extends RenderKit
{
    private Map _renderers = new HashMap();

    public RenderKitImpl()
    {
        //Standard Renderkit
        addRenderer(new FormRenderer());
        addRenderer(new TextRenderer());
        addRenderer(new SecretRenderer());
        addRenderer(new ButtonRenderer());
        addRenderer(new HyperlinkRenderer());
        addRenderer(new DataRenderer());
        addRenderer(new ErrorsRenderer());
        addRenderer(new ListboxRenderer());
        addRenderer(new MenuRenderer());
        addRenderer(new ListRenderer());
        addRenderer(new GroupRenderer());

        //State Handling
        if (MyFacesConfig.isStateZipping())
        {
            addRenderer(new ZippingStateRenderer());
        }
        else
        {
            addRenderer(new StateRenderer());
        }

        //MyFaces Extensions
        addRenderer(new NavigationRenderer());
        addRenderer(new NavigationItemRenderer());
    }

    private void addRenderer(HTMLRenderer r)
    {
        _renderers.put(r.getRendererType(), r);
    }

    public void addComponentClass(Class class1)
    {
        throw new UnsupportedOperationException();
    }

    public void addRenderer(String s, Renderer renderer)
    {
        _renderers.put(s, renderer);
    }

    public Iterator getComponentClasses()
    {
        throw new UnsupportedOperationException();
    }

    public Renderer getRenderer(String rendererType)
    {
        Renderer renderer = (Renderer)_renderers.get(rendererType);
        if (renderer == null)
        {
            throw new IllegalArgumentException("Unsupported renderer type: " + rendererType);
        }
        return renderer;
    }

    public Iterator getRendererTypes()
    {
        return getRendererTypes((String)null);
    }

    public Iterator getRendererTypes(String componentType)
    {
        List lst = new ArrayList();
        Iterator it = _renderers.entrySet().iterator();
        while (it.hasNext())
        {
            HTMLRenderer r = (HTMLRenderer)it.next();
            if (componentType == null ||
                r.supportsComponentType(componentType))
            {
                lst.add(r.getRendererType());
            }
        }
        return lst.iterator();
    }

    public Iterator getRendererTypes(UIComponent uicomponent)
    {
        return getRendererTypes(uicomponent.getComponentType());
    }
}
