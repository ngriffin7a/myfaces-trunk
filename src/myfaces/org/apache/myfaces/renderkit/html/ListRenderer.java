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

import net.sourceforge.myfaces.renderkit.attr.ListRendererAttributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ListRenderer
        extends AbstractPanelRenderer
        implements ListRendererAttributes
{
    public static final String TYPE = "List";

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIPanel.TYPE);
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        if (uicomponent.getRendererType().equals(TYPE))
        {
            pushListComponent(context, uicomponent);

            writer.write("<table");
            String style = (String)uicomponent.getAttribute(PANEL_CLASS_ATTR);
            if (style != null && style.length() > 0)
            {
                writer.write(" class=\"");
                writer.write(style);
                writer.write("\"");
            }
            writer.write(">\n");

            storeRenderKit(context, uicomponent);
        }
        else
        {
            UIComponent parent = uicomponent.getParent();
            if ((parent.getRendererType().equals(DataRenderer.TYPE) ||
                parent.getRendererType().equals(GroupRenderer.TYPE)) &&
                parent.getParent().getRendererType().equals(TYPE))
            {
                openNewColumn(context);
            }

            encodeBeginWithOriginalRenderer(context, uicomponent);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uicomponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (uicomponent.getRendererType().equals(TYPE))
        {
            popListComponent(facesContext);

            writer.write("</table>\n");
            restoreRenderKit(facesContext, uicomponent);
        }
        else
        {
            encodeEndWithOriginalRenderer(facesContext, uicomponent);

            UIComponent parent = uicomponent.getParent();
            if ((parent.getRendererType().equals(DataRenderer.TYPE) ||
                parent.getRendererType().equals(GroupRenderer.TYPE)) &&
                parent.getParent().getRendererType().equals(TYPE))
            {
                writer.write("</td>\n");
            }
        }
    }


    /**
     * Delegates all method-calls except {@link #getRenderer} to the renderKit
     * given in the constructor.
     */
    public static class ListRenderKitImpl
        extends RenderKit
    {
        public static final String ID = ListRenderKitImpl.class.getName();

        private RenderKit _renderKit;
        private Map _renderers = new HashMap();
        private Renderer _defaultRenderer;

        public ListRenderKitImpl(RenderKit renderKit)
        {
            HTMLRenderer renderer = new ListRenderer();
            _renderers.put(renderer.getRendererType(), renderer);
            _defaultRenderer = renderer;

            renderer = new JspDataRenderer();
            _renderers.put(renderer.getRendererType(), renderer);

            renderer = new JspGroupRenderer();
            _renderers.put(renderer.getRendererType(), renderer);

            _renderKit = renderKit;
        }

        public void addComponentClass(Class aClass)
        {
            _renderKit.addComponentClass(aClass);
        }

        public void addRenderer(String s, Renderer renderer)
        {
            _renderKit.addRenderer(s, renderer);
        }

        public Iterator getComponentClasses()
        {
            return _renderKit.getComponentClasses();
        }

        /**
         * Always returns one out of the following Renderers:<br>
         * ListRenderer,<br>
         * JspDataRenderer,<br>
         * JspGroupRenderer<br>
         * Default is ListRenderer
         * @param s
         * @return JspListRenderer
         */
        public Renderer getRenderer(String s)
        {
            Renderer r = (Renderer)_renderers.get(s);
            return r != null ? r : _defaultRenderer;
        }

        public Iterator getRendererTypes()
        {
            return _renderKit.getRendererTypes();
        }

        public Iterator getRendererTypes(String s)
        {
            return _renderKit.getRendererTypes(s);
        }

        public Iterator getRendererTypes(UIComponent component)
        {
            return _renderKit.getRendererTypes(component);
        }
    }
}


