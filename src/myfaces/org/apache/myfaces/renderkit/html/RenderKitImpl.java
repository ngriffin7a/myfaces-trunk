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
import net.sourceforge.myfaces.renderkit.html.ext.*;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RenderKitImpl
    extends RenderKit
{
    private Map _renderers = null;

    public RenderKitImpl()
    {
    }

    private void initAndAddRenderers()
    {
        _renderers = new HashMap();

        //Standard Renderkit
        _addRenderer(new FormRenderer());
        _addRenderer(new TextRenderer());
        _addRenderer(new DateRenderer());
        _addRenderer(new DateTimeRenderer());
        _addRenderer(new TimeRenderer());
        _addRenderer(new MessageRenderer());
        _addRenderer(new SecretRenderer());
        _addRenderer(new ButtonRenderer());
        _addRenderer(new HyperlinkRenderer());
        _addRenderer(new ErrorsRenderer());
        _addRenderer(new ImageRenderer());
        _addRenderer(new ListboxRenderer());
        _addRenderer(new MenuRenderer());
        _addRenderer(new ListRenderer());
        _addRenderer(new DataRenderer());
        _addRenderer(new GroupRenderer());
        _addRenderer(new GridRenderer());
        _addRenderer(new LabelRenderer());
        _addRenderer(new CheckboxRenderer());
        _addRenderer(new HiddenRenderer());
        _addRenderer(new TextareaRenderer());

        //MyFaces Extensions
        _addRenderer(new NavigationRenderer());
        _addRenderer(new NavigationItemRenderer());
        _addRenderer(new SortColumnRenderer());
        _addRenderer(new LayoutRenderer());
        _addRenderer(new FileUploadRenderer());
    }

    private void _addRenderer(HTMLRenderer r)
    {
        _renderers.put(r.getRendererType(), r);
    }


    public void addRenderer(String s, Renderer renderer)
    {
        if (_renderers == null) initAndAddRenderers();
        _renderers.put(s, renderer);
    }

    public Renderer getRenderer(String rendererType)
    {
        if (_renderers == null) initAndAddRenderers();
        Renderer renderer = (Renderer)_renderers.get(rendererType);
        if (renderer == null)
        {
            if (rendererType.equals(StateRenderer.TYPE))
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                if (facesContext != null)
                {
                    int mode = MyFacesConfig.getStateSavingMode(((ServletContext)facesContext.getExternalContext().getContext()));
                    renderer = new StateRenderer(mode);
                    addRenderer(StateRenderer.TYPE, renderer);
                    return renderer;
                }
            }

            throw new IllegalArgumentException("Unsupported renderer type: " + rendererType);
        }
        return renderer;
    }


    /*
    public Iterator getRendererTypes()
    {
        return getRendererTypes((String)null);
    }

    public Iterator getRendererTypes(String componentType)
    {
        if (_renderers == null) initAndAddRenderers();
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



    public void addComponentClass(Class class1)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getComponentClasses()
    {
        throw new UnsupportedOperationException();
    }
    */


}
